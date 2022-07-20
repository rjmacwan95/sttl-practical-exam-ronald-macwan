package com.example.practicalexamronaldmacwan.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_data_table")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    var id: Int,

    @ColumnInfo(name = "contact_image")
    var image: String,

    @ColumnInfo(name = "contact_first_name")
    var firstName: String,

    @ColumnInfo(name = "contact_last_name")
    var lastName: String,

    @ColumnInfo(name = "contact_mobile_number")
    var mobileNumber: String,

    @ColumnInfo(name = "contact_email")
    var email: String,

    @ColumnInfo(name = "category_name")
    var categoryName: String,

    @ColumnInfo(name = "category_id")
    var categoryId: Int,
)