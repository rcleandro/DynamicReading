package br.com.leandro.dynamicreading.presentation.recyclerview.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.leandro.dynamicreading.R
import br.com.leandro.dynamicreading.model.SearchHistory
import br.com.leandro.dynamicreading.databinding.ItemHeaderBinding
import br.com.leandro.dynamicreading.databinding.ItemSearchHistoryBinding

/**
 * A sealed class that represents a ViewHolder for a RecyclerView item.
 * This class is responsible for binding data to the views in the RecyclerView item layout.
 */
sealed class DataItemViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     * A ViewHolder subclass for displaying search history items in the RecyclerView.
     *
     * @property binding The binding object that gives access to all views in the item layout.
     */
    class SearchHistoryViewHolder(private val binding: ItemSearchHistoryBinding) :
        DataItemViewHolder(binding) {

        /**
         * This function binds the search history data to the views in the item layout.
         *
         * @param history The search history data to be displayed.
         * @param onItemClickListener A lambda function that is invoked when the item is clicked.
         * @param onItemLongClickListener A lambda function that is invoked when the item is long
         * clicked.
         */
        fun bind(
            history: SearchHistory,
            onItemClickListener: ((SearchHistory) -> Unit),
            onItemLongClickListener: ((SearchHistory) -> Unit)
        ) {
            with(binding) {
                val context = binding.root.context
                val spannableSynopsis = SpannableStringBuilder(
                    "${getString(context, R.string.synopses)} ${history.synopsis}"
                )
                spannableSynopsis.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    getString(context, R.string.synopses).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textViewSynopsis.text = spannableSynopsis

                val spannableText = SpannableStringBuilder(
                    "${getString(context, R.string.text)} ${history.text}"
                )
                spannableText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    getString(context, R.string.text).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textViewText.text = spannableText


                val spannableTextSize = SpannableStringBuilder(
                    "${getString(context, R.string.text_size)} ${history.numberOfWords} ${
                        getString(context, R.string.words)}"
                )
                spannableText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    getString(context, R.string.text).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textViewSize.text = spannableTextSize

                root.setOnClickListener { onItemClickListener.invoke(history) }
                root.setOnLongClickListener {
                    onItemLongClickListener.invoke(history)
                    true
                }
            }
        }

        companion object {
            /**
             * This function inflates the item layout and returns a ViewHolder instance.
             *
             * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
             * @return A ViewHolder instance.
             */
            fun from(parent: ViewGroup): SearchHistoryViewHolder {
                val binding: ItemSearchHistoryBinding = ItemSearchHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SearchHistoryViewHolder(binding)
            }
        }
    }

    /**
     * A ViewHolder subclass for displaying headers in the RecyclerView.
     *
     * @property binding The binding object that gives access to all views in the item layout.
     */
    class HeaderViewHolder(private val binding: ItemHeaderBinding) : DataItemViewHolder(binding) {

        /**
         * This function binds the header data to the views in the item layout.
         *
         * @param item The header data to be displayed.
         */
        fun bind(item: DataItem.Header) {
            with(binding) {
                date.text = item.header
            }
        }

        companion object {
            /**
             * This function inflates the item layout and returns a ViewHolder instance.
             *
             * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
             * @return A ViewHolder instance.
             */
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding: ItemHeaderBinding = ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderViewHolder(binding)
            }
        }
    }
}