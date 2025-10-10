// package chess;

public class ChessBoard {
    private static final char[] FILES = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static final int[] RANKS = {1, 2, 3, 4, 5, 6, 7, 8};
    
    public enum PieceType {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }
    
    public enum Color {
        WHITE, BLACK
    }
    
    public static class ChessPiece {
        private final PieceType type;
        private final Color color;
        private boolean hasMoved; 
        
        public ChessPiece(PieceType type, Color color) {
            this.type = type;
            this.color = color;
            this.hasMoved = false;
        }
        
        public PieceType getType() {
            return type;
        }
        
        public Color getColor() {
            return color;
        }
        
        public boolean hasMoved() {
            return hasMoved;
        }
        
        public void setMoved(boolean moved) {
            this.hasMoved = moved;
        }
        
        @Override
        public String toString() {
            String colorSymbol = (color == Color.WHITE) ? "W" : "B";
            String typeSymbol = getTypeSymbol();
            return colorSymbol + typeSymbol;
        }
        
        private String getTypeSymbol() {
            switch (type) {
                case KING: return "K";
                case QUEEN: return "Q";
                case ROOK: return "R";
                case BISHOP: return "B";
                case KNIGHT: return "N";
                case PAWN: return "P";
                default: return "?";
            }
        }
        
        public ChessPiece copy() {
            ChessPiece copy = new ChessPiece(this.type, this.color);
            copy.hasMoved = this.hasMoved;
            return copy;
        }
    }
    
    private ChessPiece[][] board;
    
    public ChessBoard() {
        initializeBoard();
        setupPieces();
    }
    
    private void initializeBoard() {
        board = new ChessPiece[8][8];
    }
    
    private void setupPieces() {
        setupWhitePieces();
        
        setupBlackPieces();
    }
    
    private void setupWhitePieces() {
        placePiece(PieceType.ROOK, Color.WHITE, "a1");
        placePiece(PieceType.KNIGHT, Color.WHITE, "b1");
        placePiece(PieceType.BISHOP, Color.WHITE, "c1");
        placePiece(PieceType.QUEEN, Color.WHITE, "d1");
        placePiece(PieceType.KING, Color.WHITE, "e1");
        placePiece(PieceType.BISHOP, Color.WHITE, "f1");
        placePiece(PieceType.KNIGHT, Color.WHITE, "g1");
        placePiece(PieceType.ROOK, Color.WHITE, "h1");
        
        for (char file : FILES) {
            placePiece(PieceType.PAWN, Color.WHITE, file + "2");
        }
    }
    
    private void setupBlackPieces() {
        placePiece(PieceType.ROOK, Color.BLACK, "a8");
        placePiece(PieceType.KNIGHT, Color.BLACK, "b8");
        placePiece(PieceType.BISHOP, Color.BLACK, "c8");
        placePiece(PieceType.QUEEN, Color.BLACK, "d8");
        placePiece(PieceType.KING, Color.BLACK, "e8");
        placePiece(PieceType.BISHOP, Color.BLACK, "f8");
        placePiece(PieceType.KNIGHT, Color.BLACK, "g8");
        placePiece(PieceType.ROOK, Color.BLACK, "h8");
        
        for (char file : FILES) {
            placePiece(PieceType.PAWN, Color.BLACK, file + "7");
        }
    }
    
    private void placePiece(PieceType type, Color color, String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            board[indices[0]][indices[1]] = new ChessPiece(type, color);
        }
    }
    
    public void placePiece(ChessPiece piece, String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            board[indices[0]][indices[1]] = piece;
        }
    }
    
    public void placePiece(ChessPiece piece, int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    public ChessPiece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }
    
    public ChessPiece getPiece(String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            return board[indices[0]][indices[1]];
        }
        return null;
    }
    
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            return;
        }
        
        ChessPiece piece = board[fromRow][fromCol];
        if (piece != null) {
            // marking piece as moved
            piece.setMoved(true);
            board[toRow][toCol] = piece;
            board[fromRow][fromCol] = null;
        }
    }
    
    public void movePiece(String fromPosition, String toPosition) {
        int[] fromIndices = positionToIndices(fromPosition);
        int[] toIndices = positionToIndices(toPosition);
        
        if (fromIndices != null && toIndices != null) {
            movePiece(fromIndices[0], fromIndices[1], toIndices[0], toIndices[1]);
        }
    }
    
    public void removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = null;
        }
    }
    
    public void removePiece(String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            board[indices[0]][indices[1]] = null;
        }
    }
    
    public boolean isEmpty(int row, int col) {
        return getPiece(row, col) == null;
    }
    
    public boolean isEmpty(String position) {
        return getPiece(position) == null;
    }
    
    public boolean isOccupiedByColor(int row, int col, Color color) {
        ChessPiece piece = getPiece(row, col);
        return piece != null && piece.getColor() == color;
    }
    
    public int[] findKing(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                    return new int[]{row, col};
                }
            }
        }
        // if this happens theres most def something wrong with our code
        return null; 
    }
    
    public ChessBoard copy() {
        ChessBoard copy = new ChessBoard();
        copy.board = new ChessPiece[8][8];
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (this.board[row][col] != null) {
                    copy.board[row][col] = this.board[row][col].copy();
                }
            }
        }
        
        return copy;
    }
    
    public static int[] positionToIndices(String position) {
        if (position == null || position.length() != 2) {
            return null;
        }
        
        char fileChar = position.charAt(0);
        char rankChar = position.charAt(1);
        
        int fileIndex = -1;
        for (int i = 0; i < FILES.length; i++) {
            if (FILES[i] == fileChar) {
                fileIndex = i;
                break;
            }
        }
        
        int rankIndex = -1;
        try {
            int rank = Character.getNumericValue(rankChar);
            if (rank >= 1 && rank <= 8) {
                rankIndex = 8 - rank; 
            }
        } catch (NumberFormatException e) {
            return null;
        }
        
        if (fileIndex == -1 || rankIndex == -1) {
            return null;
        }
        
        return new int[]{rankIndex, fileIndex};
    }
    
    public static String indicesToPosition(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        char file = FILES[col];
        int rank = 8 - row; 
        return "" + file + rank;
    }
    
    public static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    public static boolean isValidPosition(String position) {
        return positionToIndices(position) != null;
    }
    
    public static int fileToCol(char file) {
        file = Character.toLowerCase(file);
        for (int i = 0; i < FILES.length; i++) {
            if (FILES[i] == file) {
                return i;
            }
        }
        return -1;
    }
    
    public static char colToFile(int col) {
        if (col >= 0 && col < 8) {
            return FILES[col];
        }
        return '?';
    }
    
    public static int rankToRow(int rank) {
        return 8 - rank;
    }
    
    public static int rowToRank(int row) {
        return 8 - row;
    }
    
    public void printBoard() {
        System.out.println("Chess Board:");
        System.out.println("============");
        
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " "); 
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    System.out.print(piece.toString() + "  ");
                } else {
                    String position = indicesToPosition(row, col);
                    System.out.print("[" + position + "] ");
                }
            }
            System.out.println();
        }
        
        System.out.print("  ");
        for (char file : FILES) {
            System.out.print(" " + file + "   ");
        }
        System.out.println();
    }
    
    public void printSimpleBoard() {
        System.out.println("Simple Board View:");
        System.out.println("==================");
        
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    System.out.print(piece.toString() + " ");
                } else {
                    System.out.print("-- ");
                }
            }
            System.out.println();
        }
        System.out.println("   a  b  c  d  e  f  g  h");
    }
    
    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }

    //Creates the ArrayList needed for printBoard in PlayChess
    public ArrayList<ReturnPiece> toReturnPieces() {
    ArrayList<ReturnPiece> pieces = new ArrayList<>();

    
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            ChessPiece piece = board[row][col];
            if (piece != null) {
                ReturnPiece rp = new ReturnPiece();

                
                char fileChar = colToFile(col);
                int rank = rowToRank(row);

                rp.pieceFile = ReturnPiece.PieceFile.valueOf(String.valueOf(fileChar));
                rp.pieceRank = rank;
                rp.pieceType = toReturnPieceType(piece);

                pieces.add(rp);
            }
        }
    }

    return pieces;
}    

    //formats piece representation into expected from
    private static ReturnPiece.PieceType toReturnPieceType(ChessPiece p) {
    boolean white = p.getColor() == Color.WHITE;
    return switch (p.getType()) {
        case KING   -> white ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK;
        case QUEEN  -> white ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
        case ROOK   -> white ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
        case BISHOP -> white ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
        case KNIGHT -> white ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
        case PAWN   -> white ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP;
    };
}



    
    // testing
    public static void main(String[] args) {
        ChessBoard chessBoard = new ChessBoard();
        
        System.out.println("Full Board Display:");
        chessBoard.printBoard();
        
        System.out.println("\nSimple Board Display:");
        chessBoard.printSimpleBoard();
        
        // piece retrieval
        System.out.println("\nPiece at e1: " + chessBoard.getPiece("e1"));
        System.out.println("Piece at e8: " + chessBoard.getPiece("e8"));
        System.out.println("Piece at e4: " + chessBoard.getPiece("e4"));
        
        // position convert
        System.out.println("\nPosition Conversions:");
        System.out.println("e4 -> indices: " + java.util.Arrays.toString(positionToIndices("e4")));
        System.out.println("Row 4, Col 4 -> position: " + indicesToPosition(4, 4));
        
        // test move
        System.out.println("\nTesting move e2 to e4:");
        chessBoard.movePiece("g1", "g3");
        chessBoard.printSimpleBoard();
    }
}
