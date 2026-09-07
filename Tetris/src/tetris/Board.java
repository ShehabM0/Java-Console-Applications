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

        init();
    }

    void init() {
        for(int i = 0; i < N; i++)
            for(int j = 0; j < M; j++)
                board[i][j] = '-';
    }

    void spawn(Tetromino tetromino) {
        grid = new Grid(tetromino);
        isTetrominoPlaced = false;

        Integer[] gridOrigin = grid.getOrigin();
        originX = gridOrigin[0]; originY = gridOrigin[1];
        spawnOriginX = originX; spawnOriginY = originY;

        drawPiece();
    }

    void moveTetrominoRight() {
        if(!canMoveRight())
            return;

        clearPiece();
        originY++;
        drawPiece();
    }

    void moveTetrominoLeft() {
        if(!canMoveLeft())
            return;

        clearPiece();
        originY--;
        drawPiece();
    }

    void moveTetrominoDown() {
        if(!canMoveDown())
            return;

        clearPiece();
        originX++;
        drawPiece();
    }

    void rotateTetromino() {
        if(!canRotate())
            return;

        Integer[] nextStateBorders = grid.getNextStateBorders();
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

    void disappear() {
        int delXs = 0;

        int cells;
        for(int i = N - 1; i > -1; i--) {
            cells = 0;
            for(int j = 0; j < M; j++)
                cells += board[i][j] == '0' ? 1 : 0;
            if(cells == M) {
                delXs++;
                for(int j = 0; j < M; j++)
                    board[i][j] = '-';
            }
        }

        while (delXs-- > 0)
            for (int i = N - 1; i > -1; i--)
                for (int j = 0; j < M; j++)
                    if (board[i][j] == '0') {
                        board[i + 1][j] = '0';
                        board[i][j] = '-';
                    }
    }

    boolean isGameOver() {
        boolean maxHeight = false;
        int cnt;
        for(int i = 0; i < M; i++) {
            cnt = 0;
            for(int j = 0; j < N; j++)
                cnt += board[j][i] == '0' ? 1 : 0;
            if(cnt == N) {
                maxHeight = true;
                break;
            }
        }

        return (!canMoveDown() && maxHeight);
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

    private void makeTetrominoBottomRotatable(int bottomBorder) {
        while (bottomBorder >= N) {
            bottomBorder--;
            moveTetrominoUp();
        }
    }

    private void makeTetrominoRightRotatable(int rightBorder) {
        while (rightBorder >= M) {
            rightBorder--;
            moveTetrominoLeft();
        }
    }

    private void makeTetrominoLeftRotatable(int leftBorder) {
        while (leftBorder < 0) {
            leftBorder++;
            moveTetrominoRight();
        }
    }

    private boolean canRotate() {
        clearPiece();
        for(int gridCord : grid.getNextStateCords()) {
            int boardX = originX + (grid.idxToX(gridCord) - spawnOriginX);
            int boardY = originY + (grid.idxToY(gridCord) - spawnOriginY);
            if(boardX < N && boardY < M && board[boardX][boardY] == '0') {
                drawPiece();
                return false;
            }
        }
        drawPiece();
        return true;
    }

    private boolean canMoveRight() {
        if(isTetrominoPlaced)
            return false;

        int boardY = originY + (grid.getRightBorder() - spawnOriginY);
        if(boardY + 1 == M)
            return false;

        for(int gridCord : grid.getRightCells()) {
            int boardX = originX + (grid.idxToX(gridCord) - spawnOriginX);
            boardY = originY + (grid.idxToY(gridCord) - spawnOriginY);
            if(board[boardX][boardY + 1] == '0') {
                return false;
            }
        }
        return true;
    }

    private boolean canMoveLeft() {
        if(isTetrominoPlaced)
            return false;

        int boardY = originY + (grid.getLeftBorder() - spawnOriginY);
        if(boardY - 1 == -1)
            return false;

        for(int gridCord : grid.getLeftCells()) {
            int boardX = originX + (grid.idxToX(gridCord) - spawnOriginX);
            boardY = originY + (grid.idxToY(gridCord) - spawnOriginY);
            if(board[boardX][boardY - 1] == '0') {
                return false;
            }
        }
        return true;
    }

    private boolean canMoveDown() {
        if(isTetrominoPlaced)
            return false;

        int bottomBorder = grid.getBottomBorder();
        int boardX = originX + (bottomBorder - spawnOriginX);

        if(boardX == N - 1) {
            isTetrominoPlaced = true;
            return false;
        }

        for(int leafCell : grid.getLeafCells()) {
            int boardY = originY + (grid.idxToY(leafCell) - spawnOriginY);
            boardX = originX + (grid.idxToX(leafCell) - spawnOriginX);
            if(board[boardX + 1][boardY] == '0') {
                isTetrominoPlaced = true;
                return false;
            }
        }
        return true;
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
