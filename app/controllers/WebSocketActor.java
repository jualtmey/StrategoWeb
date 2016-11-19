package controllers;

import akka.actor.*;

public class WebSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(WebSocketActor.class, out);
    }

    private final ActorRef out;
    private final ActorRef lobby;

    public WebSocketActor(ActorRef out) {
        this.out = out;
        this.lobby = controllers.HomeController.lobby;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            message = (String) message;
            if (message.equals("lobby")) {
                lobby.tell(new LobbyProtocol.Join("unnamed"), self());
            } else if (message.equals("entered")) {
                //out.tell("entered lobby", self());
            } else if (message.equals("new")) {
                //out.tell("new client in lobby", self());
            }
        } else if (message instanceof GameProtocol.Refresh) {
            GameProtocol.Refresh refresh = (GameProtocol.Refresh) message;
            out.tell(refresh.json, self());
        }
        
    }
    
    public void postStop() {
        lobby.tell(new LobbyProtocol.Leave(), self());
        System.out.println("websocket closed");
    }

}