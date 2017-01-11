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
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class WebSocketController extends Controller {

    public static ActorRef lobby;

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

    public LegacyWebSocket<String> socket() {
        return WebSocket.withActor((out) -> WebSocketActor.props(out, "hans"));
    }

    @Secure(clients = "OidcClient")
    public Result lobby() {
        System.out.println("<<<<<<<<<. - .>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<lobby wurde aufgerufen!!! TEST"); // TODO test
        return ok(views.html.lobby.render(getProfiles()));
    }

    private List<CommonProfile> getProfiles() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        System.out.println(profileManager.get(true));ß
        return profileManager.getAll(true);
    }
}
