public final class Move {
    private final char fromFile;
    private final int  fromRank;
    private final char toFile;
    private final int  toRank;
    private final char promotionPiece;
    private final boolean isResign;
    private final boolean isDrawOffer;


    // Normal / promotion / draw
    public Move(char fromFile, int fromRank, char toFile, int toRank,
                char promotionPiece, boolean isDrawOffer) {
        this.fromFile = normalizeFile(fromFile);
        this.fromRank = fromRank;
        this.toFile   = normalizeFile(toFile);
        this.toRank   = toRank;
        this.promotionPiece = normalizePromotion(promotionPiece);
        this.isDrawOffer = isDrawOffer;
        this.isResign = false;

        if (!isValidFile(this.fromFile) || !isValidFile(this.toFile)
            || !isValidRank(this.fromRank) || !isValidRank(this.toRank)) {
            throw new IllegalArgumentException("Invalid square in move.");
        }
    }

    // resign
    public Move(boolean isResign, boolean isDrawOffer) {
        this.fromFile = 'a';
        this.fromRank = 1;
        this.toFile   = 'a';
        this.toRank   = 1;
        this.promotionPiece = '\0';
        this.isDrawOffer = isDrawOffer;
        this.isResign = isResign;
    }

    // parse input into a Move
    public static Move parse(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Move cannot be null.");
        }
        input = input.trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Move cannot be empty.");
        }

        String[] parts = input.split("\\s+");

        // resign
        if (parts[0].equalsIgnoreCase("resign")) {
            return new Move(true, false);
        }

        if (parts.length < 2) {
            throw new IllegalArgumentException("Expected at least: <from> <to>");
        }

        // from and to
        String fromSq = parts[0];
        String toSq   = parts[1];
        if (!isSquareToken(fromSq) || !isSquareToken(toSq)) {
            throw new IllegalArgumentException("Squares must be like e2 or g8.");
        }

        char fromFile = normalizeFile(fromSq.charAt(0));
        int  fromRank = fromSq.charAt(1) - '0';
        char toFile   = normalizeFile(toSq.charAt(0));
        int  toRank   = toSq.charAt(1) - '0';

        char promotion = '\0';
        boolean draw = false;

        if (parts.length == 3) {
            if (parts[2].equalsIgnoreCase("draw?")) {
                draw = true;
            } else {
                promotion = normalizePromotion(parts[2].charAt(0));
            }
        } else if (parts.length == 4) {
            promotion = normalizePromotion(parts[2].charAt(0));
            if (parts[3].equalsIgnoreCase("draw?")) {
                draw = true;
            }
        } else if (parts.length > 4) {
            throw new IllegalArgumentException("Too many tokens in move.");
        }

        return new Move(fromFile, fromRank, toFile, toRank, promotion, draw);
    }

   
    public int getFromRow() { return rankToRow(fromRank); }
    public int getFromCol() { return fileToCol(fromFile); }
    public int getToRow()   { return rankToRow(toRank); }
    public int getToCol()   { return fileToCol(toFile); }

   
    public boolean isResign() { return isResign; }
    public boolean isDrawOffer() { return isDrawOffer; }
    public boolean isPromotionRequested() { return promotionPiece != '\0'; }
    public char getPromotionPiece() { return promotionPiece; }

    public char getFromFile() { return fromFile; }
    public int  getFromRank() { return fromRank; }
    public char getToFile()   { return toFile; }
    public int  getToRank()   { return toRank; }

    
    public boolean isCastlingAttempt() {
        // king moves two files on same rank
        return fromRank == toRank && Math.abs(toFile - fromFile) == 2;
    }

    // Coordinate conversions
    public static int fileToCol(char file) { return normalizeFile(file) - 'a'; }
    public static int rankToRow(int rank)  { return 8 - rank; }
    public static char colToFile(int col)  { return (char)('a' + col); }
    public static int rowToRank(int row)   { return 8 - row; }

    // Validation helpers
    private static boolean isSquareToken(String s) {
        return s != null
            && s.length() == 2
            && isValidFile(normalizeFile(s.charAt(0)))
            && isValidRank(s.charAt(1) - '0');
    }

    private static boolean isValidFile(char f) { return f >= 'a' && f <= 'h'; }
    private static boolean isValidRank(int r)  { return r >= 1 && r <= 8; }

    private static char normalizeFile(char f) {
        char c = Character.toLowerCase(f);
        if (!isValidFile(c)) {
            throw new IllegalArgumentException("Invalid file: " + f);
        }
        return c;
    }

    private static char normalizePromotion(char p) {
        if (p == '\0') return '\0';
        char c = Character.toUpperCase(p);
        if (c == 'Q' || c == 'R' || c == 'B' || c == 'N') return c;
        throw new IllegalArgumentException("Promotion must be Q, R, B, or N.");
    }

    @Override
    public String toString() {
        String base = "" + fromFile + fromRank + " -> " + toFile + toRank;
        if (isResign) return "resign";
        String tail = "";
        if (isPromotionRequested()) tail += " promo=" + promotionPiece;
        if (isDrawOffer) tail += " draw?";
        return tail.isEmpty() ? base : base + tail;
    }
}
