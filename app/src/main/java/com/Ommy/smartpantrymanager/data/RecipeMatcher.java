package com.Ommy.smartpantrymanager.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeMatcher {

    // ---------- Cleaning up names ----------

    // "  Tomatoes " -> "tomato", "Maize  Meal" -> "maize meal"
    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String[] words = raw.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (result.length() > 0) result.append(' ');
            result.append(singular(word));
        }
        return result.toString();
    }

    // Turns a plural word into its singular form (simple rules, not full language processing)
    private static String singular(String word) {
        if (word.length() <= 3) return word;
        if (word.endsWith("ies")) return word.substring(0, word.length() - 3) + "y";   // berries -> berry
        if (word.endsWith("oes") || word.endsWith("ches") || word.endsWith("shes")
                || word.endsWith("xes") || word.endsWith("sses")) {
            return word.substring(0, word.length() - 2);                                // tomatoes -> tomato
        }
        if (word.endsWith("ss") || word.endsWith("us")) return word;                    // keep as is
        if (word.endsWith("s")) return word.substring(0, word.length() - 1);            // eggs -> egg
        return word;
    }

    // ---------- Units ----------

    // Groups units so we only compare like with like: weight, volume or count
    private static String unitType(String unit) {
        String u = (unit == null) ? "" : unit.trim().toLowerCase(Locale.ROOT);
        switch (u) {
            case "g":
            case "kg":
                return "weight";
            case "ml":
            case "l":
                return "volume";
            default:
                return "count";   // pcs
        }
    }

    // Converts to the base unit: kg -> g, l -> ml
    private static double toBase(double quantity, String unit) {
        String u = (unit == null) ? "" : unit.trim().toLowerCase(Locale.ROOT);
        if (u.equals("kg") || u.equals("l")) return quantity * 1000;
        return quantity;
    }

    // ---------- The matching ----------

    // Adds up the pantry: key = "name|unitType", value = total amount in the base unit
    public static Map<String, Double> buildPantryMap(List<PantryItem> pantry) {
        Map<String, Double> map = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = normalizeName(item.name) + "|" + unitType(item.unit);
            double amount = toBase(item.quantity, item.unit);
            Double current = map.get(key);
            map.put(key, (current == null ? 0 : current) + amount);
        }
        return map;
    }

    // Counts how many of a recipe's ingredients are missing or not enough
    public static int countMissing(List<RecipeIngredient> needed, Map<String, Double> pantryMap) {
        int missing = 0;
        for (RecipeIngredient ingredient : needed) {
            String key = normalizeName(ingredient.name) + "|" + unitType(ingredient.unit);
            double required = toBase(ingredient.quantity, ingredient.unit);
            Double have = pantryMap.get(key);
            if (have == null || have + 0.0001 < required) {
                missing++;
            }
        }
        return missing;
    }

    // THE STRICT RULE: a recipe qualifies only if it has zero missing ingredients
    public static List<Recipe> findStrictMatches(AppDatabase db) {
        Map<String, Double> pantryMap = buildPantryMap(db.pantryDao().getAll());
        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : db.recipeDao().getAllRecipes()) {
            List<RecipeIngredient> needed = db.recipeDao().getIngredients(recipe.id);
            if (needed.isEmpty()) continue;   // ignore a recipe with no ingredients
            if (countMissing(needed, pantryMap) == 0) {
                matches.add(recipe);
            }
        }
        return matches;
    }
}