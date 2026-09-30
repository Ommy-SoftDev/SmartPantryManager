package com.Ommy.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.Ommy.smartpantrymanager.data.AppDatabase;
import com.Ommy.smartpantrymanager.data.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    // Key used to pass the item's id from the list screen to this screen
    public static final String EXTRA_ITEM_ID = "item_id";

    private static final String[] UNITS = {"g", "kg", "ml", "l", "pcs"};

    private AppDatabase db;
    private EditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;
    private Button buttonDelete;

    // null means we are adding a new item; otherwise we are editing this one
    private PantryItem existingItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addEditRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.get(this);

        // Find the views on the screen
        TextView textFormTitle = findViewById(R.id.textFormTitle);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Fill the unit drop-down
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, UNITS);
        spinnerUnit.setAdapter(unitAdapter);

        // Did the list screen send us an item id? If yes, we are in edit mode.
        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            existingItem = db.pantryDao().getById(itemId);
        }

        if (existingItem != null) {
            textFormTitle.setText("Edit Ingredient");
            buttonDelete.setVisibility(View.VISIBLE);
            fillForm(existingItem);
        }

        buttonSave.setOnClickListener(v -> save());
        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    // Puts the saved values into the input boxes when editing
    private void fillForm(PantryItem item) {
        editName.setText(item.name);

        if (item.quantity == (long) item.quantity) {
            editQuantity.setText(String.valueOf((long) item.quantity));
        } else {
            editQuantity.setText(String.valueOf(item.quantity));
        }

        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equals(item.unit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }

        if (item.expiryDate != null) {
            editExpiry.setText(item.expiryDate);
        }
    }

    // Validates the input, then creates or updates the item
    private void save() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        boolean valid = true;

        // Validation rule 1: the name is required
        if (name.isEmpty()) {
            editName.setError("Please enter an ingredient name");
            valid = false;
        }

        // Validation rule 2: the quantity must be a number greater than zero
        double quantity = 0;
        if (quantityText.isEmpty()) {
            editQuantity.setError("Please enter a quantity");
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(quantityText);
                if (quantity <= 0) {
                    editQuantity.setError("Quantity must be more than zero");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                editQuantity.setError("Enter a valid number");
                valid = false;
            }
        }

        // Validation rule 3: the expiry date is optional, but must be a real date if given
        if (!expiry.isEmpty() && !isValidDate(expiry)) {
            editExpiry.setError("Use the format yyyy-MM-dd, e.g. 2026-12-31");
            valid = false;
        }

        if (!valid) return;   // stop here and show the errors

        PantryItem item = (existingItem != null) ? existingItem : new PantryItem();
        item.name = name;
        item.quantity = quantity;
        item.unit = (String) spinnerUnit.getSelectedItem();
        item.expiryDate = expiry.isEmpty() ? null : expiry;

        if (existingItem == null) {
            db.pantryDao().insert(item);          // CREATE
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.pantryDao().update(item);          // UPDATE
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();   // close this screen and go back to the list
    }

    // Checks that the text is a real date in the form yyyy-MM-dd
    private boolean isValidDate(String text) {
        if (text.length() != 10) return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false);
        try {
            format.parse(text);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Asks "are you sure?" before deleting
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + existingItem.name + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.pantryDao().delete(existingItem);   // DELETE
                    Toast.makeText(this, "Ingredient deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}