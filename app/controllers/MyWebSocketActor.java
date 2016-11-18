package controllers;

import akka.actor.*;

public class MyWebSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(MyWebSocketActor.class, out);
    }

    private final ActorRef out;

    public MyWebSocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            message = (String) message;
            if (message.equals("lobby")) {
                controllers.HomeController.lobby.tell(new LobbyProtocol.Join("unnamed"), self());
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

}