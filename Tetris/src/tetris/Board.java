package tetris;

class Board {
    static final int M = 10;
    private final int N;
    private final char[][] board;

    private Grid grid;
    private int originX, originY;
    private int spawnOriginX, spawnOriginY;
    private boolean isTetrominoPlaced;

    Board() {
        this(20);
    }

    Board(int n) {
        if(n < 5)
            throw new IllegalArgumentException("Enter valid board size!");
        N = n;
        board = new char[N][M];
        isTetrominoPlaced = false;

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
        if(isTetrominoPlaced)
            return;

        int rightBorder = grid.getRightBorder();
        int boardY = originY + (rightBorder - spawnOriginY);
        if(boardY + 1 < M) {
            clearPiece();
            originY++;
            drawPiece();
        }
    }

    void moveTetrominoLeft() {
        if(isTetrominoPlaced)
            return;

        int leftBorder = grid.getLeftBorder();
        int boardY = originY + (leftBorder - spawnOriginY);
        if(boardY - 1 > -1) {
            clearPiece();
            originY--;
            drawPiece();
        }
    }

    void moveTetrominoDown() {
        int bottomBorder = grid.getBottomBorder();
        int boardX = originX + (bottomBorder - spawnOriginX);
        if(boardX + 1 < N) {
            clearPiece();
            originX++;
            drawPiece();
        } else {
            isTetrominoPlaced = true;
        }
    }

    void rotateTetromino() {
        if(isTetrominoPlaced)
            return;

        int[] nextStateBorders = grid.getNextStateBorders();
        int nextBottomBorder = originX + (nextStateBorders[0] - spawnOriginX);
        int nextLeftBorder = originY + (nextStateBorders[1] - spawnOriginY),
            nextRightBorder = originY + (nextStateBorders[2] - spawnOriginY);

        if(nextBottomBorder >= N)
            makeTetrominoBottomRotatable(nextBottomBorder);
        if(nextRightBorder >= M)
            makeTetrominoRightRotatable(nextRightBorder);
        if(nextLeftBorder < 0)
            makeTetrominoLeftRotatable(nextLeftBorder);
        clearPiece();
        grid.rotate();
        drawPiece();
    }

    void makeTetrominoBottomRotatable(int bottomBorder) {
        while (bottomBorder >= N) {
            bottomBorder--;
            moveTetrominoUp();
        }
    }

    void makeTetrominoRightRotatable(int rightBorder) {
        while (rightBorder >= M) {
            rightBorder--;
            moveTetrominoLeft();
        }
    }

    void makeTetrominoLeftRotatable(int leftBorder) {
        while (leftBorder < 0) {
            leftBorder++;
            moveTetrominoRight();
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

    private void clearPiece() {
        for(int gridCord : grid.getCurrentStateCords()) {
            int offsetX = grid.idxToX(gridCord) - spawnOriginX;
            int offsetY = grid.idxToY(gridCord) - spawnOriginY;
            int boardX = originX + offsetX;
            int boardY = originY + offsetY;
            board[boardX][boardY] = '-';
        }
    }

    private void drawPiece() {
        for (int gridCord : grid.getCurrentStateCords()) {
            int offsetX = grid.idxToX(gridCord) - spawnOriginX;
            int offsetY = grid.idxToY(gridCord) - spawnOriginY;
            int boardX = originX + offsetX;
            int boardY = originY + offsetY;
            board[boardX][boardY] = '0';
        }
    }

    private void moveTetrominoUp() {
        int topBorder = 0;
        int boardX = originX + (topBorder - spawnOriginX);
        if(boardX - 1 > -1) {
            clearPiece();
            originX--;
            drawPiece();
        }
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
*/
