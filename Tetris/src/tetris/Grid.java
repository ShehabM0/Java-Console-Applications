package tetris;

class Grid {
    static final int SIZE = 4;

    private final Tetromino tetromino;
    private int[] currentTetrominoState;
    private final char[][] grid;
    private int rotationIndex;

    Grid(Tetromino tetromino) {
        this.tetromino = tetromino;
        grid = new char[SIZE][SIZE];
        rotationIndex = 0;
        currentTetrominoState = tetromino.getState(rotationIndex);

        placeTetromino();
    }

    void rotate() {
        rotationIndex = (rotationIndex + 1) % tetromino.getStatesSize();
        currentTetrominoState = tetromino.getState(rotationIndex);
        placeTetromino();
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
        for(int idx : currentTetrominoState) {
            int row = idx / SIZE;
            int col = idx % SIZE;
            grid[row][col] = '0';
        }
    }

    ///////

    private void transpose() {
        for(int i = 0; i < SIZE; i++)
            for(int j = i + 1; j < SIZE; j++)
                swap(i, j, j, i);
    }

    private void reverse() {
        for(int i = 0; i < SIZE / 2; i++)
            for(int j = 0; j < SIZE; j++)
                swap(i, j, SIZE - 1 - i, j);
    }

    private void swap(int i1, int j1, int i2, int j2) {
        char temp = grid[i1][j1];
        grid[i1][j1] = grid[i2][j2];
        grid[i2][j2] = temp;
    }
}
