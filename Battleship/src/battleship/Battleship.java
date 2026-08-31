package battleship;

import java.util.Scanner;

class Battleship {
    private final Scanner sc;
    private final Grid grid;

    Battleship(Scanner sc) {
        this.sc = sc;
        grid = new Grid();
        grid.display();
        placeAllShips();
    }

    private void placeAllShips() {
        for(Ship ship : Ship.values()) {
            System.out.printf(
                    "Enter the coordinates of the %s (%d cells):%n",
                    ship,
                    ship.size()
            );
            placeShipInput(ship);
        }
    }

    private void placeShipInput(Ship ship) {
        String[] shipCell;
        while (true) {
            shipCell = sc.nextLine().split("\\s+");
            if(shipCell.length != 2) {
                System.out.println("Error!");
                continue;
            }
            try {
                ShipCells shipCells = new ShipCells(
                        Cell.parseCell(shipCell[0]),
                        Cell.parseCell(shipCell[1])
                );
                new ShipPlacement(ship, shipCells);

                grid.placeShip(shipCells);
                grid.display();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
