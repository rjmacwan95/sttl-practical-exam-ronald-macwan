package com.example.practicalexamronaldmacwan.roomdb.repository

import com.example.practicalexamronaldmacwan.roomdb.dao.ContactDAO
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: ContactDAO) {

    val contacts = dao.getAllContacts()

    fun insert(contact: Contact): Long {
        return dao.insertContact(contact)
    }

    fun update(contact: Contact): Int {
        return dao.updateContact(contact)
    }

    fun delete(contact: Contact): Int {
        return dao.deleteContact(contact)
    }

    fun deleteAll(): Int {
        return dao.deleteAll()
    }

    fun search(searchQuery: String): Flow<List<Contact>> {
        return dao.searchContacts(searchQuery)
    }

    fun filter(categoryId: Int): Flow<List<Contact>> {
        return dao.filterContacts(categoryId)
    }
}