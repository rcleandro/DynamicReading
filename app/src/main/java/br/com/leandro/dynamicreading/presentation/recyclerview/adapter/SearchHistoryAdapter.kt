package br.com.leandro.dynamicreading.presentation.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.leandro.dynamicreading.model.SearchHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A [ListAdapter] subclass responsible for managing and displaying search history data in
 * a RecyclerView.
 *
 * This class is responsible for the following functionalities:
 * - Creating ViewHolder instances for each item in the RecyclerView.
 * - Binding data to the ViewHolder instances.
 * - Handling item clicks in the RecyclerView.
 * - Submitting new data to the RecyclerView.
 *
 * The class uses a DiffUtil.ItemCallback [SearchHistoryDiffCallback] to calculate the difference
 * between two lists.
 * It also uses a CoroutineScope to perform list transformations off the main thread.
 *
 * @property onItemClickListener A lambda function that is invoked when an item in the RecyclerView
 * is clicked.
 * @property adapterScope The CoroutineScope that is used to perform list transformations off the
 * main thread.
 */
class SearchHistoryAdapter : ListAdapter<DataItem, DataItemViewHolder>(SearchHistoryDiffCallback()) {

    lateinit var onItemClickListener: (history: SearchHistory) -> Unit
    lateinit var onItemLongClickListener: (history: SearchHistory) -> Unit
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    companion object {
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> DataItemViewHolder.HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> DataItemViewHolder.SearchHistoryViewHolder.from(parent)
            else -> throw ClassCastException("ViewType unknown $viewType")
        }
    }

    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        when (holder) {
            is DataItemViewHolder.SearchHistoryViewHolder -> {
                val item = getItem(position) as DataItem.SearchHistoryItem
                holder.bind(item.history, onItemClickListener, onItemLongClickListener)
            }

            is DataItemViewHolder.HeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SearchHistoryItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    /**
     * This function is responsible for adding headers and submitting the list to the RecyclerView.
     * It uses a CoroutineScope to perform the list transformation off the main thread.
     *
     * @param list The list of search history items to be displayed.
     */
    fun addHeadersAndSubmitList(list: List<SearchHistory>?) {
        adapterScope.launch {
            val listDataItem = list?.toListOfDataItem() ?: emptyList()
            withContext(Dispatchers.Main) {
                submitList(listDataItem)
            }
        }
    }

    class SearchHistoryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * This function is responsible for converting a list of SearchHistory objects into a list
     * of DataItem objects.
     * It groups the SearchHistory objects by date and adds a header for each group.
     *
     * @return A list of DataItem objects that represents the grouped search history.
     */
    private fun List<SearchHistory>.toListOfDataItem(): List<DataItem> {

        val grouping = this.groupBy { history ->
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(history.date.time)
        }

        val listDataItem = mutableListOf<DataItem>()
        grouping.forEach { mapEntry ->
            listDataItem.add(DataItem.Header(mapEntry.key))
            listDataItem.addAll(
                mapEntry.value.map { history ->
                    DataItem.SearchHistoryItem(history)
                }
            )
        }

        return listDataItem
    }
}