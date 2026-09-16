package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class BudgetManager {
    private final File file = new File("purchases.txt");
    private final PurchaseType[] purchaseTypes = PurchaseType.values();
    private final EnumMap<PurchaseType, List<Purchase>> purchases;
    private final Scanner sc;

    private double balance;

    BudgetManager(Scanner sc) {
        this.sc = sc;
        purchases = new EnumMap<>(PurchaseType.class);
        for (PurchaseType purchaseType : purchaseTypes)
            purchases.put(purchaseType, new ArrayList<>());
        balance = 0;
    }

    void menu() {
        boolean stop = false;
        while (!stop) {
            Action[] actions = Action.values();
            System.out.println("Choose your action:");
            displayMenuItems(Action.class, false);

            int actionNo = readNonNegInt();
            while(actionNo < 0 || actionNo >= actions.length) {
                System.out.println("Please enter a valid list number!");
                actionNo = readNonNegInt();
            }

            System.out.println();
            switch (actionNo) {
                case 1 -> incomeHandler();
                case 2 -> purchaseHandler();
                case 3 -> displayPurchases();
                case 4 -> displayBalance();
                case 5 -> saveFile();
                case 6 -> loadFile();
                case 0 -> {
                    System.out.print("\nBye!");
                    stop = true;
                }
            }
        }
    }

    private void incomeHandler() {
        System.out.println("Enter income:");
        int incomeInput = readNonNegInt();
        addIncome(incomeInput);
        System.out.println("Income was added!\n");
    }

    private void purchaseHandler() {
        System.out.println("Choose the type of purchase");
        displayMenuItems(PurchaseType.class, true);
        int menuItemNo = readNonNegInt();
        while (menuItemNo < 1 || menuItemNo > purchaseTypes.length + 1) {
            System.out.println("Please enter a valid list number!");
            menuItemNo = readNonNegInt();
        }
        if(menuItemNo == purchaseTypes.length + 1) {
            System.out.println();
            return;
        }

        System.out.println("\nEnter purchase name:");
        String purchaseName = sc.nextLine();
        System.out.println("Enter its price: ");
        double purchasePrice = readNonNegDouble();

        subIncome(purchasePrice);
        PurchaseType purchaseType = purchaseTypes[menuItemNo - 1];
        purchases.get(purchaseType).add(new Purchase(purchaseName, purchasePrice));

        System.out.println("Purchase was added!\n");
        purchaseHandler();
    }

    private void displayPurchases() {
        boolean isEmpty = isPurchasesEmpty();
        if(isEmpty) {
            System.out.println("The purchase list is empty!\n");
            return;
        }

        System.out.println("Choose the type of purchases");
        displayMenuItems(PurchaseType.class, false);
        int menuItemNo = readNonNegInt();
        while (menuItemNo < 1 || menuItemNo > purchaseTypes.length + 2) {
            System.out.println("Please enter a valid list number!");
            menuItemNo = readNonNegInt();
        }
        if(menuItemNo == purchaseTypes.length + 2) { // Back
            System.out.println();
            return;
        }

        double sum = 0;
        if(menuItemNo == purchaseTypes.length + 1) { // ALL
            System.out.println("\nAll:");
            for(List<Purchase> purchaseList : purchases.values()) {
                for(Purchase purchase : purchaseList) {
                    System.out.printf("%s $%.2f%n", purchase.item(), purchase.price());
                    sum += purchase.price();
                }
            }
        } else {
            PurchaseType purchaseType = purchaseTypes[menuItemNo - 1];
            List<Purchase> purchaseList = purchases.get(purchaseType);

            System.out.printf("%n%s:%n", purchaseType);
            if(purchaseList.isEmpty()) {
                isEmpty = true;
                System.out.println("The purchase list is empty!\n");
            } else {
                for(Purchase purchase : purchaseList) {
                    System.out.printf("%s $%.2f%n", purchase.item(), purchase.price());
                    sum += purchase.price();
                }
            }
        }

        if(!isEmpty)
            System.out.printf("Total sum: $%.2f%n%n", sum);
        displayPurchases();
    }

    private void displayBalance() {
        System.out.printf("Balance: $%.2f%n%n", balance);
    }

    private void addIncome(int price) {
        balance += price;
    }

    private void subIncome(double price) {
        balance = Math.max(0, balance - price);
    }

    private boolean isPurchasesEmpty() {
        boolean isEmpty = true;
        for(List<Purchase> purchaseList : purchases.values())
            if(!purchaseList.isEmpty()) {
                isEmpty = false;
                break;
            }
        return isEmpty;
    }

    private <T extends Enum<T>> void displayMenuItems(Class<T> enumClass, boolean isAddPurchase) {
        for(T item : enumClass.getEnumConstants())
            System.out.printf("%d) %s%n", getItemOrder(item), item);
        if(enumClass == PurchaseType.class) {
            int order = purchaseTypes.length + 1;
            if(!isAddPurchase)
                System.out.printf("%d) %s%n", order++, "All");
            System.out.printf("%d) %s%n", order, "Back");
        }
    }

    private <T extends Enum<T>> int getItemOrder(T item) {
        if(item == Action.EXIT)
            return 0;
        return item.ordinal() + 1;
    }

    private void loadFile() {
        try (Scanner scanner = new Scanner(file)) {
            balance = Double.parseDouble(scanner.nextLine());
            int purchaseTypesCount = purchaseTypes.length;
            while (purchaseTypesCount-- > 0) {
                String purchaseTypeStr = scanner.nextLine().toUpperCase();
                PurchaseType purchaseType = PurchaseType.valueOf(purchaseTypeStr);
                purchases.get(purchaseType).clear();

                int itemsCount = Integer.parseInt(scanner.nextLine());
                while (itemsCount-- > 0) {
                    String itemName = scanner.nextLine();
                    double itemPrice = Double.parseDouble(scanner.nextLine());
                    purchases.get(purchaseType).add(new Purchase(itemName, itemPrice));
                }
            }
            System.out.println("Purchases were loaded!\n");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    private void saveFile() {
        try(FileWriter writer = new FileWriter(file, true)) {
            writer.write(String.format("%.2f%n", balance));
            for(var purchase : purchases.entrySet()) {
                writer.write(String.format("%s%n", purchase.getKey()));
                writer.write(String.format("%d%n", purchase.getValue().size()));
                for(Purchase item : purchase.getValue()) {
                    writer.write(String.format("%s%n", item.item()));
                    writer.write(String.format("%.2f%n", item.price()));
                }
            }
            System.out.println("Purchases were saved!\n");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e);
        }
    }

    private int readNonNegInt() {
        int in = -1;
        while (in == -1) {
            try {
                in = Integer.parseInt(sc.nextLine());
                if(in < 0) {
                    System.out.println("Please enter a valid number!");
                    in = -1;
                }
            } catch (NumberFormatException _) {
                System.out.println("Please enter a valid number!");
            }
        }
        return in;
    }

    private double readNonNegDouble() {
        double in = -1;
        while (in == -1) {
            try {
                in = Double.parseDouble(sc.nextLine());
                if(in < 0) {
                    System.out.println("Please enter a valid number!");
                    in = -1;
                }
            } catch (NumberFormatException _) {
                System.out.println("Please enter a valid number!");
            }
        }
        return in;
    }
}
