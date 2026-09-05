package tetris;

enum Tetromino {
    O(new int[][] {
            {5, 6, 9, 10}
    }),
    I(new int[][] {
            {1, 5, 9, 13},
            {4, 5, 6, 7}
    }),
    S(new int[][] {
            {5, 6, 8, 9},
            {5, 9, 10, 14}
    }),
    Z(new int[][] {
            {4, 5, 9, 10},
            {2, 5, 6, 9}
    }),
    L(new int[][] {
            {1, 5, 9, 10},
            {2, 4, 5, 6},
            {1, 2, 6, 10},
            {4, 5, 6, 8},
    }),
    J(new int[][] {
            {2, 6, 9, 10},
            {4, 5, 6, 10},
            {1, 2, 5, 9},
            {0, 4, 5, 6},
    }),
    T(new int[][] {
            {1, 5, 6, 9},
            {1, 4, 5, 6},
            {1, 4, 5, 9},
            {4, 5, 6, 9},
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
