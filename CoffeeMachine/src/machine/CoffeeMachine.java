package machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CoffeeMachine {
    private final Scanner sc = new Scanner(System.in);

    private final Coffee[] coffees = Coffee.values();

    private final IngredientAmount[] inventory = {
            new IngredientAmount(Ingredient.WATER, 400),
            new IngredientAmount(Ingredient.MILK, 540),
            new IngredientAmount(Ingredient.BEANS, 120)
    };
    private int cups = 9;

    private int cupsMade = 0;
    private int cash = 550;

    void menu() {
        String action;
        while (true) {
            displayActions();
            action = sc.next();
            try {
                switch (Action.valueOf(action.toUpperCase())) {
                    case BUY -> buyDrink();
                    case FILL -> fillMachine();
                    case TAKE -> collectAllCash();
                    case CLEAN -> cleanMachine();
                    case REMAINING -> displayMachineStatus();
                    case EXIT -> { return; }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown action: " + action);
            }
        }
    }

    private void buyDrink() {
        final int CLEAN_LIMIT = 10;
        if(cupsMade == CLEAN_LIMIT) {
            System.out.println("I need cleaning!");
            return;
        }

        System.out.print("What do you want to buy?");
        for(int i = 0; i < coffees.length; i++)
            System.out.printf(" %d - %s, ", i + 1, coffees[i]);
        System.out.println("back - to main menu:");

        String buyAction = sc.next();
        if(buyAction.equalsIgnoreCase("back"))
            return;
        int coffeeNumber = validateCoffeeNumber(buyAction);

        Coffee coffee = coffees[coffeeNumber - 1];
        List<Ingredient> shortage = getInventoryShortage(coffee);
        if(!shortage.isEmpty()) {
            displayInventoryShortage(shortage);
            return;
        }

        if(cups == 0) {
            System.out.println("Sorry, not enough cups!");
            return;
        }

        System.out.println("I have enough resources, making you a coffee!");
        for(IngredientAmount stock : inventory) {
            Ingredient ingredient = stock.getIngredient();
            int coffeeIngredientAmount = coffee.getIngredientAmount(ingredient);
            stock.consume(coffeeIngredientAmount);
        }
        cups--;
        cupsMade++;
        cash += coffee.getPrice();

        System.out.println();
    }

    private void fillMachine() {
        Ingredient[] ingredients = Ingredient.values();
        int ingredientsLength = ingredients.length;

        for(int i = 0; i < ingredientsLength; i++) {
            String ingredientName = ingredients[i].toString();
            String ingredientAmount = ingredients[i].getUnit();
            System.out.printf(
                    "Write how many %s of %s you want to add:%n",
                    ingredientAmount,
                    ingredientName
            );
            int amount = validateFillNumber(sc.next());
            inventory[i].fill(amount);
        }

        System.out.println("Write how many disposable cups you want to add:");
        int addCups = validateFillNumber(sc.next());
        cups += addCups;

        System.out.println();
    }

    private void collectAllCash() {
        System.out.printf("I gave you $%d%n%n", cash);
        cash = 0;
    }

    private void cleanMachine() {
        System.out.println("I have been cleaned!");
        cupsMade = 0;
    }

    private List<Ingredient> getInventoryShortage(Coffee coffee) {
        List<Ingredient> shortageIngredients = new ArrayList<>();
        for(IngredientAmount stock : inventory) {
            Ingredient ingredient = stock.getIngredient();
            int stockIngredientAmount = stock.getAmount();
            int coffeeIngredientAmount = coffee.getIngredientAmount(ingredient);
            if(stockIngredientAmount < coffeeIngredientAmount)
                shortageIngredients.add(ingredient);
        }
        return shortageIngredients;
    }

    void displayMachineStatus() {
        System.out.println("The coffee machine has:");
        for (IngredientAmount item : inventory) {
            System.out.printf(
                    "%d %s of %s%n",
                    item.getAmount(),
                    item.getIngredient().getUnit(),
                    item.getIngredient()
            );
        }
        System.out.printf("%d disposable cups%n", cups);
        System.out.printf("$%d of money%n", cash);
    }

    void displayInventoryShortage(List<Ingredient> shortage) {
        System.out.printf("Sorry, not enough %s", shortage.getFirst());
        int shortageLen = shortage.size();
        for(int i = 1; i < shortageLen; i++)
            System.out.printf("%s %s", i == shortageLen - 1 ? " and" : ",", shortage.get(i));
        System.out.println("!");
    }

    private void displayActions() {
        System.out.print("Write action (");
        Action[] actions = Action.values();
        int actionsLen = actions.length;
        for (int i = 0; i < actionsLen; i++)
            System.out.printf(
                    "%s%s",
                    actions[i],
                    i == actionsLen - 1 ? "" : ", "
            );
        System.out.println("):");
    }

    private int validateInputNumber(String input) {
        while (true) {
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number!");
                input = sc.next();
            }
        }
    }

    private int validateCoffeeNumber(String input) {
        int number = validateInputNumber(input);

        while(number < 1 || number > coffees.length) {
            System.out.printf("Enter a number in range (%d - %d)!%n", 1, coffees.length);
            number = validateCoffeeNumber(sc.next());
        }

        return number;
    }

    private int validateFillNumber(String input) {
        int number = validateInputNumber(input);

        while(number < 0) {
            System.out.println("Enter a non-negative number!");
            number = validateFillNumber(sc.next());
        }

        return number;
    }
}
