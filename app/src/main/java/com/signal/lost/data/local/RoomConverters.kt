package com.signal.lost.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json
    private val stringListSerializer = ListSerializer(String.serializer())

    @TypeConverter
    fun stringListToJson(value: List<String>): String {
        return json.encodeToString(stringListSerializer, value)
    }

    @TypeConverter
    fun jsonToStringList(value: String): List<String> {
        return json.decodeFromString(stringListSerializer, value)
    }
}
