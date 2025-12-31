package berndgoetz.camunda.engine.idservice;

import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.Session;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomIdentitySessionFactory implements SessionFactory {

    private final CustomIdentityProviderSession session;

    public CustomIdentitySessionFactory(CustomIdentityProviderSession session) {
        this.session = session;
    }

    @Override
    public Class<?> getSessionType() {
        return ReadOnlyIdentityProvider.class;
    }

    @Override
    public Session openSession() {
        return session;
    }

}
