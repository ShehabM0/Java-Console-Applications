package machine;

import java.util.Map;

enum Coffee {
    ESPRESSO(
            4,
            Map.of(
                    Ingredient.WATER, 250,
                    Ingredient.MILK, 0,
                    Ingredient.BEANS, 16
            )
    ),
    LATTE(
            7,
            Map.of(
                    Ingredient.WATER, 350,
                    Ingredient.MILK, 75,
                    Ingredient.BEANS, 20
            )
    ),
    CAPPUCCINO(
            6,
            Map.of(
                    Ingredient.WATER, 200,
                    Ingredient.MILK, 100,
                    Ingredient.BEANS, 12
            )
    );

    private final int price;
    private final Map<Ingredient, Integer> recipe;

    Coffee(int price, Map<Ingredient, Integer> recipe) {
        this.price = price;
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    int getPrice() {
        return price;
    }

    int getIngredientAmount(Ingredient ingredient) {
        return recipe.get(ingredient);
    }
}
