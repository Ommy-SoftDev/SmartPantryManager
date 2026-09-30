package com.Ommy.smartpantrymanager.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insertRecipe(Recipe recipe);

    @Insert
    void insertIngredients(List<RecipeIngredient> ingredients);

    @Query("SELECT * FROM recipe")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe getRecipe(int id);

    @Query("SELECT * FROM recipe_ingredient WHERE recipeId = :recipeId")
    List<RecipeIngredient> getIngredients(int recipeId);

    @Query("SELECT COUNT(*) FROM recipe")
    int count();
}