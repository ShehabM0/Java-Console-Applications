package tictactoe;

import java.util.*;

class TicTacToe {
    private final Scanner scanner = new Scanner(System.in);

    private final int GRID_SIZE = 3;
    private final CellType[][] grid = new CellType[GRID_SIZE][GRID_SIZE];

    private GridState gridState = GridState.NOT_FINISHED;
    int xCells, oCells;

    TicTacToe() {
        initGrid();

        choosePlayers();
    }

    void initGrid() {
        gridState = GridState.NOT_FINISHED;
        xCells = 0; oCells = 0;
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                grid[row][col] = CellType.EMPTY;
    }

    void choosePlayers() {
        while (true) {
            System.out.print("Input command: ");
            String[] args = scanner.nextLine().trim().split("\\s+"); // consecutive spaces

            if(args.length == 1 && args[0].equalsIgnoreCase("exit")) {
                return;
            }
            if (args.length != 3 || !args[0].equalsIgnoreCase("start")) {
                System.out.println("Bad parameters!");
                continue;
            }
            if(!isValidPlayerType(args[1]) || !isValidPlayerType(args[2])) {
                System.out.println("BBad parameters!");
                continue;
            }

            Player player1 = createPlayer(args[1], CellType.X);
            Player player2 = createPlayer(args[2], CellType.O);

            initGrid();
            printGrid();
            start(player1, player2);
        }
    }

    void start(Player player1, Player player2) {
        while(gridState == GridState.NOT_FINISHED) {
            Cell player1PickedCell = player1.makeMove();
            addCell(player1PickedCell, CellType.X);
            printGrid();
            if(gridState != GridState.NOT_FINISHED)
                break;
            Cell player2PickedCell = player2.makeMove();
            addCell(player2PickedCell, CellType.O);
            printGrid();
        }
        System.out.println(gridState.state());
    }

    Player createPlayer(String playerType, CellType cellType) {
        if (playerType.equalsIgnoreCase("user"))
            return new HumanPlayer(grid);

        Difficulty difficulty = getGameDifficulty(playerType);
        return new BotPlayer(grid, cellType, difficulty);
    }

    // user & bot(easy, medium)
    boolean isValidPlayerType(String playerType) {
        if(playerType.equalsIgnoreCase("user"))
            return true;

        for(Difficulty botDifficulty : Difficulty.values())
            if(playerType.equalsIgnoreCase(botDifficulty.name()))
                return true;

        return false;
    }

    Difficulty getGameDifficulty(String difficultyType) {
        return difficultyType.equalsIgnoreCase(Difficulty.EASY.name()) ? Difficulty.EASY : Difficulty.MEDIUM;
    }

    void updateGridState() {
        boolean xWins = false, oWins = false;
        if(scanGrid(CellType.X))
            xWins = true;
        if(scanGrid(CellType.O))
            oWins = true;

        if(xWins) {
            gridState = GridState.X;
        } else if(oWins) {
            gridState = GridState.O;
        } else if(xCells + oCells == GRID_SIZE * GRID_SIZE) {
            gridState = GridState.DRAW;
        }
    }

    boolean scanGrid(CellType cellType) {
        int i, j;
        // row, col
        for(i = 0; i < GRID_SIZE; i++) {
            int rowCnt = 0, colCnt = 0;
            for(j = 0; j < GRID_SIZE; j++) {
                rowCnt += grid[i][j] == cellType ? 1 : 0;
                colCnt += grid[j][i] == cellType ? 1 : 0;
            }
            if(rowCnt == GRID_SIZE || colCnt == GRID_SIZE)
                return true;
        }

        // main-diagonal
        int k = 0;
        while(k < GRID_SIZE && grid[k][k] == cellType) {
            k++;
        }

        // anti-diagonal
        i = 0; j = GRID_SIZE - 1;
        while(i < GRID_SIZE && j > -1 && grid[i][j] == cellType) {
            i++;
            j--;
        }

        return (k == GRID_SIZE) || (i == GRID_SIZE && j == -1);
    }

    void addCell(Cell cell, CellType cellType) {
        int row = cell.x(), col = cell.y();
        grid[row][col] = cellType;
        xCells += grid[row][col] == CellType.X ? 1 : 0;
        oCells += grid[row][col] == CellType.O ? 1 : 0;
        updateGridState();
    }

    void printGrid() {
        final int ind = 2 + GRID_SIZE + (GRID_SIZE - 1) + 2;
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
        for(int i = 0; i < GRID_SIZE; i++) {
            System.out.print("| ");
            for(int j = 0; j < GRID_SIZE; j++)
                System.out.print(grid[i][j].symbol() + " ");
            System.out.println('|');
        }
        for(int i = 0; i < ind; i++) {
            System.out.print('-');
        }
        System.out.println();
    }
}
