package com.Ommy.smartpantrymanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_item")
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double quantity;
    public String unit;        // "g", "kg", "ml", "l", "pcs"
    public String expiryDate;  // optional, "yyyy-MM-dd" or null
}