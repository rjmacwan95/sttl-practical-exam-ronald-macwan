package com.example.practicalexamronaldmacwan.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.practicalexamronaldmacwan.roomdb.repository.CategoryRepository
import java.lang.IllegalArgumentException


class AddCategoryViewModelFactory(
    private val repository: CategoryRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddCategoryViewModel::class.java)) {
            return AddCategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}