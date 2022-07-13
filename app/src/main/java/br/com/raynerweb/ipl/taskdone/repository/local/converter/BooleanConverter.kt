package br.com.raynerweb.ipl.taskdone.repository.local.converter

import androidx.room.TypeConverter

class BooleanConverter {

    @TypeConverter
    fun toInt(value: Boolean) = if (value) 1 else 0

    @TypeConverter
    fun toBoolean(value: Int) = value > 0
}