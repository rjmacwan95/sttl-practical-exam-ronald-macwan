package com.example.practicalexamronaldmacwan.ui.contact

import android.util.Patterns
import androidx.lifecycle.*
import com.example.practicalexamronaldmacwan.Event
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact
import com.example.practicalexamronaldmacwan.roomdb.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel (private val repository: ContactRepository) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var contactToUpdateOrDelete: Contact
    val actionBarTitle = MutableLiveData<String>()
    val image = MutableLiveData<String>()
    val inputFirstName = MutableLiveData<String>()
    val inputLastName = MutableLiveData<String>()
    val inputMobileNumber = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val inputCategoryName = MutableLiveData<String>()
    val inputCategoryId = MutableLiveData<Int>()

    val saveOrUpdateButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        actionBarTitle.value = "Add Contact"
    }

    fun initCategory(categoryId: Int, categoryName: String) {
        inputCategoryId.value = categoryId
        inputCategoryName.value = categoryName
    }

    fun initUpdateAndDelete(contact: Contact) {
        image.value = contact.image
        inputFirstName.value = contact.firstName
        inputLastName.value = contact.lastName
        inputMobileNumber.value = contact.mobileNumber
        inputEmail.value = contact.email
        inputCategoryId.value = contact.categoryId
        inputCategoryName.value = contact.categoryName
        isUpdateOrDelete = true
        contactToUpdateOrDelete = contact
        saveOrUpdateButtonText.value = "Update"
        actionBarTitle.value = "Update Contact"
    }

    fun saveOrUpdate() {
        if (image.value == null || image.value == "") {
            statusMessage.value = Event("Please select image")
        } else if (inputFirstName.value == null || inputFirstName.value == "") {
            statusMessage.value = Event("Please enter first name")
        } else if (inputLastName.value == null || inputLastName.value == "") {
            statusMessage.value = Event("Please enter last name")
        } else if (inputMobileNumber.value == null || inputMobileNumber.value == "") {
            statusMessage.value = Event("Please enter mobile number")
        } else if (inputMobileNumber.value != null && inputMobileNumber.value!!.length != 10) {
            statusMessage.value = Event("Please enter a correct mobile number")
        } else if (inputEmail.value == null || inputEmail.value == "") {
            statusMessage.value = Event("Please enter email address")
        } else if (inputEmail.value != null && !Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        }  else if (inputCategoryName.value == null || inputCategoryName.value == ""
            || inputCategoryId.value == null || inputCategoryId.value == 0) {
            statusMessage.value = Event("Please select category")
        }else {
            if (isUpdateOrDelete) {
                contactToUpdateOrDelete.image = image.value!!
                contactToUpdateOrDelete.firstName = inputFirstName.value!!
                contactToUpdateOrDelete.lastName = inputLastName.value!!
                contactToUpdateOrDelete.mobileNumber = inputMobileNumber.value!!
                contactToUpdateOrDelete.email = inputEmail.value!!
                contactToUpdateOrDelete.categoryId = inputCategoryId.value!!
                contactToUpdateOrDelete.categoryName = inputCategoryName.value!!
                updateContact(contactToUpdateOrDelete)
            } else {
                val image = image.value!!
                val firstName = inputFirstName.value!!
                val lastName = inputLastName.value!!
                val mobileNumber = inputMobileNumber.value!!
                val email = inputEmail.value!!
                val categoryId = inputCategoryId.value!!
                val categoryName = inputCategoryName.value!!

                insertContact(Contact(0, image,firstName,lastName,mobileNumber,email,categoryName,categoryId,))
                resetContact()
            }
        }
    }

    private fun insertContact(contact: Contact) = viewModelScope.launch {
        val newRowId = repository.insert(contact)
        if (newRowId > -1) {
            statusMessage.value = Event("Contact Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    private fun updateContact(contact: Contact) = viewModelScope.launch {
        val noOfRows = repository.update(contact)
        if (noOfRows > 0) {
            resetContact()
                statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedContacts() = liveData {
        repository.contacts.collect {
            emit(it)
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteContact(contactToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(contact)
        if (noOfRowsDeleted > 0) {
            resetContact()
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Contact Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun searchContacts(searchQuery: String): LiveData<List<Contact>> {
        return repository.search(searchQuery).asLiveData()
    }

    fun filterContacts(categoryId: Int): LiveData<List<Contact>> {
        return repository.filter(categoryId).asLiveData()
    }

    fun resetContact() {
        image.value = ""
        inputFirstName.value = ""
        inputLastName.value = ""
        inputMobileNumber.value = ""
        inputEmail.value = ""
        inputCategoryId.value = 0
        inputCategoryName.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        actionBarTitle.value = "Add Contact"
    }
}