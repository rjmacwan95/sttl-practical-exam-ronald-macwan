package com.example.practicalexamronaldmacwan.ui.category

import androidx.lifecycle.*
import com.example.practicalexamronaldmacwan.Event
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import com.example.practicalexamronaldmacwan.roomdb.repository.CategoryRepository
import kotlinx.coroutines.launch

class AddCategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var categoryToUpdateOrDelete: Category
    val inputName = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
    }

    fun initUpdateAndDelete(category: Category) {
        inputName.value = category.name
        isUpdateOrDelete = true
        categoryToUpdateOrDelete = category
        saveOrUpdateButtonText.value = "Update"
    }

    fun saveOrUpdate() {
        if (inputName.value != null && inputName.value != "") {
            if (isUpdateOrDelete) {
                categoryToUpdateOrDelete.name = inputName.value!!
                updateCategory(categoryToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                insertCategory(Category(0, name))
                inputName.value = ""
            }
        } else {
            statusMessage.value = Event("Please enter category name")
        }
    }

    private fun insertCategory(category: Category) = viewModelScope.launch {
        val newRowId = repository.insert(category)
        if (newRowId > -1) {
            statusMessage.value = Event("Category Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    private fun updateCategory(category: Category) = viewModelScope.launch {
        val noOfRows = repository.update(category)
        if (noOfRows > 0) {
            inputName.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedCategories() = liveData {
        repository.categories.collect {
            emit(it)
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteCategory(categoryToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(category)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Category Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}