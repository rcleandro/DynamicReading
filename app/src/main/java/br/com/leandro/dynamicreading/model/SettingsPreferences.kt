package br.com.leandro.dynamicreading.model

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val PREFERENCES_NAME = "br.com.leandro.dynamicreading.database.SettingsPreferences"
private const val WORDS_PER_MINUTE = "WordsPerMinute"
private const val NUMBER_OF_WORDS = "NumberOfWords"

/**
 * A class responsible for managing the settings preferences of the application.
 *
 * This class is responsible for the following functionalities:
 * - Retrieving the words per minute preference from the shared preferences.
 * - Saving the words per minute preference to the shared preferences.
 *
 * The class uses Android's SharedPreferences to persist the words per minute preference.
 *
 * @property preferences The SharedPreferences object that is used to interact with the
 * shared preferences.
 */
class SettingsPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * This function retrieves the words per minute preference from the shared preferences.
     *
     * @return The words per minute preference, or 300 if no preference is found.
     */
    fun getWordsPerMinutePreference() = preferences.getInt(WORDS_PER_MINUTE, 300)

    /**
     * This function saves the words per minute preference to the shared preferences.
     *
     * @param value The words per minute value to be saved.
     */
    fun setWordsPerMinutePreference(value: Int) = preferences.edit { putInt(WORDS_PER_MINUTE, value) }

    /**
     * This function retrieves the number of words preference from the shared preferences.
     *
     * @return The number of words preference, or 600 if no preference is found.
     */
    fun getNumberOfWordsPreference() = preferences.getInt(NUMBER_OF_WORDS, 600)

    /**
     * This function saves the number of words preference to the shared preferences.
     *
     * @param value The number of words value to be saved.
     */
    fun setNumberOfWordsPreference(value: Int) = preferences.edit { putInt(NUMBER_OF_WORDS, value) }
}