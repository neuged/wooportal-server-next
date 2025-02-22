package app.wooportal.server.features.survey.questionOption;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import app.wooportal.server.core.base.BaseEntity;
import app.wooportal.server.features.survey.question.QuestionEntity;
import app.wooportal.server.features.survey.questionOption.translations.QuestionOptionTranslatableEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Entity
@Table(name = "question_options")
@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
public class QuestionOptionEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Integer order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private QuestionEntity question;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  private Set<QuestionOptionTranslatableEntity> translatables;
}
