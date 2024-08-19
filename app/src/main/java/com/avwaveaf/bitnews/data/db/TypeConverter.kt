package com.avwaveaf.bitnews.data.db

import androidx.room.TypeConverter
import com.avwaveaf.bitnews.data.models.Source

class TypeConverter {

    @TypeConverter
    fun fromSourceType(source: Source): String {
        return source.name.toString()
    }

    @TypeConverter
    fun toSourceType(name: String): Source {
        return Source(name, name)
    }
}