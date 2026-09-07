package tetris;

enum Tetromino {
    O(new Integer[][] {
            {4, 14, 15, 5}
    }),
    I(new Integer[][] {
            {4, 14, 24, 34},
            {3, 4, 5, 6}
    }),
    S(new Integer[][] {
            {5, 4, 14, 13},
            {4, 14, 15, 25}
    }),
    Z(new Integer[][] {
            {4, 5, 15, 16},
            {5, 15, 14, 24}
    }),
    L(new Integer[][] {
            {4, 14, 24, 25},
            {5, 15, 14, 13},
            {4, 5, 15, 25},
            {6, 5, 4, 14}
    }),
    J(new Integer[][] {
            {5, 15, 25, 24},
            {15, 5, 4, 3},
            {5, 4, 14, 24},
            {4, 14, 15, 16}
    }),
    T(new Integer[][] {
            {4, 14, 24, 15},
            {4, 13, 14, 15},
            {5, 15, 25, 14},
            {4, 5, 6, 15}
    });

    private final Integer[][] states;

    Tetromino(Integer[][] states) {
        this.states = states;
    }

    public Integer[] getState(Integer idx) {
        return states[idx];
    }

    public int getStatesSize() {
        return states.length;
    }
}
