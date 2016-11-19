package controllers;

public final class LobbyProtocol {
    
    public static final class Join {
        public final String name;
        
        public Join(String name) {
            this.name = name;
        }
    }
    
    public static final class Leave {
        
    }
    
}