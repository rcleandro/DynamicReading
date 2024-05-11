package br.com.leandro.dynamicreading.presentation.dynamicreading

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

/**
 * A custom [NestedScrollView] subclass that allows disabling of scrolling.
 *
 * This class is used in scenarios where the scroll functionality of a NestedScrollView
 * needs to be controlled programmatically. It provides a public property [isScrollable]
 * that can be used to enable or disable scrolling.
 *
 * @property isScrollable A Boolean property that controls whether the ScrollView is scrollable.
 */
class NonScrollableScrollView : NestedScrollView {

    var isScrollable = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    /**
     * Handles the touch event.
     *
     * This method overrides the onTouchEvent method of the ScrollView. It checks the [isScrollable]
     * property before passing the touch event to the superclass. If [isScrollable] is false, it
     * consumes the touch event and prevents scrolling.
     *
     * @param ev The motion event being processed.
     * @return Returns true if the event was handled, false otherwise.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isScrollable && super.onTouchEvent(ev)
    }

    /**
     * Determines whether the touch event should be intercepted.
     *
     * This method overrides the onInterceptTouchEvent method of the ScrollView. It checks
     * the [isScrollable]
     * property before deciding whether to intercept the touch event. If [isScrollable] is false, it
     * does not intercept the touch event, allowing child views to receive it.
     *
     * @param ev The motion event being processed.
     * @return Returns true if the event should be intercepted, false otherwise.
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isScrollable && super.onInterceptTouchEvent(ev)
    }
}