package com.example.practicalexamronaldmacwan.roomdb.dao

import androidx.room.*
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Insert
    fun insertCategory(category: Category) : Long

    @Update
    fun updateCategory(category: Category) : Int

    @Delete
    fun deleteCategory(category: Category) : Int

    @Query("DELETE FROM category_data_table")
    fun deleteAll() : Int

    @Query("SELECT * FROM category_data_table")
    fun getAllCategories():Flow<List<Category>>
}