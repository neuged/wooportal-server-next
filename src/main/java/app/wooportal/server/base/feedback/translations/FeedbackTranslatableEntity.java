package app.wooportal.server.base.feedback.translations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import app.wooportal.server.base.feedback.base.FeedbackEntity;
import app.wooportal.server.core.i18n.entities.TranslatableEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "feedback_translatables")
public class FeedbackTranslatableEntity extends TranslatableEntity<FeedbackEntity> {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String content;

}
