package com.Ommy.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private PantryAdapter adapter;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Open the database and load the recipes on the very first run
        db = AppDatabase.get(this);
        DatabaseSeeder.seedIfEmpty(db);

        // Set up the list
        textEmpty = findViewById(R.id.textEmpty);
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

        // The Add button opens the same form with no id, so it works in add mode
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddEditActivity.class)));
    }

    // Runs every time the screen becomes visible, so the list is always up to date
    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }

    private void loadPantry() {
        List<PantryItem> items = db.pantryDao().getAll();
        adapter.setItems(items);
        textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }
}