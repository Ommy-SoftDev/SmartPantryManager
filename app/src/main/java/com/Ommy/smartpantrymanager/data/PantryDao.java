package com.Ommy.smartpantrymanager.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {
    @Insert
    void insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    @Query("SELECT * FROM pantry_item ORDER BY name")
    List<PantryItem> getAll();

    @Query("SELECT * FROM pantry_item WHERE id = :id")
    PantryItem getById(int id);
}