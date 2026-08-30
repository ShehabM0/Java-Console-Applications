package battleship;

import java.util.Scanner;

class Battleship {
    private final Scanner sc;
    private final Grid grid;

    Battleship(Scanner sc) {
        this.sc = sc;
        grid = new Grid();
        grid.display();
        placeShipInput();
    }

    private void placeShipInput() {
        String[] shipCell;
        while (true) {
            System.out.println("Enter the coordinates of the ship:");
            shipCell = sc.nextLine().split("\\s+");
            if(shipCell.length != 2) {
                System.out.println("Error! Enter valid coordinates.");
                continue;
            }
            try {
                ShipCells shipCells = new ShipCells(
                        Cell.parseCell(shipCell[0]),
                        Cell.parseCell(shipCell[1])
                );
                grid.placeShip(shipCells);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
