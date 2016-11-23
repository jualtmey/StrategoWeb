package actors;

import akka.actor.ActorRef;

public final class LobbyProtocol {
    
    public static final class Join {
        public final String name;
        
        public Join(String name) {
            this.name = name;
        }
    }
    
    public static final class Leave {
        
    }

    public static final class NewGame {
        public final ActorRef game;

        public NewGame(ActorRef game) {
            this.game = game;
        }
    }
    
}