package br.com.leandro.dynamicreading.presentation.recyclerview.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * A custom LinearLayoutManager that disables scrolling in a RecyclerView.
 *
 * This class extends LinearLayoutManager and overrides the canScrollVertically() and
 * canScrollHorizontally() methods to disable vertical and horizontal scrolling respectively.
 *
 * @param context The context in which this LinearLayoutManager is being used.
 */
class NonScrollableLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    /**
     * Determines whether the RecyclerView can be scrolled vertically.
     *
     * This method is overridden to return false, disabling vertical scrolling.
     *
     * @return false, indicating that vertical scrolling is not allowed.
     */
    override fun canScrollVertically(): Boolean = false

    /**
     * Determines whether the RecyclerView can be scrolled horizontally.
     *
     * This method is overridden to return false, disabling horizontal scrolling.
     *
     * @return false, indicating that horizontal scrolling is not allowed.
     */
    override fun canScrollHorizontally(): Boolean = false
}