package com.example.period_app_01.data

import androidx.room.TypeConverter
import java.time.LocalDate

// performs conversions when inserting entries into database and using entries to perform operations
class DatesConverter {
    // defines method as a TypeConverter
    @TypeConverter
    // conversion from Long to LocalDate
    fun fromTimestamp(value: Long?): LocalDate? {
        // returns LocalDate, ofEpochDay yields number of days since 1970-01-01
        return value?.let { LocalDate.ofEpochDay(it) }
    }
    @TypeConverter
    // conversion from LocalDate to Long
    fun dateToTimestamp(date: LocalDate?): Long? {
        // toEpochDay performs the inverse conversion to ofEpochDay
        return date?.toEpochDay()
    }
}