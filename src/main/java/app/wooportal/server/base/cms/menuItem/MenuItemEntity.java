package app.wooportal.server.base.cms.menuItem;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import app.wooportal.server.base.cms.feature.FeatureEntity;
import app.wooportal.server.base.cms.menuItem.translations.MenuItemTranslatableEntity;
import app.wooportal.server.base.cms.page.PageEntity;
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
@Table(name = "menu_items")
public class MenuItemEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private Boolean header;
  
  private Integer order;

  @ManyToOne(fetch = FetchType.LAZY)
  private FeatureEntity feature;

  @ManyToOne(fetch = FetchType.LAZY)
  private PageEntity page;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private MenuItemEntity parent;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  private Set<MenuItemEntity> subMenuItems;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
  private Set<MenuItemTranslatableEntity> translatables;
}
