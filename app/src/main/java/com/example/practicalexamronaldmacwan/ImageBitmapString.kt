package com.example.practicalexamronaldmacwan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class ImageBitmapString {
    companion object {
        @TypeConverter
        fun bitMapToString(bitmap: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        @TypeConverter
        fun stringToBitMap(encodedString: String?): Bitmap? {
            return try {
                val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                bitmap
            } catch (e: Exception) {
                e.message
                null
            }
        }
    }
}


