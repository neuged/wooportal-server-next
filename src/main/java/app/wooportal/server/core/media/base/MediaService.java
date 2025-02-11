package app.wooportal.server.core.media.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import app.wooportal.server.core.base.DataService;
import app.wooportal.server.core.error.exception.BadParamsException;
import app.wooportal.server.core.error.exception.NotFoundException;
import app.wooportal.server.core.media.image.ImageService;
import app.wooportal.server.core.media.storage.StorageService;
import app.wooportal.server.core.repository.DataRepository;

@Service
public class MediaService extends DataService<MediaEntity, MediaPredicateBuilder> {

  private final List<String> imageFormats = List.of("bmp", "gif", "png", "tiff", "tiff", "jpeg");

  private final ImageService imageService;

  private final StorageService storageService;

  public MediaService(DataRepository<MediaEntity> repo, MediaPredicateBuilder predicate,
      ImageService imageService, StorageService storageService) {
    super(repo, predicate);

    this.imageService = imageService;
    this.storageService = storageService;
  }

  @Override
  public void postSave(MediaEntity entity, MediaEntity newEntity, JsonNode context) {
    if (newEntity.getBase64() != null && !newEntity.getBase64().isBlank()) {
      byte[] data = Base64.getDecoder().decode(newEntity.getBase64());
      var formatType = MediaHelper.extractFormatFromMimeType(newEntity.getMimeType());
      if (isImage(newEntity.getMimeType())) {
        data = imageService.resize(data, formatType);
      }

      if (entity.getId() != null && !entity.getId().isBlank()) {
        storageService.delete(entity.getId(), formatType);
      }
      try {
        storageService.store(entity.getId(), formatType, data);
      } catch (IOException e) {

      }
    }
  }

  private boolean isImage(String mimeType) {
    return imageFormats.stream().anyMatch(format -> mimeType.toLowerCase().contains(format));
  }

  public ResponseEntity<byte[]> download(String id) throws IOException {
    var result = repo.findOne(singleQuery(predicate.withId(id)));

    if (result.isEmpty()) {
      throw new NotFoundException("media does not exist", id);
    }
    var formatType = MediaHelper.extractFormatFromMimeType(result.get().getMimeType());
    return ResponseEntity.ok().headers(createHeader(result.get().getName(), formatType))
        .contentType(MediaType.parseMediaType(result.get().getMimeType()))
        .body(storageService.read(id, formatType));
  }

  public ResponseEntity<byte[]> getMedia(String id) throws IOException {
    var result = repo.findOne(singleQuery(predicate.withId(id)));

    if (result.isEmpty()) {
      throw new NotFoundException("media does not exist", id);
    }
    var formatType = MediaHelper.extractFormatFromMimeType(result.get().getMimeType());
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(result.get().getMimeType()))
        .body(storageService.read(id, formatType));
  }

  public ResponseEntity<byte[]> export(MediaHtmlDto content) throws Exception {

    switch (content.getType()) {
      case pdf -> {
        return exportPdf(content);
      }
      case docx -> {
        return exportDocx(content);
      }
      default -> throw new BadParamsException(
          "content type: " + content.getType() + " is not provided");
    }
  }

  public ResponseEntity<byte[]> exportPdf(MediaHtmlDto content) {

    try (var os = new ByteArrayOutputStream()) {
      var builder = new PdfRendererBuilder();
      builder.toStream(os);
      builder.withW3cDocument(new W3CDom().fromJsoup(getHtmlDocument(content)), "/");
      builder.run();
      return ResponseEntity.ok().headers(createHeader(content.getName(), "pdf"))
          .contentType(MediaType.APPLICATION_PDF).body(os.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseEntity<byte[]> exportDocx(MediaHtmlDto content) throws Exception {

    var wordMLPackage = WordprocessingMLPackage.createPackage();
    var XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
    wordMLPackage.getMainDocumentPart().getContent()
        .addAll(XHTMLImporter.convert(getHtmlDocument(content).html(), null));
    
    try (var os = new ByteArrayOutputStream()) {
      wordMLPackage.save(os);
      
      return ResponseEntity.ok().headers(createHeader(content.getName(), "docx"))
          .body(os.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private Document getHtmlDocument(MediaHtmlDto content) {
    var document = Jsoup.parse(content.getHtml(), "UTF-8");
    document.outputSettings()
      .syntax(Document.OutputSettings.Syntax.xml)
      .escapeMode(EscapeMode.xhtml);
    return document;
  }

  public HttpHeaders createHeader(String name, String formatType) {
    HttpHeaders header = new HttpHeaders();
    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name + "." + getFormatType(formatType));
    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
    header.add("Pragma", "no-cache");
    header.add("Expires", "0");
    return header;
  }

  private String getFormatType(String formatType) {
    return switch (formatType) {
      case "vnd.oasis.opendocument.text" -> "odt";
      case "vnd.oasis.opendocument.text-template" -> "ott";
      case "vnd.oasis.opendocument.text-web" -> "oth";
      case "vnd.oasis.opendocument.text-master" -> "odm";
      case "vnd.oasis.opendocument.graphics" -> "odg";
      case "vnd.oasis.opendocument.graphics-template" -> "otg";
      case "vnd.oasis.opendocument.presentation" -> "odp";
      case "vnd.oasis.opendocument.presentation-template" -> "otp";
      case "vnd.oasis.opendocument.spreadsheet" -> "ods";
      case "vnd.oasis.opendocument.spreadsheet-template" -> "ots";
      case "vnd.oasis.opendocument.chart" -> "odc";
      case "vnd.oasis.opendocument.formula" -> "odf";
      case "vnd.oasis.opendocument.database" -> "odb";
      case "vnd.oasis.opendocument.image" -> "odi";
      case "vnd.openofficeorg.extension" -> "oxt";
      default -> formatType;
    };
  }
}

