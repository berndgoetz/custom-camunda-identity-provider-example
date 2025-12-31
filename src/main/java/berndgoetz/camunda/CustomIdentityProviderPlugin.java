package berndgoetz.camunda;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.SessionFactory;
import org.camunda.bpm.engine.spring.SpringProcessEnginePlugin;
import org.springframework.stereotype.Component;

@Component
public class CustomIdentityProviderPlugin extends SpringProcessEnginePlugin {

    private final SessionFactory identitySessionFactory;

    public CustomIdentityProviderPlugin(SessionFactory identitySessionFactory) {
        this.identitySessionFactory = identitySessionFactory;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl cfg) {
        // This overrides the default DB-backed identity provider
        cfg.setIdentityProviderSessionFactory(identitySessionFactory);
    }

}
