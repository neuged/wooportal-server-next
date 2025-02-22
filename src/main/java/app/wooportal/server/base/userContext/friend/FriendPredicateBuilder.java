package app.wooportal.server.base.userContext.friend;

import org.springframework.stereotype.Service;
import com.querydsl.core.types.dsl.BooleanExpression;
import app.wooportal.server.core.base.PredicateBuilder;

@Service
public class FriendPredicateBuilder extends PredicateBuilder<QFriendEntity, FriendEntity> {

  public FriendPredicateBuilder() {
    super(QFriendEntity.friendEntity);
  }

  @Override
  public BooleanExpression freeSearch(String term) {
    return null;
  }
}
