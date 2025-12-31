package berndgoetz.camunda.engine.idservice;

import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.UserQueryImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

import java.util.List;

public class CustomUserQuery extends UserQueryImpl {

    public CustomUserQuery(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        var provider = (CustomIdentityProviderSession)commandContext.getReadOnlyIdentityProvider();
        return provider.findUserCountByQueryCriteria(this);
    }

    @Override
    public List<User> executeList(CommandContext commandContext, Page page) {
        var provider = (CustomIdentityProviderSession)commandContext.getReadOnlyIdentityProvider();
        return provider.findUserByQueryCriteria(this, page);
    }

}
