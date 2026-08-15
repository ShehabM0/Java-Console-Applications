package cinema;

import java.util.Scanner;

public class Cinema {
    private final Scanner sc = new Scanner(System.in);

    private final int ROWS, COLS;
    private final SeatStatus[][] SEATS;
    private final int TOTAL_SEATS, EXPECTED_INCOME;
    private int purchasedSeats, currentIncome;

    private static final int SMALL_CINEMA_LIMIT = 60;
    private static final int FRONT_PRICE = 10;
    private static final int BACK_PRICE = 8;

    Cinema() {
        System.out.println("Enter the number of rows:");
        ROWS = sc.nextInt();
        System.out.println("Enter the number of seats in each row:");
        COLS = sc.nextInt();

        SEATS = new SeatStatus[ROWS][COLS];
        TOTAL_SEATS = ROWS * COLS;
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                SEATS[i][j] = SeatStatus.AVAILABLE;

        EXPECTED_INCOME = calculateExpectedIncome();
    }

    void menu() {
        int opt;
        do {
            System.out.println("\n1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            opt = sc.nextInt();
            System.out.println();
            switch (opt) {
                case 1 -> printSeats();
                case 2 -> buyTicket();
                case 3 -> statistics();
                default -> System.out.println("Invalid option");
            }
        } while (opt != 0);
    }

    private void printSeats() {
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int j = 0; j < COLS; j++)
            System.out.print(j + 1 + " ");
        System.out.println();

        for (int i = 0; i < ROWS; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < COLS; j++) {
                System.out.print(SEATS[i][j].symbol() + " ");
            }
            System.out.println();
        }
    }

    private void buyTicket() {
        int row, col;
        do {
            System.out.println("Enter a row number:");
            row = sc.nextInt();
            System.out.println("Enter a seat number in that row:");
            col = sc.nextInt();

            if (isOutOfBound(row, col)) {
                System.out.println("\nWrong input!\n");
                continue;
            }
            if (isSeatBooked(row, col)) {
                System.out.println("\nThat ticket has already been purchased!\n");
                continue;
            }
            break;
        } while (true);

        SEATS[row - 1][col - 1] = SeatStatus.BOOKED;
        purchasedSeats++;
        int ticketPrice = ticketPrice(row);
        currentIncome += ticketPrice;

        System.out.printf("%nTicket price: $%d%n", ticketPrice);
    }

    private void statistics() {
        double seatsPercentage = ((double) purchasedSeats / TOTAL_SEATS) * 100;
        System.out.printf(
                "Number of purchased tickets: %d%nPercentage: %.2f%%%nCurrent income: $%d%nTotal income: $%d%n",
                purchasedSeats,
                seatsPercentage,
                currentIncome,
                EXPECTED_INCOME
        );
    }

    private int ticketPrice(int row) {
        if (TOTAL_SEATS <= SMALL_CINEMA_LIMIT) {
            return 10;
        }
        return row <= ROWS / 2 ? FRONT_PRICE : BACK_PRICE;
    }

    private int calculateExpectedIncome() {
        int profit = TOTAL_SEATS * FRONT_PRICE;
        if (TOTAL_SEATS > 60) {
            int frontHalf = ROWS / 2;
            int backHalf = ROWS - frontHalf;
            profit = COLS * ((frontHalf * FRONT_PRICE) + (backHalf * BACK_PRICE));
        }
        return profit;
    }

    private boolean isOutOfBound(int row, int col) {
        return row < 1 || row > ROWS || col < 1 || col > COLS;
    }

    private boolean isSeatBooked(int row, int col) {
        return SEATS[row - 1][col - 1] == SeatStatus.BOOKED;
    }
}