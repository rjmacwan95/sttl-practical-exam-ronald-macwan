package com.example.practicalexamronaldmacwan.roomdb.dao

import androidx.room.*
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDAO {
    @Insert
    fun insertContact(contact: Contact) : Long

    @Update
    fun updateContact(contact: Contact) : Int

    @Delete
    fun deleteContact(contact: Contact) : Int

    @Query("DELETE FROM contact_data_table")
    fun deleteAll() : Int

    @Query("SELECT * FROM contact_data_table")
    fun getAllContacts():Flow<List<Contact>>

    @Query("SELECT * FROM contact_data_table WHERE contact_first_name LIKE :searchQuery OR contact_last_name LIKE :searchQuery")
    fun searchContacts(searchQuery: String): Flow<List<Contact>>

    @Query("SELECT * FROM contact_data_table WHERE category_id LIKE :categoryId")
    fun filterContacts(categoryId: Int): Flow<List<Contact>>
}