package com.example.practicalexamronaldmacwan.roomdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.practicalexamronaldmacwan.ImageBitmapString
import com.example.practicalexamronaldmacwan.roomdb.dao.CategoryDAO
import com.example.practicalexamronaldmacwan.roomdb.dao.ContactDAO
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact

@Database(entities = [Category::class, Contact::class], version = 1)
abstract class PracticalExamDatabase : RoomDatabase() {
    abstract val categoryDAO: CategoryDAO
    abstract val contactDAO: ContactDAO

    companion object {
        @Volatile
        private var INSTANCE: PracticalExamDatabase? = null
        fun getInstance(context: Context): PracticalExamDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PracticalExamDatabase::class.java,
                        "practical_exam_data_database"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}

