package app.wooportal.server.base.configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import app.wooportal.server.core.base.BaseEntity;
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
@Table(name = "configurations")
public class ConfigurationEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(
      nullable = false,
      unique = true)
  private String key;
  
  @Column(nullable = false)
  private String value;

}
