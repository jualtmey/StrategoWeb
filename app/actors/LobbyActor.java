package actors;

import akka.actor.*;
import org.pac4j.core.profile.CommonProfile;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class LobbyActor extends UntypedActor {

    public static Props props(ActorSystem actorSystem) {
        return Props.create(LobbyActor.class, actorSystem);
    }

    private ActorSystem actorSystem;

    private Lobby lobby;

    public LobbyActor(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        lobby = new Lobby();
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof LobbyProtocol.Join) {
            CommonProfile profile = ((LobbyProtocol.Join) message).profile;
            lobby.join(new User(profile, sender()));

            notifyUsersInLobby();
        } else if (message instanceof LobbyProtocol.Leave) {
           // TODO:
        }
    }

    public void notifyUsersInLobby() {
        for (ActorRef userActor: lobby.getAllUserActors()) {
            userActor.tell(new LobbyProtocol.Refresh(lobby.toJson()), self());
        }
    }

}