package controllers;

public final class GameProtocol {
    
    public static final class Add {
        public final int row;
        public final int column;
        public final int rank;
        
        public Add(int row, int column, int rank) {
            this.row = row;
            this.column = column;
            this.rank = rank;
        }
    }
    
    public static final class Refresh {
        public final String json;
        
        public Refresh(String json) {
            this.json = json;
        }
    }
    
}