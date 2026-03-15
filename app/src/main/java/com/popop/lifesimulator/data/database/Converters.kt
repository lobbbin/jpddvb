package com.popop.lifesimulator.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split(",")

    @TypeConverter
    fun fromIntMap(map: Map<String, Int>): String = map.entries.joinToString(";") { "${it.key}=${it.value}" }

    @TypeConverter
    fun toIntMap(value: String): Map<String, Int> {
        if (value.isEmpty()) return emptyMap()
        return value.split(";").associate {
            val (k, v) = it.split("=")
            k to v.toInt()
        }
    }
}
