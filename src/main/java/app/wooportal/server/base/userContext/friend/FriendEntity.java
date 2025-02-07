package app.wooportal.server.base.userContext.friend;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import app.wooportal.server.base.userContext.base.UserContextEntity;
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
@Table(name = "friends")
public class FriendEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private Boolean accepted;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserContextEntity addressee;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserContextEntity requester;
  
}
