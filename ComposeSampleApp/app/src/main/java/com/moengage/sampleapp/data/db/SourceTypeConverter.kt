package com.moengage.sampleapp.data.db

import androidx.room.TypeConverter
import com.moengage.sampleapp.model.Source
import kotlinx.serialization.json.Json

class SourceTypeConverter {

    @TypeConverter
    fun fromSource(value: Source): String {
        return Json.encodeToString(Source.serializer(), value)
    }

    @TypeConverter
    fun toSource(value: String): Source {
        return Json.decodeFromString(Source.serializer(), value)
    }
}