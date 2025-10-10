//package chess;

public class GameState {

    private Player toMove; // tracks whose turn it is
    private boolean gameOver;
    private ReturnPlay.Message resultMessage; 
    private boolean wCastleK, wCastleQ; // white king side and queen side
    private boolean bCastleK, bCastleQ; // black king side and queen side

    // en passant target square
    private Square enPassantTarget;

    public enum Player { WHITE, BLACK;
        public Player opponent() { return this == WHITE ? BLACK : WHITE; }
    }

    public static class Square {
        public final int row, col;
        public Square(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public GameState() { reset(); }

    // resets the gamestate to starting point
    public void reset() {
        wCastleK = true;
        wCastleQ = true;
        bCastleK = true;
        bCastleQ = true;
        toMove = Player.WHITE;   // White always moves first
        gameOver = false;
        resultMessage = null;
        enPassantTarget = null;
    }

    public Player getToMove() { return toMove; }
    public void flipTurn() { toMove = toMove.opponent(); }

    // tracks castling
    public boolean canCastleK(Player p) { return (p == Player.WHITE) ? wCastleK : bCastleK; }
    public boolean canCastleQ(Player p) { return (p == Player.WHITE) ? wCastleQ : bCastleQ; }

    public void disableCastlingForKing(Player p) {
        if (p == Player.WHITE) { wCastleK = false; wCastleQ = false; }
        else { bCastleK = false; bCastleQ = false; }
    }
    public void disableCastleK(Player p) {
        if (p == Player.WHITE) wCastleK = false; else bCastleK = false;
    }
    public void disableCastleQ(Player p) {
        if (p == Player.WHITE) wCastleQ = false; else bCastleQ = false;
    }

    // tracks En Passant
    public void setEnPassantTargetIfTwoStep(Move move) {
        int from = move.getFromRank();
        int to   = move.getToRank();
        if (Math.abs(to - from) == 2) {
            int midRank = (from + to) / 2;              // chess rank
            int row = 8 - midRank;                      // convert to board row
            int col = move.getToCol();                  // board col
            enPassantTarget = new Square(row, col);     // store as board indices
        } else {
            enPassantTarget = null;
        }
    }
    public void clearEnPassant() { enPassantTarget = null; }
    public Square getEnPassantTarget() { return enPassantTarget; }

    public boolean isGameOver() { return gameOver; }
    public ReturnPlay.Message getResultMessage() { return resultMessage; }

    public void setCheck() { 
        resultMessage = ReturnPlay.Message.CHECK; 
    }

    public void setStalemate() {
        gameOver = true;
        resultMessage = ReturnPlay.Message.STALEMATE;
    }

    public void setCheckmate(Player winner) {
        gameOver = true;
        resultMessage = (winner == Player.WHITE) 
            ? ReturnPlay.Message.CHECKMATE_WHITE_WINS 
            : ReturnPlay.Message.CHECKMATE_BLACK_WINS;
    }

    public void setResign(Player resigned) {
        gameOver = true;
        resultMessage = (resigned == Player.WHITE)
            ? ReturnPlay.Message.RESIGN_BLACK_WINS
            : ReturnPlay.Message.RESIGN_WHITE_WINS;
    }

    public void clearMessage() { if (!gameOver) resultMessage = null; }
}
