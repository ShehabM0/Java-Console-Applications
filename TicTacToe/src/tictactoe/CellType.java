package tictactoe;

enum CellType {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final char symbol;

    CellType(char symbol) {
        this.symbol = symbol;
    }

    char symbol() {
        return symbol;
    }
}
