package br.com.leandro.dynamicreading.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * A [data class] that represents a search history entry in the application.
 *
 * This class is responsible for storing the following information:
 * - The unique identifier of the search history entry (id).
 * - The date when the search history entry was created (date).
 * - The synopsis used to generate the text (synopsis).
 * - The generated text (text).
 *
 * The class is annotated with [@Entity] to indicate that it represents a table in the Room database.
 * The [id] property is annotated with [@PrimaryKey] and is set to auto-generate, meaning that Room
 * will automatically generate a unique id for each new search history entry.
 *
 * @property id The unique identifier of the search history entry.
 * @property date The date when the search history entry was created.
 * @property synopsis The synopsis used to generate the text.
 * @property text The generated text.
 * @property numberOfWords The number of words in the generated text.
 * @property language The language of the generated text.
 */
@Entity
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0L,
    val date: Date,
    val synopsis: String,
    val text: String,
    val numberOfWords: Int,
    val language: String
)