package com.example.database.user.converter

import android.net.Uri
import androidx.room.TypeConverter

/**
 * Created by jrzeznicki on 17/02/2023.
 */
class UriConverter {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri?.toString()
    }
}
