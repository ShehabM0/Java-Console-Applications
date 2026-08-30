package battleship;

enum CellType {
    HIT('X'),
    SHIP('O'),
    MISS('M'),
    EMPTY('~');

    private final char symbol;

    CellType(char symbol) {
        this.symbol = symbol;
    }

    char symbol() {
        return symbol;
    }
}
