package br.com.leandro.dynamicreading.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * A DAO (Data Access Object) interface for managing the search history in the database.
 *
 * This interface provides methods to perform CRUD operations on the search history.
 * It uses Room persistence library annotations to map method calls to SQL queries.
 */
@Dao
interface SearchHistoryDAO {

    /**
     * Retrieves all search history records from the database.
     *
     * @return A list of all search history records.
     */
    @Query("SELECT * FROM SearchHistory")
    fun getAll(): List<SearchHistory>

    /**
     * Inserts one or more search history records into the database.
     *
     * @param marker The search history record(s) to be inserted.
     */
    @Insert
    fun insert(vararg marker: SearchHistory)

    /**
     * Deletes a specific search history record from the database.
     *
     * @param marker The search history record to be deleted.
     */
    @Delete
    fun delete(marker: SearchHistory)

    /**
     * Deletes all search history records from the database.
     */
    @Query("DELETE FROM SearchHistory")
    fun deleteAll()
}