package app.wooportal.server.features.survey.question;

import org.springframework.stereotype.Repository;
import app.wooportal.server.core.repository.DataRepository;

@Repository
public interface QuestionRepository extends DataRepository<QuestionEntity> {

}
