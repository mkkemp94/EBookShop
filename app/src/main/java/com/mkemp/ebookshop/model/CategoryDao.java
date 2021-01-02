package com.mkemp.ebookshop.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDao
{
    @Insert
    void insert(Category category);
    
    @Update
    void update(Category category);
    
    @Delete
    void delete(Category category);
    
    @Query("SELECT * FROM categories_table")
    LiveData<List<Category>> getAllCategories();
}
