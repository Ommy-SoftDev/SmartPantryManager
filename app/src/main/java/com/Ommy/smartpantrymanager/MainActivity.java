package com.Ommy.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Ommy.smartpantrymanager.data.AppDatabase;
import com.Ommy.smartpantrymanager.data.DatabaseSeeder;
import com.Ommy.smartpantrymanager.data.PantryItem;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // The greeting shown above the pantry. Change this to the greeting you use at home.
    private static final String GREETING = "Dumela";

    private AppDatabase db;
    private PantryAdapter adapter;
    private TextView textEmpty, textTitle, textExpiring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep the 16dp padding from the layout and add the status bar space on top of it
        final int pad = (int) (16 * getResources().getDisplayMetrics().density);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left + pad, bars.top + pad, bars.right + pad, bars.bottom + pad);
            return insets;
        });

        // The toolbar is our navigation element: its menu opens the Settings screen
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Open the database and load the recipes on the very first run
        db = AppDatabase.get(this);
        DatabaseSeeder.seedIfEmpty(db);

        textEmpty = findViewById(R.id.textEmpty);
        textTitle = findViewById(R.id.textTitle);
        textExpiring = findViewById(R.id.textExpiring);

        // Set up the list
        RecyclerView recyclerPantry = findViewById(R.id.recyclerPantry);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(new ArrayList<>());
        recyclerPantry.setAdapter(adapter);

        // Tapping a row opens the form in edit mode, sending the item's id with the Intent
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra(AddEditActivity.EXTRA_ITEM_ID, item.id);
            startActivity(intent);
        });

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEditActivity.class)));

        Button buttonSuggested = findViewById(R.id.buttonSuggested);
        buttonSuggested.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class)));
    }

    // Puts the toolbar menu on screen
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handles taps on the toolbar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Runs every time the screen becomes visible, so everything is always up to date
    @Override
    protected void onResume() {
        super.onResume();
        showGreeting();
        loadPantry();
    }

    // Shows the greeting, with the name from Settings if there is one
    private void showGreeting() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString(SettingsActivity.KEY_USER_NAME, "");
        if (name.isEmpty()) {
            textTitle.setText(GREETING + "! Here is your pantry");
        } else {
            textTitle.setText(GREETING + ", " + name + "! Here is your pantry");
        }
    }

    private void loadPantry() {
        List<PantryItem> items = db.pantryDao().getAll();
        adapter.setItems(items);
        textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        showExpiringWarning(items);
    }

    // If the setting is on, warns about items that expire within 3 days
    private void showExpiringWarning(List<PantryItem> items) {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        boolean alertsOn = prefs.getBoolean(SettingsActivity.KEY_EXPIRING_ALERTS, true);

        int count = 0;
        if (alertsOn) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            long now = new Date().getTime();
            for (PantryItem item : items) {
                if (item.expiryDate == null || item.expiryDate.isEmpty()) continue;
                try {
                    Date expiry = format.parse(item.expiryDate);
                    long daysLeft = (expiry.getTime() - now) / (24L * 60 * 60 * 1000);
                    if (daysLeft <= 3) count++;
                } catch (ParseException e) {
                    // ignore a date that cannot be read
                }
            }
        }

        if (count > 0) {
            textExpiring.setText(count + " ingredient(s) expire within 3 days. Cook them soon!");
            textExpiring.setVisibility(View.VISIBLE);
        } else {
            textExpiring.setVisibility(View.GONE);
        }
    }
}