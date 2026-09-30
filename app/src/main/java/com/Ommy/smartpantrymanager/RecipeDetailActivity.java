package com.Ommy.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Ommy.smartpantrymanager.data.AppDatabase;
import com.Ommy.smartpantrymanager.data.Recipe;
import com.Ommy.smartpantrymanager.data.RecipeIngredient;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    // Key used to pass the recipe's id from the suggestions screen to this screen
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView textTitle = findViewById(R.id.textRecipeTitle);
        TextView textIngredients = findViewById(R.id.textIngredients);
        TextView textMethod = findViewById(R.id.textMethod);

        // Read the recipe id that was sent with the Intent
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        AppDatabase db = AppDatabase.get(this);
        Recipe recipe = db.recipeDao().getRecipe(recipeId);

        if (recipe == null) {
            textTitle.setText("Recipe not found");
            return;
        }

        textTitle.setText(recipe.name);
        textMethod.setText(recipe.steps);

        // Build the ingredient list, one per line
        List<RecipeIngredient> ingredients = db.recipeDao().getIngredients(recipe.id);
        StringBuilder text = new StringBuilder();
        for (RecipeIngredient ingredient : ingredients) {
            String quantity;
            if (ingredient.quantity == (long) ingredient.quantity) {
                quantity = String.valueOf((long) ingredient.quantity);
            } else {
                quantity = String.valueOf(ingredient.quantity);
            }
            text.append("\u2022 ")
                    .append(ingredient.name)
                    .append(" - ")
                    .append(quantity)
                    .append(" ")
                    .append(ingredient.unit)
                    .append("\n");
        }
        textIngredients.setText(text.toString().trim());
    }
}