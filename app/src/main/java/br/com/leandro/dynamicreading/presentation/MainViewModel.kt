package br.com.leandro.dynamicreading.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.leandro.dynamicreading.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

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
 * The class uses a GenerativeModel [GenerativeModel] to generate text based on a given prompt.
 *
 * @property _progressBarVisibility The LiveData object that holds the visibility state of the
 * progress bar.
 * @property progressBarVisibility The public LiveData object that other classes can observe.
 * @property _wordsPerMinute The LiveData object that holds the words per minute setting.
 * @property wordsPerMinute The public LiveData object that other classes can observe.
 * @property _numberOfWords The LiveData object that holds the number of words setting.
 * @property numberOfWords The public LiveData object that other classes can observe.
 * @property _generatedText The LiveData object that holds the generated text.
 * @property generatedText The public LiveData object that other classes can observe.
 * @property generativeModel The GenerativeModel object used to generate text.
 */
class MainViewModel : ViewModel() {

    private val _progressBarVisibility = MutableLiveData<Boolean>().apply { value = false }
    val progressBarVisibility: LiveData<Boolean>
        get() = _progressBarVisibility

    private val _wordsPerMinute = MutableLiveData<Int>()
    val wordsPerMinute: LiveData<Int>
        get() = _wordsPerMinute

    private val _numberOfWords = MutableLiveData<Int>()
    val numberOfWords: LiveData<Int>
        get() = _numberOfWords

    private val _generatedText = MutableLiveData<String>()
    val generatedText: LiveData<String>
        get() = _generatedText

    private lateinit var generativeModel: GenerativeModel

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

    /**
     * Initializes the generative model from the Gemini API.
     */
    fun startGeminiApi() {
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.GEMINI_API_TOKEN
        )
    }

    /**
     * Generates a text from a prompt using the generative model from the Gemini API. During the
     * generation, the visibility of the progress bar is changed.
     *
     * @param synopsis The prompt for the generative model.
     * @param textSize The desired size of the generated text.
     * @param language The language in which the text should be generated.
     * @return The generated text.
     */
    suspend fun getGenerativeText(synopsis: String, textSize: Int, language: String): String? {
        _progressBarVisibility.postValue(true)
        val prompt =
            "Write a story based on the synopsis ```$synopsis``` with $textSize words in the language $language."
        val response = generativeModel.generateContent(prompt)

        Log.d("MainViewModel", "Generated text: ${response.text}")

        _progressBarVisibility.postValue(false)
        return response.text
    }
}