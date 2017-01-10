package modules;

import com.google.inject.AbstractModule;
import controllers.authentication.CustomAuthorizer;
import controllers.authentication.HttpActionAdapter;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.play.store.PlayCacheStore;
import org.pac4j.play.store.PlaySessionStore;
import play.Configuration;
import play.Environment;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;

public class SecurityModule extends AbstractModule {

    private final Configuration configuration;

    public SecurityModule(final Environment environment, final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void configure() {

        bind(PlaySessionStore.class).to(PlayCacheStore.class);
        final String baseUrl = configuration.getString("baseUrl");

        // OpenID Connect Google
        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId("659727346044-62m661chrbo1174cb099fkf63o41anf6.apps.googleusercontent.com");
        oidcConfiguration.setSecret("dl-K_RiDSLYhKMLw0Hj2NVcy");
        oidcConfiguration.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration");
        oidcConfiguration.addCustomParam("prompt", "consent");
        final OidcClient oidcClient = new OidcClient(oidcConfiguration);
        oidcClient.addAuthorizationGenerator(profile -> profile.addRole("ROLE_ADMIN"));

        final Clients clients = new Clients(baseUrl + "/callback", oidcClient);

        final Config config = new Config(clients);
        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer<>("ROLE_ADMIN"));
        config.addAuthorizer("custom", new CustomAuthorizer());
        config.setHttpActionAdapter(new HttpActionAdapter());
        bind(Config.class).toInstance(config);


    }
}
