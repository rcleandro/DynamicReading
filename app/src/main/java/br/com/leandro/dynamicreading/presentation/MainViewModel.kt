package br.com.leandro.dynamicreading.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A [ViewModel] subclass responsible for managing and storing UI-related data in a lifecycle
 * conscious way.
 *
 * This class is responsible for the following functionalities:
 * - Managing the visibility of the progress bar.
 * - Storing and updating the words per minute setting.
 * - Storing and updating the number of words setting.
 * - Storing and updating the generated text.
 * - Initializing the generative model from the Gemini API.
 * - Generating a text from a prompt using the generative model.
 *
 * @property _wordsPerMinute The LiveData object that holds the words per minute setting.
 * @property wordsPerMinute The public LiveData object that other classes can observe.
 * @property _numberOfWords The LiveData object that holds the number of words setting.
 * @property numberOfWords The public LiveData object that other classes can observe.
 * @property _generatedText The LiveData object that holds the generated text.
 * @property generatedText The public LiveData object that other classes can observe.
 */
class MainViewModel : ViewModel() {

    private val _wordsPerMinute = MutableLiveData<Int>()
    val wordsPerMinute: LiveData<Int>
        get() = _wordsPerMinute

    private val _numberOfWords = MutableLiveData<Int>()
    val numberOfWords: LiveData<Int>
        get() = _numberOfWords

    private val _generatedText = MutableLiveData<String>()
    val generatedText: LiveData<String>
        get() = _generatedText

    /**
     * This function updates the words per minute setting.
     *
     * @param value The new words per minute setting.
     */
    fun setWordsPerMinute(value: Int) {
        _wordsPerMinute.postValue(value)
    }

    /**
     * This function updates the number of words setting.
     *
     * @param value The new number of words setting.
     */
    fun setNumberOfWords(value: Int) {
        _numberOfWords.postValue(value)
    }

    /**
     * This function updates the generated text.
     *
     * @param value The new generated text.
     */
    fun setGeneratedText(value: String) {
        _generatedText.postValue(value)
    }
}