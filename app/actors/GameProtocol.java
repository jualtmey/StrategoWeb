package actors;

public final class GameProtocol {
    
    public static final class NewGame {

    }
    
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

    public static final class Swap {
        public final int fromRow;
        public final int fromColumn;
        public final int toRow;
        public final int toColumn;

        public Swap(int fromRow, int fromColumn, int toRow, int toColumn) {
            this.fromRow = fromRow;
            this.fromColumn = fromColumn;
            this.toRow = toRow;
            this.toColumn = toColumn;
        }
    }

    public static final class Remove {
        public final int row;
        public final int column;

        public Remove(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static final class Move {
        public final int fromRow;
        public final int fromColumn;
        public final int toRow;
        public final int toColumn;

        public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
            this.fromRow = fromRow;
            this.fromColumn = fromColumn;
            this.toRow = toRow;
            this.toColumn = toColumn;
        }
    }

    public static final class Finish {

    }

    public static final class Refresh {
        public final String json;
        
        public Refresh(String json) {
            this.json = json;
        }
    }
    
}