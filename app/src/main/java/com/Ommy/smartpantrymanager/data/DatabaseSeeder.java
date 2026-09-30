package com.Ommy.smartpantrymanager.data;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {

    // Runs every time the app starts, but only adds recipes when the table is empty
    public static void seedIfEmpty(AppDatabase db) {
        RecipeDao dao = db.recipeDao();
        if (dao.count() > 0) return;   // recipes already saved, so do nothing

        add(dao, "Pap and Morogo",
                "1. Boil the water with a pinch of salt.\n2. Stir in the maize meal and cook slowly until thick.\n3. Fry the onion and tomato in oil, add the morogo and cook until soft.\n4. Serve the pap with the morogo.",
                ing("maize meal", 250, "g"), ing("water", 500, "ml"), ing("morogo", 200, "g"),
                ing("onion", 1, "pcs"), ing("tomato", 1, "pcs"), ing("oil", 15, "ml"), ing("salt", 2, "g"));

        add(dao, "Soft Porridge (Bogobe)",
                "1. Bring the water to the boil with a pinch of salt.\n2. Mix the maize meal with a little cold water, then stir it into the pot.\n3. Cook on low heat, stirring, until smooth and soft.",
                ing("maize meal", 100, "g"), ing("water", 500, "ml"), ing("salt", 1, "g"));

        add(dao, "Pap and Tomato Gravy",
                "1. Cook the pap with water and salt until thick.\n2. Fry the onion in oil, add chopped tomatoes and simmer into a gravy.\n3. Serve the gravy over the pap.",
                ing("maize meal", 250, "g"), ing("water", 500, "ml"), ing("tomato", 3, "pcs"),
                ing("onion", 1, "pcs"), ing("oil", 15, "ml"), ing("salt", 2, "g"));

        add(dao, "Dikgobe (Samp and Beans)",
                "1. Soak the samp and beans overnight.\n2. Boil them together in water for about 2 hours until soft.\n3. Fry the onion in oil and stir it in.\n4. Season with salt.",
                ing("samp", 250, "g"), ing("beans", 150, "g"), ing("water", 1000, "ml"),
                ing("onion", 1, "pcs"), ing("oil", 15, "ml"), ing("salt", 3, "g"));

        add(dao, "Magwinya (Fat Cakes)",
                "1. Mix the flour, sugar, yeast and salt.\n2. Add warm water and knead into a soft dough.\n3. Let it rise for one hour.\n4. Shape into balls and deep fry until golden.",
                ing("flour", 500, "g"), ing("sugar", 30, "g"), ing("yeast", 10, "g"),
                ing("salt", 3, "g"), ing("water", 300, "ml"), ing("oil", 500, "ml"));

        add(dao, "Chakalaka",
                "1. Fry the onion in oil.\n2. Add grated carrots, chopped tomatoes and chilli.\n3. Simmer for 20 minutes and season with salt.",
                ing("onion", 1, "pcs"), ing("carrot", 2, "pcs"), ing("tomato", 2, "pcs"),
                ing("chilli", 1, "pcs"), ing("oil", 15, "ml"), ing("salt", 2, "g"));

        add(dao, "Cabbage and Potatoes",
                "1. Fry the onion in oil.\n2. Add the diced potatoes and cook for 5 minutes.\n3. Add the shredded cabbage and a splash of water.\n4. Cover and cook until soft, then season with salt.",
                ing("cabbage", 300, "g"), ing("potato", 3, "pcs"), ing("onion", 1, "pcs"),
                ing("oil", 15, "ml"), ing("salt", 2, "g"));

        add(dao, "Rice and Beans",
                "1. Boil the beans until tender.\n2. Fry the onion in oil and add the beans.\n3. Serve over cooked rice.",
                ing("rice", 200, "g"), ing("beans", 150, "g"), ing("onion", 1, "pcs"),
                ing("oil", 15, "ml"), ing("salt", 2, "g"));

        add(dao, "Egg and Tomato Relish",
                "1. Fry the onion in oil.\n2. Add the chopped tomatoes and cook until soft.\n3. Break in the eggs and stir until cooked.",
                ing("egg", 3, "pcs"), ing("tomato", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("oil", 10, "ml"), ing("salt", 1, "g"));

        add(dao, "Chicken Stew",
                "1. Brown the chicken pieces in oil.\n2. Add the onions, tomatoes and potatoes.\n3. Simmer slowly until the chicken is cooked through.\n4. Season with salt.",
                ing("chicken", 500, "g"), ing("onion", 2, "pcs"), ing("tomato", 3, "pcs"),
                ing("potato", 2, "pcs"), ing("oil", 20, "ml"), ing("salt", 3, "g"));

        add(dao, "Slow-Cooked Pounded Beef",
                "1. Boil the beef in water with salt for about 3 hours until very tender.\n2. Pound or shred the meat.\n3. Return it to the pot with a little of the liquid.",
                ing("beef", 1000, "g"), ing("water", 1500, "ml"), ing("salt", 5, "g"));

        add(dao, "Pumpkin Porridge",
                "1. Boil the pumpkin until soft and mash it.\n2. Stir in the maize meal and water.\n3. Cook on low heat until thick, then add sugar and salt.",
                ing("pumpkin", 300, "g"), ing("maize meal", 100, "g"), ing("water", 500, "ml"),
                ing("sugar", 20, "g"), ing("salt", 1, "g"));

        add(dao, "Sorghum Porridge",
                "1. Bring the water to the boil.\n2. Mix the sorghum meal with a little cold water and stir it in.\n3. Cook slowly, stirring often, until thick.",
                ing("sorghum meal", 200, "g"), ing("water", 600, "ml"), ing("salt", 1, "g"));

        add(dao, "Buttered Bread",
                "1. Slice the bread.\n2. Spread the butter on each slice.",
                ing("bread", 2, "pcs"), ing("butter", 10, "g"));

        add(dao, "Fried Egg Sandwich",
                "1. Fry the egg in a little oil.\n2. Place it between the slices of bread.",
                ing("bread", 2, "pcs"), ing("egg", 1, "pcs"), ing("oil", 5, "ml"));

        add(dao, "Beetroot Salad",
                "1. Boil the beetroot until soft, peel and slice it.\n2. Slice the onion thinly.\n3. Mix with vinegar, sugar and salt and leave for 30 minutes.",
                ing("beetroot", 2, "pcs"), ing("onion", 1, "pcs"), ing("vinegar", 30, "ml"),
                ing("sugar", 5, "g"), ing("salt", 1, "g"));
    }

    // Helper: builds one ingredient object
    private static RecipeIngredient ing(String name, double quantity, String unit) {
        RecipeIngredient i = new RecipeIngredient();
        i.name = name;
        i.quantity = quantity;
        i.unit = unit;
        return i;
    }

    // Helper: saves one recipe, then saves its ingredients linked to that recipe
    private static void add(RecipeDao dao, String name, String steps, RecipeIngredient... ingredients) {
        Recipe r = new Recipe();
        r.name = name;
        r.steps = steps;
        long id = dao.insertRecipe(r);        // the database returns the new recipe's id

        List<RecipeIngredient> list = new ArrayList<>();
        for (RecipeIngredient i : ingredients) {
            i.recipeId = (int) id;            // link each ingredient to its recipe
            list.add(i);
        }
        dao.insertIngredients(list);
    }
}