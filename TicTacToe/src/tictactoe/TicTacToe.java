package tictactoe;

import java.util.*;

class TicTacToe {
    private final Scanner scanner = new Scanner(System.in);
    private final Grid grid;

    TicTacToe() {
        grid = new Grid();
        startGame();
    }

    void startGame() {
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
                System.out.println("Bad parameters!");
                continue;
            }

            Player player1 = createPlayer(args[1], CellType.X);
            Player player2 = createPlayer(args[2], CellType.O);

            grid.print();
            start(player1, player2);
            grid.reset();
        }
    }

    void start(Player player1, Player player2) {
        while(grid.getState() == GridState.NOT_FINISHED) {
            Cell player1PickedCell = player1.makeMove();
            grid.addCell(player1PickedCell, CellType.X);
            grid.print();
            if(grid.getState() != GridState.NOT_FINISHED)
                break;
            Cell player2PickedCell = player2.makeMove();
            grid.addCell(player2PickedCell, CellType.O);
            grid.print();
        }
        System.out.println(grid.getState().state());
    }

    Player createPlayer(String playerType, CellType cellType) {
        if (playerType.equalsIgnoreCase("user"))
            return new HumanPlayer(grid);

        Difficulty difficulty = getGameDifficulty(playerType);
        return new BotPlayer(grid, cellType, difficulty);
    }

    // user & bot(easy, medium, hard)
    boolean isValidPlayerType(String playerType) {
        if(playerType.equalsIgnoreCase("user"))
            return true;

        for(Difficulty botDifficulty : Difficulty.values())
            if(playerType.equalsIgnoreCase(botDifficulty.name()))
                return true;

        return false;
    }

    Difficulty getGameDifficulty(String difficultyType) {
        return switch (difficultyType.toUpperCase()) {
            case "MEDIUM" -> Difficulty.MEDIUM;
            case "HARD" -> Difficulty.HARD;
            default -> Difficulty.EASY;
        };
    }
}
