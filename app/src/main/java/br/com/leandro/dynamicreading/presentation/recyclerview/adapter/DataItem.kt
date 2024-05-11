package br.com.leandro.dynamicreading.presentation.recyclerview.adapter

import br.com.leandro.dynamicreading.model.SearchHistory

/**
 * A sealed class that represents a data item in the RecyclerView.
 *
 * This class is used to represent two types of items in the RecyclerView: search history
 * items and headers.
 * Each type of item is represented by a subclass of DataItem: SearchHistoryItem and Header.
 *
 * @property id The unique identifier for the data item.
 */
sealed class DataItem {

    abstract val id: Long

    /**
     * A data class that represents a search history item in the RecyclerView.
     *
     * This class is used to store the search history text and a unique identifier for the item.
     *
     * @property history The search history item.
     * @property id The unique identifier for the search history item.
     */
    data class SearchHistoryItem(
        val history: SearchHistory,
        override val id: Long = Long.MIN_VALUE
    ) : DataItem()

    /**
     * A data class that represents a header in the RecyclerView.
     *
     * This class is used to store the header text and a unique identifier for the item.
     *
     * @property header The header text.
     * @property id The unique identifier for the header.
     */
    data class Header(
        val header: String,
        override val id: Long = Long.MIN_VALUE
    ) : DataItem()
}