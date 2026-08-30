package battleship;

record ShipCells(Cell from, Cell to) {
    ShipCells {
        if(!isValidCords(from, to))
            throw new IllegalArgumentException("Error! Wrong ship location! Try again:");

        if(compare(from, to) > 0) {
            Cell temp = from;
            from = to;
            to = temp;
        }
    }

    void displayCordsInfo() {
        System.out.printf("Length: %d%n", to.x() - from.x() + to.y() - from.y() + 1);
        System.out.print("Parts: ");
        for(int i = from.x(); i <= to.x(); i++)
            for(int j = from().y(); j <= to().y(); j++)
                System.out.print((char) (i + 'A') + "" + (j + 1) + " ");
        System.out.println();
    }

    private static boolean isValidCords(Cell from, Cell to) {
        return from.x() == to.x() || from.y() == to.y();
    }

    private int compare(Cell c1, Cell c2) {
        if(c1.x() == c2.x())
            return Integer.compare(c1.y(), c2.y());
        return Integer.compare(c1.x(), c2.x());
    }
}
