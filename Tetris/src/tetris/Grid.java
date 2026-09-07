package tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Grid {
    static final int SIZE = 4;

    private final Tetromino tetromino;
    private final char[][] grid;

    private Integer[] currentTetrominoState;
    private int rotationIndex;

    Grid(Tetromino tetromino) {
        this.tetromino = tetromino;
        grid = new char[SIZE][SIZE];
        rotationIndex = 0;
        currentTetrominoState = tetromino.getState(rotationIndex);

        placeTetromino();
    }

    Integer[] getNextStateBorders() {
        int nextRotationIndex = (rotationIndex + 1) % tetromino.getStatesSize();
        Integer[] nextTetrominoState = tetromino.getState(nextRotationIndex);

        int nextBottomBorder = getBottomBorder(nextTetrominoState);
        int nextLeftBorder = getLeftBorder(nextTetrominoState),
            nextRightBorder = getRightBorder(nextTetrominoState);
        return new Integer[]{ nextBottomBorder, nextLeftBorder, nextRightBorder };
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

    int getRightBorder(Integer[] tetrominoState) {
        int rightBorder = Integer.MIN_VALUE;
        for(int idx : tetrominoState) {
            rightBorder = Math.max(rightBorder, idx % Board.M);
        }
        return rightBorder;
    }

    int getLeftBorder(Integer[] tetrominoState) {
        int leftBorder = Integer.MAX_VALUE;
        for(int idx : tetrominoState) {
            leftBorder = Math.min(leftBorder, idx % Board.M);
        }
        return leftBorder;
    }

    int getBottomBorder(Integer[] tetrominoState) {
        int bottomBorder = Integer.MIN_VALUE;
        for(int idx : tetrominoState) {
            bottomBorder = Math.max(bottomBorder, idx / Board.M);
        }
        return bottomBorder;
    }

    List<Integer> getLeafCells() {
        List<Integer> leafCells = new ArrayList<>(List.of());
        for(int idx : currentTetrominoState) {
            if(!Arrays.asList(currentTetrominoState).contains(idx + Board.M)) {
                leafCells.add(idx);
            }
        }
        return leafCells;
    }

    List<Integer> getRightCells() {
        List<Integer> rightCells = new ArrayList<>(List.of());
        for(int idx : currentTetrominoState) {
            if(!Arrays.asList(currentTetrominoState).contains(idx + 1)) {
                rightCells.add(idx);
            }
        }
        return rightCells;
    }

    List<Integer> getLeftCells() {
        List<Integer> leftCells = new ArrayList<>(List.of());
        for(int idx : currentTetrominoState) {
            if(!Arrays.asList(currentTetrominoState).contains(idx - 1)) {
                leftCells.add(idx);
            }
        }
        return leftCells;
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

    Integer[] getCurrentStateCords() {
        return currentTetrominoState;
    }

    Integer[] getNextStateCords() {
        int nextRotationIndex = (rotationIndex + 1) % tetromino.getStatesSize();
        return tetromino.getState(nextRotationIndex);
    }

    Integer[] getOrigin() {
        int originX = Integer.MAX_VALUE, originY = Integer.MAX_VALUE;
        for(int idx : currentTetrominoState) {
            originX = Math.min(originX, idx / Board.M);
            originY = Math.min(originY, idx % Board.M);
        }
        return new Integer[]{ originX, originY };
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
        // top-left corner of Grid inside Board.
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

    /////// Cached tetromino rotations ///////

    private void ccw() {
        transpose();
        reverse();
    }

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
