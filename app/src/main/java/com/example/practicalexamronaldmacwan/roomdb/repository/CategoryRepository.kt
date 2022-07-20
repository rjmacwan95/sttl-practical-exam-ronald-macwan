package com.example.practicalexamronaldmacwan.roomdb.repository

import com.example.practicalexamronaldmacwan.roomdb.dao.CategoryDAO
import com.example.practicalexamronaldmacwan.roomdb.entity.Category

class CategoryRepository(private val dao: CategoryDAO) {

    val categories = dao.getAllCategories()

    fun insert(category: Category): Long {
        return dao.insertCategory(category)
    }

    fun update(category: Category): Int {
        return dao.updateCategory(category)
    }

    fun delete(category: Category): Int {
        return dao.deleteCategory(category)
    }

    fun deleteAll(): Int {
        return dao.deleteAll()
    }
}