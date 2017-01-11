package controllers;

import actors.LobbyActor;
import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.htwg.stratego.StrategoModule;
import de.htwg.stratego.controller.IMultiDeviceStrategoController;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.api.mvc.RequestHeader;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.HttpCookie;
import java.util.List;

@Singleton
public class WebSocketController extends Controller {

    public static ActorRef lobby;
    private Http.Context lastContext;

    @Inject
    public WebSocketController(ActorSystem actorSystem) {
        lobby = actorSystem.actorOf(LobbyActor.props(actorSystem));
    }

    @Inject
    private PlaySessionStore playSessionStore;

    public Result strategoWui() {
        Injector injector = Guice.createInjector(new StrategoModule());
        IMultiDeviceStrategoController strategoController = injector.getInstance(IMultiDeviceStrategoController.class);
        return ok(views.html.strategoWuiWebsocket.render(strategoController));
    }

    @Secure(clients = "OidcClient")
    public LegacyWebSocket<String> socket() {
        LegacyWebSocket socket = WebSocket.withActor((out) -> WebSocketActor.props(out, getProfile(lastContext)));
        return socket;
    }

    @Secure(clients = "OidcClient")
    public Result lobby() {
        lastContext = new Http.Context(request());
        return ok(views.html.lobby.render());
    }

    private CommonProfile getProfile(Http.Context ctx) {
        final PlayWebContext context = new PlayWebContext(ctx, playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true).get();
    }
}
