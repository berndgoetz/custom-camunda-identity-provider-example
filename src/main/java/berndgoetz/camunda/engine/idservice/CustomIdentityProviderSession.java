package berndgoetz.camunda.engine.idservice;

import org.camunda.bpm.engine.BadUserRequestException;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomIdentityProviderSession implements ReadOnlyIdentityProvider {

    private final IdentityService identityService;

    public CustomIdentityProviderSession(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public User findUserById(String userId) {
        return identityService.findUserById(userId);
    }

    @Override
    public UserQuery createUserQuery() {
        return new CustomUserQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public UserQuery createUserQuery(CommandContext context) {
        return new CustomUserQuery(context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        throw new BadUserRequestException("not supported");
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        if(userId == null || password == null || userId.isEmpty() || password.isEmpty())
            return false;
        User user = findUserById(userId);
        if(user == null)
            return false;
        return user.getPassword().equals(password);
    }

    @Override
    public Group findGroupById(String groupId) {
        return identityService.findGroupById(groupId);
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new CustomGroupQuery(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());

    }

    @Override
    public GroupQuery createGroupQuery(CommandContext context) {
        return new CustomGroupQuery(context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public Tenant findTenantById(String tenantId) {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery() {
        return new CustomTenant(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        return new CustomTenant();
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {
    }

    public long findUserCountByQueryCriteria(CustomUserQuery query) {
        return identityService.countUsers(query);
    }

    public long findGroupCountByQueryCriteria(CustomGroupQuery query) {
        return identityService.countGroups(query);
    }

    public List<Group> findGroupByQueryCriteria(CustomGroupQuery query, Page page) {
        return identityService.findGroupByQueryCriteria(query, page);
    }

    public List<User> findUserByQueryCriteria(CustomUserQuery query, Page page) {
        return identityService.findUserByQueryCriteria(query, page);
    }

}
