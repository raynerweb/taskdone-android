package br.com.raynerweb.ipl.taskdone.repository.local.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long) = Date(timestamp)

    @TypeConverter
    fun toTimestamp(date: Date) = date.time

}