package tictactoe;

import java.util.*;

class TicTacToe {
    private final Scanner scanner = new Scanner(System.in);
    private final Grid grid;

    TicTacToe() {
        grid = new Grid();
    }

    void startGame() {
        displayGameInstructions();
        while (true) {
            System.out.print("Input command: ");
            String[] args = scanner.nextLine().trim().split("\\s+"); // regex consecutive whitespace

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

            grid.display();
            start(player1, player2);
            grid.reset();
        }
    }

    private void start(Player player1, Player player2) {
        while(grid.getState() == GridState.NOT_FINISHED) {
            Cell player1PickedCell = player1.makeMove();
            grid.addCell(player1PickedCell, CellType.X);
            grid.display();
            if(grid.getState() != GridState.NOT_FINISHED)
                break;
            Cell player2PickedCell = player2.makeMove();
            grid.addCell(player2PickedCell, CellType.O);
            grid.display();
        }
        System.out.println(grid.getState().getState());
    }

    private Player createPlayer(String playerType, CellType cellType) {
        String[] segments = playerType.split(":");

        Difficulty difficulty;
        if(segments.length == 1) {
            if(segments[0].equalsIgnoreCase("user")) {
                return new HumanPlayer(grid, scanner);
            } else {
                Difficulty[] difficulties = Difficulty.values();
                int idx = new Random().nextInt(3);
                difficulty = difficulties[idx];
            }
        } else { // segments.length == 2
             difficulty = getGameDifficulty(segments[1]);
        }

        return new BotPlayer(grid, cellType, difficulty);
    }

    // user | bot | bot:[easy | medium | hard]
    private boolean isValidPlayerType(String playerType) {
        String[] segments = playerType.split(":");

        if(segments.length == 1)
            if(segments[0].equalsIgnoreCase("user") ||
                    segments[0].equalsIgnoreCase("bot"))
                return true;

        if(segments.length == 2 && segments[0].equalsIgnoreCase("bot"))
            for(Difficulty botDifficulty : Difficulty.values())
                if(segments[1].equalsIgnoreCase(botDifficulty.name()))
                    return true;

        return false;
    }

    private Difficulty getGameDifficulty(String difficultyType) {
        return switch (difficultyType.toUpperCase()) {
            case "MEDIUM" -> Difficulty.MEDIUM;
            case "HARD" -> Difficulty.HARD;
            default -> Difficulty.EASY;
        };
    }

    void displayGameInstructions() {
        System.out.println("""
                --------------------------------------
                How to play:
                  start <player1> <player2>
                  exit
                Players: user | bot | bot:<difficulty>
                Difficulty: easy | medium | hard
                e.g. start user bot:medium
                --------------------------------------
                """);
    }
}
