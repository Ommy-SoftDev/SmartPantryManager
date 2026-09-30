package com.Ommy.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    // Names used to store the settings on the phone (SharedPreferences)
    public static final String PREFS_NAME = "pantry_prefs";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_EXPIRING_ALERTS = "expiring_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        // Keep the 16dp padding from the layout and add the status bar space on top of it
        final int pad = (int) (16 * getResources().getDisplayMetrics().density);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left + pad, bars.top + pad, bars.right + pad, bars.bottom + pad);
            return insets;
        });

        EditText editUserName = findViewById(R.id.editUserName);
        SwitchMaterial switchExpiring = findViewById(R.id.switchExpiring);
        Button buttonSave = findViewById(R.id.buttonSaveSettings);

        // Load the saved settings into the screen
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editUserName.setText(prefs.getString(KEY_USER_NAME, ""));
        switchExpiring.setChecked(prefs.getBoolean(KEY_EXPIRING_ALERTS, true));

        // Save the settings when the button is tapped
        buttonSave.setOnClickListener(v -> {
            prefs.edit()
                    .putString(KEY_USER_NAME, editUserName.getText().toString().trim())
                    .putBoolean(KEY_EXPIRING_ALERTS, switchExpiring.isChecked())
                    .apply();
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}