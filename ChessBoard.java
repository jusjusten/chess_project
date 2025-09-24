public class ChessBoard {
    // Define file (column) labels
    private static final char[] FILES = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    
    // Define rank (row) numbers
    private static final int[] RANKS = {1, 2, 3, 4, 5, 6, 7, 8};
    
    // Piece types
    private enum PieceType {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }
    
    // Piece colors
    private enum Color {
        WHITE, BLACK
    }
    
    // Class representing a chess piece
    private static class ChessPiece {
        private final PieceType type;
        private final Color color;
        private final String position;
        
        public ChessPiece(PieceType type, Color color, String position) {
            this.type = type;
            this.color = color;
            this.position = position;
        }
        
        @Override
        public String toString() {
            String colorSymbol = (color == Color.WHITE) ? "W" : "B";
            String typeSymbol = getTypeSymbol();
            return colorSymbol + typeSymbol + "@" + position;
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
        // Setup white pieces (ranks 1 and 2)
        setupWhitePieces();
        
        // Setup black pieces (ranks 7 and 8)
        setupBlackPieces();
    }
    
    private void setupWhitePieces() {
        // Rank 1: Back row pieces
        placePiece(PieceType.ROOK, Color.WHITE, "a1");
        placePiece(PieceType.KNIGHT, Color.WHITE, "b1");
        placePiece(PieceType.BISHOP, Color.WHITE, "c1");
        placePiece(PieceType.QUEEN, Color.WHITE, "d1"); // Queen on d file
        placePiece(PieceType.KING, Color.WHITE, "e1");
        placePiece(PieceType.BISHOP, Color.WHITE, "f1");
        placePiece(PieceType.KNIGHT, Color.WHITE, "g1");
        placePiece(PieceType.ROOK, Color.WHITE, "h1");
        
        // Rank 2: Pawns
        for (char file : FILES) {
            placePiece(PieceType.PAWN, Color.WHITE, file + "2");
        }
    }
    
    private void setupBlackPieces() {
        // Rank 8: Back row pieces
        placePiece(PieceType.ROOK, Color.BLACK, "a8");
        placePiece(PieceType.KNIGHT, Color.BLACK, "b8");
        placePiece(PieceType.BISHOP, Color.BLACK, "c8");
        placePiece(PieceType.QUEEN, Color.BLACK, "d8"); // Queen on d file
        placePiece(PieceType.KING, Color.BLACK, "e8");
        placePiece(PieceType.BISHOP, Color.BLACK, "f8");
        placePiece(PieceType.KNIGHT, Color.BLACK, "g8");
        placePiece(PieceType.ROOK, Color.BLACK, "h8");
        
        // Rank 7: Pawns
        for (char file : FILES) {
            placePiece(PieceType.PAWN, Color.BLACK, file + "7");
        }
    }
    
    private void placePiece(PieceType type, Color color, String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            board[indices[0]][indices[1]] = new ChessPiece(type, color, position);
        }
    }
    
    private int[] positionToIndices(String position) {
        if (position.length() != 2) return null;
        
        char fileChar = position.charAt(0);
        char rankChar = position.charAt(1);
        
        // Convert file (a-h) to column index (0-7)
        int fileIndex = -1;
        for (int i = 0; i < FILES.length; i++) {
            if (FILES[i] == fileChar) {
                fileIndex = i;
                break;
            }
        }
        
        // Convert rank (1-8) to row index (0-7)
        int rankIndex = Character.getNumericValue(rankChar) - 1;
        
        if (fileIndex == -1 || rankIndex < 0 || rankIndex > 7) {
            return null;
        }
        
        return new int[]{rankIndex, fileIndex};
    }
    
    private String indicesToPosition(int rankIndex, int fileIndex) {
        if (rankIndex < 0 || rankIndex > 7 || fileIndex < 0 || fileIndex > 7) {
            return null;
        }
        return FILES[fileIndex] + String.valueOf(RANKS[rankIndex]);
    }
    
    public void printBoard() {
        System.out.println("Chess Board Setup:");
        System.out.println("==================");
        
        // Print from rank 8 down to rank 1 (top to bottom)
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " ");
            for (int file = 0; file < 8; file++) {
                ChessPiece piece = board[rank][file];
                if (piece != null) {
                    System.out.print(piece.toString() + " ");
                } else {
                    String position = indicesToPosition(rank, file);
                    System.out.print("[" + position + "] ");
                }
            }
            System.out.println();
        }
        
        // Print file labels
        System.out.print("  ");
        for (char file : FILES) {
            System.out.print("  " + file + "   ");
        }
        System.out.println();
    }
    
    public ChessPiece getPieceAt(String position) {
        int[] indices = positionToIndices(position);
        if (indices != null) {
            return board[indices[0]][indices[1]];
        }
        return null;
    }
    
    public static void main(String[] args) {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.printBoard();
        
        // Demonstrate getting a specific piece
        System.out.println("\nPiece at d1: " + chessBoard.getPieceAt("d1"));
        System.out.println("Piece at d8: " + chessBoard.getPieceAt("d8"));
        System.out.println("Piece at e4: " + chessBoard.getPieceAt("e4"));
    }
}