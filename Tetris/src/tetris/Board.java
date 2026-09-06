package tetris;

class Board {
    static final int M = 10;
    private final int N;
    private final char[][] board;

    private Grid grid;
    private int originX, originY;
    private int spawnOriginX, spawnOriginY;

    Board() {
        this(20);
    }

    Board(int n) {
        if(n < 5)
            throw new IllegalArgumentException("Enter valid board size!");
        N = n;
        board = new char[N][M];

        init();
    }

    void init() {
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
                board[i][j] = '-';
    }

    void spawn(Tetromino tetromino) {
        grid = new Grid(tetromino);

        int[] gridOrigin = grid.getOrigin();
        originX = gridOrigin[0]; originY = gridOrigin[1];
        spawnOriginX = originX; spawnOriginY = originY;

        drawPiece();
    }

    void moveTetrominoRight() {
        clearPiece();
        originY = (originY + 1) % M;
        drawPiece();
    }

    void moveTetrominoLeft() {
        clearPiece();
        originY = ((originY - 1) + M) % M;
        drawPiece();
    }

    void moveTetrominoDown() {
        clearPiece();
        originX = (originX + 1) % N;
        drawPiece();
    }

    void rotateTetromino() {
        clearPiece();
        grid.rotate();
        drawPiece();
    }

    private void clearPiece() {
        for(int gridCord : grid.getCurrentStateCords()) {
            int offsetX = grid.idxToX(gridCord) - spawnOriginX;
            int offsetY = grid.idxToY(gridCord) - spawnOriginY;
            int boardX = originX + offsetX;
            int boardY = originY + offsetY;
            board[boardX % N][boardY % M] = '-';
        }
    }

    private void drawPiece() {
        for(int gridCord : grid.getCurrentStateCords()) {
            int offsetX = grid.idxToX(gridCord) - spawnOriginX;
            int offsetY = grid.idxToY(gridCord) - spawnOriginY;
            int boardX = originX + offsetX;
            int boardY = originY + offsetY;
            board[boardX % N][boardY % M] = '0';
        }
    }

    void display() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < M; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }
}

/*
Tetromino states/rotations are board-cords based.
whole grid moves inside the board.
┌──────────────────────┐
│                      │
│                      │
│       ●───────┐      │
│       │ Grid  │      │
│       └───────┘      │
└──────────────────────┘

grid.idxToX/Y(gridCord)                  // board's spawn cords
grid.idxToX/Y(gridCord) - spawnOriginX/Y // board's origin cords (top-left corner)
originX/Y + offsetX/Y                    // Shift the Grid to its current position on the Board
*/
