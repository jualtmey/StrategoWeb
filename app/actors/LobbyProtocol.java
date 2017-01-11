package actors;

import akka.actor.ActorRef;
import org.pac4j.core.profile.CommonProfile;

import java.util.List;

public final class LobbyProtocol {
    
    public static final class Join {
        public final CommonProfile profile;
        
        public Join(CommonProfile profile) {
            this.profile = profile;
        }
    }
    
    public static final class Leave {
        
    }

    public static final class Refresh {
        public final String json;

        public Refresh(String json) {
            this.json = json;
        }
    }

    public static final class NewGame {
        public final ActorRef game;

        public NewGame(ActorRef game) {
            this.game = game;
        }
    }
    
}