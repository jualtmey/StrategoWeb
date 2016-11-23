package controllers;

import actors.LobbyActor;
import actors.WebSocketActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.htwg.stratego.StrategoModule;
import de.htwg.stratego.controller.IMultiDeviceStrategoController;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.strategoWuiWebsocket;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebSocketController extends Controller {

    public static ActorRef lobby;

    @Inject public WebSocketController(ActorSystem actorSystem) {
        lobby = actorSystem.actorOf(LobbyActor.props(actorSystem));
    }

    public Result strategoWui() {
        Injector injector = Guice.createInjector(new StrategoModule());
        IMultiDeviceStrategoController strategoController = injector.getInstance(IMultiDeviceStrategoController.class);
        return ok(strategoWuiWebsocket.render(strategoController));
    }

    public LegacyWebSocket<String> socket() {
        return WebSocket.withActor(WebSocketActor::props);
    }
    
}
