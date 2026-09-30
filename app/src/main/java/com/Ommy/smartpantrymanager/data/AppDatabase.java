package com.Ommy.smartpantrymanager.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();
    public abstract RecipeDao recipeDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase get(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "pantry.db")
                    .allowMainThreadQueries() // acceptable for a small app; note it in your report
                    .build();
        }
        return instance;
    }
}