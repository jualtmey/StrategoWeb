package controllers;

import akka.actor.*;

import java.util.List;
import java.util.ArrayList;

public class LobbyActor extends UntypedActor {

    public static Props props(ActorSystem actorSystem) {
        return Props.create(LobbyActor.class, actorSystem);
    }

    private ActorSystem actorSystem;

    private List<ActorRef> clients = new ArrayList<>();

    public LobbyActor(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof LobbyProtocol.Join) {
            sender().tell("entered", self());
            clients.forEach(client -> client.tell("new", self()));
            clients.add(sender());
            
            if (clients.size() == 2) {
                actorSystem.actorOf(GameActor.props(clients.get(0), clients.get(1)));
            }
        } else if (message instanceof LobbyProtocol.Leave) {
            clients.remove(sender());
        }
    }

}