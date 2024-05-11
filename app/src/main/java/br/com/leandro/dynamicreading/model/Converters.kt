package br.com.leandro.dynamicreading.model

import androidx.room.TypeConverter
import java.util.Date

/**
 * A Kotlin object that serves as a utility for converting between different data types.
 * This object is used in conjunction with Room database to convert non-primitive types to
 * a form that Room can understand and vice versa.
 *
 * @property fromTimestamp A function that converts a Long timestamp value into a Date object.
 * @property dateToTimestamp A function that converts a Date object into a Long timestamp value.
 */
object Converters {

    /**
     * Converts a Long timestamp value into a Date object.
     *
     * @param value The Long timestamp value to be converted.
     * @return The converted Date object, or null if the input value is null.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object into a Long timestamp value.
     *
     * @param date The Date object to be converted.
     * @return The converted Long timestamp value, or null if the input date is null.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}