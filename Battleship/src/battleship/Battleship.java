package battleship;

import java.util.Scanner;

class Battleship {
    private final Scanner sc;
    private final Player player1, player2;

    Battleship(Scanner sc) {
        this.sc = sc;
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    public void launch() {
        placeAllShips(player1);
        enterInput();
        placeAllShips(player2);
        enterInput();

        start(player1);
    }


    private void placeAllShips(Player player) {
        System.out.printf("%s, place your ships to the game field%n", player.getName());
        player.getGrid().display();
        for (Ship ship : Ship.values()) {
            System.out.printf(
                    "%nEnter the coordinates of the %s (%d cells):%n",
                    ship,
                    ship.size()
            );

            placeShipInput(player.getGrid(), ship);
        }
        System.out.println();
    }

    private void start(Player player) {
        player.displayGrids();
        System.out.printf("%s, it's your turn:%n", player.getName());

        String cellString;
        while (true) {
            System.out.println();
            cellString = sc.next(); sc.nextLine();
            try {
                Cell cell = Cell.parseCell(cellString);
                Player opponentPlayer = player == player1 ? player2 : player1;
                ShootResult shootResult = player.shoot(opponentPlayer, cell);

                System.out.println(
                        switch (shootResult) {
                            case HIT -> "\nYou hit a ship!";
                            case MISS -> "\nYou missed!";
                            case SANK -> "\nYou sank a ship!";
                            case WON -> "\nYou sank the last ship. You won. Congratulations!";
                        }
                );

                if (shootResult == ShootResult.WON)
                    break;

                enterInput();
                start(player == player1 ? player2 : player1);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void placeShipInput(Grid grid, Ship ship) {
        String[] shipCell;
        while (true) {
            System.out.println();
            shipCell = sc.nextLine().split("\\s+");
            System.out.println();
            if(shipCell.length != 2) {
                System.out.println("Error!");
                continue;
            }
            try {
                ShipCellPair shipCellPair = new ShipCellPair(
                        Cell.parseCell(shipCell[0]),
                        Cell.parseCell(shipCell[1])
                );
                ShipPlacement shipPlacement = new ShipPlacement(ship, shipCellPair);

                grid.placeShip(shipPlacement, shipCellPair);
                grid.display();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void enterInput() {
        System.out.println("\nPress Enter and pass the move to another player\n...");
        sc.nextLine();
    }
}
