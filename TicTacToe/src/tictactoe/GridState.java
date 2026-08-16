package tictactoe;

public enum GridState {
    DRAW("Draw"),
    X("X wins"),
    O("O wins"),
    NOT_FINISHED("Game not finished");

    private final String state;

    GridState(String state) {
        this.state = state;
    }

    String state() {
        return state;
    }
}
