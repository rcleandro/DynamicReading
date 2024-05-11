package br.com.leandro.dynamicreading.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Hides the virtual keyboard.
 *
 * This extension method can be called on any instance of [View].
 * It gets the [InputMethodManager] from the View's context and uses it to hide the keyboard.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}