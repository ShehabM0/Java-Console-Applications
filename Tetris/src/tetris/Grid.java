package tetris;

class Grid {
    static final int SIZE = 4;

    private final Tetromino tetromino;
    private final char[][] grid;

    private int[] currentTetrominoState;
    private int rotationIndex;

    Grid(Tetromino tetromino) {
        this.tetromino = tetromino;
        grid = new char[SIZE][SIZE];
        rotationIndex = 0;
        currentTetrominoState = tetromino.getState(rotationIndex);

        placeTetromino();
    }

    int[] getNextStateBorders() {
        int nextRotationIndex = (rotationIndex + 1) % tetromino.getStatesSize();
        int[] nextTetrominoState = tetromino.getState(nextRotationIndex);

        int nextBottomBorder = getBottomBorder(nextTetrominoState);
        int nextLeftBorder = getLeftBorder(nextTetrominoState),
            nextRightBorder = getRightBorder(nextTetrominoState);
        return new int[]{ nextBottomBorder, nextLeftBorder, nextRightBorder };
    }

    int getRightBorder() {
        int rightBorder = Integer.MIN_VALUE;
        for(int idx : currentTetrominoState) {
            rightBorder = Math.max(rightBorder, idx % Board.M);
        }
        return rightBorder;
    }

    int getLeftBorder() {
        int leftBorder = Integer.MAX_VALUE;
        for(int idx : currentTetrominoState) {
            leftBorder = Math.min(leftBorder, idx % Board.M);
        }
        return leftBorder;
    }

    int getBottomBorder() {
        int bottomBorder = Integer.MIN_VALUE;
        for(int idx : currentTetrominoState) {
            bottomBorder = Math.max(bottomBorder, idx / Board.M);
        }
        return bottomBorder;
    }

    int getRightBorder(int[] currentTetrominoState) {
        int rightBorder = Integer.MIN_VALUE;
        for(int idx : currentTetrominoState) {
            rightBorder = Math.max(rightBorder, idx % Board.M);
        }
        return rightBorder;
    }

    int getLeftBorder(int[] currentTetrominoState) {
        int leftBorder = Integer.MAX_VALUE;
        for(int idx : currentTetrominoState) {
            leftBorder = Math.min(leftBorder, idx % Board.M);
        }
        return leftBorder;
    }

    int getBottomBorder(int[] currentTetrominoState) {
        int bottomBorder = Integer.MIN_VALUE;
        for(int idx : currentTetrominoState) {
            bottomBorder = Math.max(bottomBorder, idx / Board.M);
        }
        return bottomBorder;
    }

    void rotate() {
        rotationIndex = (rotationIndex + 1) % tetromino.getStatesSize();
        currentTetrominoState = tetromino.getState(rotationIndex);
        placeTetromino();
    }

    int idxToX(int idx) {
        return idx / Board.M;
    }

    int idxToY(int idx) {
        return idx % Board.M;
    }

    int[] getCurrentStateCords() {
        return currentTetrominoState;
    }

    int[] getOrigin() {
        int originX = Integer.MAX_VALUE, originY = Integer.MAX_VALUE;
        for(int idx : currentTetrominoState) {
            originX = Math.min(originX, idx / Board.M);
            originY = Math.min(originY, idx % Board.M);
        }
        return new int[]{ originX, originY };
    }

    void display() {
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++)
                System.out.print(grid[i][j] + " ");
            System.out.println();
        }
    }

    private void placeTetromino() {
        clear();
        draw();
    }

    private void clear() {
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                grid[i][j] = '-';
    }

    private void draw() {
        int originX = Integer.MAX_VALUE, originY = Integer.MAX_VALUE;
        for(int idx : currentTetrominoState) {
            originX = Math.min(originX, idx / Board.M);
            originY = Math.min(originY, idx % Board.M);
        }
        for(int idx : currentTetrominoState) {
            int x = idx / Board.M - originX;
            int y = idx % Board.M - originY;
            grid[x][y] = '0';
        }
    }
}
