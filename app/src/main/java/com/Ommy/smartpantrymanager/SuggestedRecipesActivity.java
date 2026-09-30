package com.Ommy.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ommy.smartpantrymanager.data.AppDatabase;
import com.Ommy.smartpantrymanager.data.Recipe;
import com.Ommy.smartpantrymanager.data.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecipeAdapter adapter;
    private TextView textNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.suggestedRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.get(this);

        // Set up the list
        textNoRecipes = findViewById(R.id.textNoRecipes);
        RecyclerView recyclerRecipes = findViewById(R.id.recyclerRecipes);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(new ArrayList<>());
        recyclerRecipes.setAdapter(adapter);

        // Tapping a recipe opens the detail screen, sending the recipe's id with the Intent
        adapter.setOnRecipeClickListener(recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.id);
            startActivity(intent);
        });
    }

    // Re-runs the matching every time this screen is shown
    @Override
    protected void onResume() {
        super.onResume();
        List<Recipe> matches = RecipeMatcher.findStrictMatches(db);
        adapter.setRecipes(matches);
        textNoRecipes.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
    }
}