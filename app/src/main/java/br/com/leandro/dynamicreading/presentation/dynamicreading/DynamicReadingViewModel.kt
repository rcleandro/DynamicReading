package br.com.leandro.dynamicreading.presentation.dynamicreading

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A [ViewModel] subclass responsible for managing the dynamic reading process.
 *
 * This class is responsible for the following functionalities:
 * - Starting the dynamic reading process with the given text and words per minute.
 * - Creating a spannable string with the current highlighted word in bold.
 * - Pausing and resuming the dynamic reading process.
 *
 * The class uses a CoroutineScope [viewModelScope] to launch and manage coroutines in a
 * lifecycle conscious way.
 *
 * @property job The job that is used to manage the dynamic reading process.
 * @property _spannableText The mutable live data that holds the spannable string.
 * @property spannableText The live data that exposes the spannable string to observers.
 * @property _highlightedWord The mutable live data that holds the current highlighted word.
 * @property highlightedWord The live data that exposes the current highlighted word to observers.
 * @property _index The mutable live data that holds the index of the current highlighted word
 * in the text.
 * @property index The live data that exposes the index of the current highlighted word
 * to observers.
 * @property delayInterval The delay interval between each word in milliseconds.
 * @property isPaused The atomic boolean that indicates whether the dynamic reading process
 * is paused.
 */
class DynamicReadingViewModel : ViewModel() {

    private var job: Job? = null

    private val _spannableText = MutableLiveData<SpannableStringBuilder>()
    val spannableText: LiveData<SpannableStringBuilder>
        get() = _spannableText

    private val _highlightedWord = MutableLiveData<String>()
    val highlightedWord: LiveData<String>
        get() = _highlightedWord

    private val _index = MutableLiveData<Int>()
    val index: LiveData<Int>
        get() = _index

    private var delayInterval = 300L
    val isPaused = AtomicBoolean(false)

    /**
     * Starts the dynamic reading process with the given text and words per minute.
     *
     * @param text The text to be read dynamically.
     * @param wordsPerMinute The number of words to be read per minute.
     */
    fun startDynamicReading(text: String, wordsPerMinute: Int) {
        job?.cancel()

        val words = text.split("\\s+".toRegex())
        delayInterval = (60_000 / wordsPerMinute).toLong()
        var index = 0

        job = viewModelScope.launch {
            while (index < words.size) {
                while (isPaused.get()) {
                    delay(100) // Pause dynamic reading
                }

                _highlightedWord.postValue(words[index])

                createSpannableString(words, index).also { spannable ->
                    _spannableText.postValue(spannable)
                }

                delay(delayInterval)
                index++
            }
        }
    }

    /**
     * Creates a spannable string with the current highlighted word in bold.
     *
     * @param words The list of words in the text.
     * @param index The index of the current highlighted word in the list.
     * @return The created spannable string.
     */
    private fun createSpannableString(words: List<String>, index: Int): SpannableStringBuilder {
        val spannable = SpannableStringBuilder()
        addMarginText(spannable)

        words.forEachIndexed { i, word ->
            spannable.append(word)
            if (i == index) {
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    spannable.length - word.length, spannable.length,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                _index.postValue(spannable.length)
            }
            spannable.append(" ")
        }

        addMarginText(spannable)

        return spannable
    }

    /**
     * Adds margin text to the given spannable string.
     *
     * @param spannable The spannable string to add margin text to.
     * @return The spannable string with added margin text.
     */
    private fun addMarginText(spannable: SpannableStringBuilder): SpannableStringBuilder {
        for (i in 1..20) { spannable.append("\n") }
        return spannable
    }

    /**
     * Cancels all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}