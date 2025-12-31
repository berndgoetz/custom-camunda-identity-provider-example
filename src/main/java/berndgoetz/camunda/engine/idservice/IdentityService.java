package berndgoetz.camunda.engine.idservice;

import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.Page;

import java.util.List;

public interface IdentityService {

    User findUserById(String userId);

    Group findGroupById(String groupId);

    long countUsers(CustomUserQuery query);

    long countGroups(CustomGroupQuery query);

    List<User> findUserByQueryCriteria(CustomUserQuery query, Page page);

    List<Group> findGroupByQueryCriteria(CustomGroupQuery query, Page page);
}
