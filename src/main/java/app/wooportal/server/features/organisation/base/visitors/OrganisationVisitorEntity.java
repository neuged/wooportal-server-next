package app.wooportal.server.features.organisation.base.visitors;

import javax.persistence.Entity;
import javax.persistence.Table;
import app.wooportal.server.core.visit.visitable.VisitableEntity;
import app.wooportal.server.features.organisation.base.OrganisationEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "organisation_visitors")
public class OrganisationVisitorEntity extends VisitableEntity<OrganisationEntity> {

  private static final long serialVersionUID = 1L;
}
