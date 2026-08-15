package cinema;

enum SeatStatus {
    AVAILABLE('S'),
    BOOKED('B');

    private final char symbol;

    SeatStatus(char symbol) {
        this.symbol = symbol;
    }

    char symbol() {
        return symbol;
    }
}
