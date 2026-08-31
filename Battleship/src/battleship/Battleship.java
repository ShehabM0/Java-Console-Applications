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
        start();
    }

    private void placeAllShips() {
        for(Ship ship : Ship.values()) {
            System.out.printf(
                    "%nEnter the coordinates of the %s (%d cells):%n",
                    ship,
                    ship.size()
            );
            placeShipInput(ship);
        }
        System.out.println();
        grid.display();
    }

    private void start() {
        Grid fogGrid = new Grid();
        System.out.println("\nThe game starts!");
        System.out.println();
        fogGrid.display();
        System.out.println("\nTake a shot!");

        String cellString;
        while(true) {
            System.out.println();
            cellString = sc.next();
            try {
                Cell cell = Cell.parseCell(cellString);
                ShootResult shootResult = grid.shoot(cell);
                fogGrid.mirror(cell, shootResult);

                System.out.println();
                fogGrid.display();
                System.out.println(
                        switch (shootResult) {
                            case HIT -> "\nYou hit a ship! Try again:";
                            case MISS -> "\nYou missed. Try again:";
                            case SANK  -> "\nYou sank a ship! Specify a new target:";
                            case WON -> "\nYou sank the last ship. You won. Congratulations!";
                        }
                );

                if(shootResult == ShootResult.WON)
                    break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void placeShipInput(Ship ship) {
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

//private void placeShipInput(Ship ship) {
//    String[] cellStrings = new String[]{
//            "F3 F7",
//            "A1 D1",
//            "J10 J8",
//            "B9 D9",
//            "I2 J2"
//    };
//    for(String cellString : cellStrings) {
//        String[] shipCell = cellString.split("\\s+");
//        if(shipCell.length != 2) {
//            System.out.println("Error!");
//            continue;
//        }
//        try {
//            ShipCells shipCells = new ShipCells(
//                    Cell.parseCell(shipCell[0]),
//                    Cell.parseCell(shipCell[1])
//            );
//            ShipPlacement shipPlacement = new ShipPlacement(ship, shipCells);
//
//            grid.placeShip(shipPlacement, shipCells);
//            grid.display();
//            break;
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
