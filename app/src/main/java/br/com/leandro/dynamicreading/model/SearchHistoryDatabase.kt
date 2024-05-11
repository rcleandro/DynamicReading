package br.com.leandro.dynamicreading.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * A RoomDatabase subclass for managing the SearchHistory database.
 *
 * This class is responsible for the following functionalities:
 * - Providing an instance of the SearchHistoryDAO interface for database operations.
 * - Building the database instance with the necessary configurations.
 *
 * The class uses Room's database builder to create the database if it doesn't exist and get the
 * instance if it does.
 *
 * @property searchHistoryDAO The DAO object that is used to interact with the SearchHistory
 * table in the database.
 */
@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {

    /**
     * Abstract function to get an instance of the SearchHistoryDAO interface.
     *
     * @return An instance of the SearchHistoryDAO interface for database operations.
     */
    abstract fun searchHistoryDAO(): SearchHistoryDAO

    companion object {

        /**
         * Function to build the SearchHistory database.
         *
         * This function uses Room's database builder to create the database if it doesn't exist
         * and get the instance if it does.
         * It sets the name of the database to "SearchHistory.db".
         *
         * @param context The context in which the database is being built.
         * @return The built database instance.
         */
        fun build(context: Context) =
            Room.databaseBuilder(context, SearchHistoryDatabase::class.java, "SearchHistory.db")
                .build()
    }
}