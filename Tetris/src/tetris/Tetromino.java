package tetris;

enum Tetromino {
    O(new int[][] {
            {4, 14, 15, 5}
    }),
    I(new int[][] {
            {4, 14, 24, 34},
            {3, 4, 5, 6}
    }),
    S(new int[][] {
            {5, 4, 14, 13},
            {4, 14, 15, 25}
    }),
    Z(new int[][] {
            {4, 5, 15, 16},
            {5, 15, 14, 24}
    }),
    L(new int[][] {
            {4, 14, 24, 25},
            {5, 15, 14, 13},
            {4, 5, 15, 25},
            {6, 5, 4, 14}
    }),
    J(new int[][] {
            {5, 15, 25, 24},
            {15, 5, 4, 3},
            {5, 4, 14, 24},
            {4, 14, 15, 16}
    }),
    T(new int[][] {
            {4, 14, 24, 15},
            {4, 13, 14, 15},
            {5, 15, 25, 14},
            {4, 5, 6, 15}
    });

    private final int[][] states;

    Tetromino(int[][] states) {
        this.states = states;
    }

    public int[] getState(int idx) {
        return states[idx];
    }

    public int getStatesSize() {
        return states.length;
    }
}
