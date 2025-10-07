// package chess;

public class MoveValidator {
    
    public static boolean isValid(Move move, ChessBoard board, ChessBoard.Color currentPlayer) {
        if (move.isResign()) {
            return true; // Resign is always valid
        }
        
        if (!isWithinBounds(move)) {
            return false;
        }
        
        ChessBoard.ChessPiece piece = board.getPiece(move.getFromRow(), move.getFromCol());
        if (piece == null) {
            return false;
        }
        
        if (piece.getColor() != currentPlayer) {
            return false;
        }
        
        // in case they try to capture their own piece
        ChessBoard.ChessPiece destinationPiece = board.getPiece(move.getToRow(), move.getToCol());
        if (destinationPiece != null && destinationPiece.getColor() == currentPlayer) {
            return false;
        }
        
        switch (piece.getType()) {
            case PAWN:
                return isValidPawnMove(move, board, piece);
            case ROOK:
                return isValidRookMove(move, board);
            case KNIGHT:
                return isValidKnightMove(move, board);
            case BISHOP:
                return isValidBishopMove(move, board);
            case QUEEN:
                return isValidQueenMove(move, board);
            case KING:
                return isValidKingMove(move, board, piece);
            default:
                return false;
        }
    }
    
    private static boolean isWithinBounds(Move move) {
        return move.getFromRow() >= 0 && move.getFromRow() < 8 &&
               move.getFromCol() >= 0 && move.getFromCol() < 8 &&
               move.getToRow() >= 0 && move.getToRow() < 8 &&
               move.getToCol() >= 0 && move.getToCol() < 8;
    }
    
    private static boolean isValidPawnMove(Move move, ChessBoard board, ChessBoard.ChessPiece pawn) {
        int direction = (pawn.getColor() == ChessBoard.Color.WHITE) ? -1 : 1;
        int startRow = (pawn.getColor() == ChessBoard.Color.WHITE) ? 6 : 1;
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();
        
        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);
        
        if (colDiff == 0) {
            if (rowDiff == direction && board.isEmpty(toRow, toCol)) {
                return true;
            }
            if (rowDiff == 2 * direction && fromRow == startRow && 
                board.isEmpty(toRow, toCol) && board.isEmpty(fromRow + direction, fromCol)) {
                return true;
            }
        }
        else if (colDiff == 1 && rowDiff == direction) {
            ChessBoard.ChessPiece target = board.getPiece(toRow, toCol);
            if (target != null && target.getColor() != pawn.getColor()) {
                return true;
            }
            // im lazy add en passant logic here
        }
        
        return false;
    }
    
    private static boolean isValidRookMove(Move move, ChessBoard board) {
        return isStraightLineMove(move, board);
    }
    
    private static boolean isValidKnightMove(Move move, ChessBoard board) {
        int rowDiff = Math.abs(move.getToRow() - move.getFromRow());
        int colDiff = Math.abs(move.getToCol() - move.getFromCol());
        
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private static boolean isValidBishopMove(Move move, ChessBoard board) {
        return isDiagonalMove(move, board);
    }
    
    private static boolean isValidQueenMove(Move move, ChessBoard board) {
        return isStraightLineMove(move, board) || isDiagonalMove(move, board);
    }
    
    private static boolean isValidKingMove(Move move, ChessBoard board, ChessBoard.ChessPiece king) {
        int rowDiff = Math.abs(move.getToRow() - move.getFromRow());
        int colDiff = Math.abs(move.getToCol() - move.getFromCol());
        
        if (rowDiff <= 1 && colDiff <= 1) {
            return true;
        }
        
        // castling attempt
        if (move.isCastlingAttempt() && !king.hasMoved()) {
            return isValidCastling(move, board, king);
        }
        
        return false;
    }
    
    private static boolean isValidCastling(Move move, ChessBoard board, ChessBoard.ChessPiece king) {
        // im lazy AGAIN implement castling logic
        // check if rook hasn't moved or path is clear or king not in check
        return false; // Placeholder
    }
    
    private static boolean isStraightLineMove(Move move, ChessBoard board) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();
        
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        
        if (fromRow == toRow) {
            // Horizontal move
            int colStep = (toCol > fromCol) ? 1 : -1;
            for (int col = fromCol + colStep; col != toCol; col += colStep) {
                if (!board.isEmpty(fromRow, col)) {
                    return false;
                }
            }
        } else {
            int rowStep = (toRow > fromRow) ? 1 : -1;
            for (int row = fromRow + rowStep; row != toRow; row += rowStep) {
                if (!board.isEmpty(row, fromCol)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private static boolean isDiagonalMove(Move move, ChessBoard board) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();
        
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        if (rowDiff != colDiff) {
            return false;
        }
        
        int rowStep = (toRow > fromRow) ? 1 : -1;
        int colStep = (toCol > fromCol) ? 1 : -1;
        
        for (int i = 1; i < rowDiff; i++) {
            int checkRow = fromRow + i * rowStep;
            int checkCol = fromCol + i * colStep;
            if (!board.isEmpty(checkRow, checkCol)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean wouldPutKingInCheck(Move move, ChessBoard board, ChessBoard.Color currentPlayer) {
        // make a copy of the board
        ChessBoard testBoard = board.copy();
        
        // apply the move on the test board
        testBoard.movePiece(move.getFromRow(), move.getFromCol(), 
                           move.getToRow(), move.getToCol());
        
        // see if king is in check after the move
        return isKingInCheck(testBoard, currentPlayer);
    }
    
    public static boolean isKingInCheck(ChessBoard board, ChessBoard.Color kingColor) {
        int[] kingPosition = board.findKing(kingColor);
        if (kingPosition == null) return false;
        
        ChessBoard.Color opponentColor = (kingColor == ChessBoard.Color.WHITE) ? 
            ChessBoard.Color.BLACK : ChessBoard.Color.WHITE;
        
        // check if any opponent piece can attack the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessBoard.ChessPiece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == opponentColor) {
                    // create a temporary move from this piece to the king
                    Move attackMove = new Move(
                        ChessBoard.colToFile(col),
                        ChessBoard.rowToRank(row),
                        ChessBoard.colToFile(kingPosition[1]),
                        ChessBoard.rowToRank(kingPosition[0]),
                        '\0', false
                    );
                    
                    // use the regular validation but skip the color check
                    if (isValidMoveForPiece(attackMove, board, piece)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private static boolean isValidMoveForPiece(Move move, ChessBoard board, ChessBoard.ChessPiece piece) {
        // similar to isValid but without color and piece existence checks
        switch (piece.getType()) {
            case PAWN:
                return isValidPawnMove(move, board, piece);
            case ROOK:
                return isValidRookMove(move, board);
            case KNIGHT:
                return isValidKnightMove(move, board);
            case BISHOP:
                return isValidBishopMove(move, board);
            case QUEEN:
                return isValidQueenMove(move, board);
            case KING:
                return isValidKingMove(move, board, piece);
            default:
                return false;
        }
    }
}