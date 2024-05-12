package br.com.leandro.dynamicreading.presentation.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.leandro.dynamicreading.BuildConfig
import br.com.leandro.dynamicreading.model.SearchHistory
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import kotlinx.coroutines.launch

/**
 * A [ViewModel] subclass that manages and stores UI-related data for the StartFragment.
 *
 * This class is responsible for the following functionalities:
 * - Storing and updating the search history.
 *
 * The class uses a MutableLiveData object to hold the search history and exposes it as a LiveData
 * object for other classes to observe.
 *
 * @property _progressBarVisibility The LiveData object that holds the visibility state of the
 * progress bar.
 * @property progressBarVisibility The public LiveData object that other classes can observe.
 * @property _searchHistory The MutableLiveData object that holds the search history.
 * @property searchHistory The public LiveData object that other classes can observe.
 * @property _responseText The MutableLiveData object that holds the generated text.
 * @property responseText The public LiveData object that other classes can observe.
 * @property _error The MutableLiveData object that holds the error message.
 * @property error The public LiveData object that other classes can observe.
 * @property generativeModel The GenerativeModel object used to generate text.
 */
class StartViewModel : ViewModel() {

    init {
        startGeminiApi()
    }

    private val _progressBarVisibility = MutableLiveData<Boolean>().apply { value = false }
    val progressBarVisibility: LiveData<Boolean>
        get() = _progressBarVisibility

    private val _searchHistory = MutableLiveData<List<SearchHistory>>()
    val searchHistory: LiveData<List<SearchHistory>>
        get() = _searchHistory

    private val _responseText = MutableLiveData<String?>()
    val responseText: LiveData<String?>
        get() = _responseText

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private lateinit var generativeModel: GenerativeModel

    /**
     * Updates the search history.
     *
     * @param value The new search history.
     */
    fun setSearchHistory(value: List<SearchHistory>) {
        _searchHistory.postValue(value)
    }

    fun clearResponse() {
        _responseText.postValue(null)
        _error.postValue(null)
    }

    /**
     * Initializes the generative model from the Gemini API using [SafetySetting] objects to
     * define the safety settings.
     */
    private fun startGeminiApi() {
        generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.GEMINI_API_TOKEN,
            safetySettings = listOf(
                SafetySetting(
                    harmCategory = HarmCategory.HATE_SPEECH,
                    threshold = BlockThreshold.LOW_AND_ABOVE
                ),
                SafetySetting(
                    harmCategory = HarmCategory.HARASSMENT,
                    threshold = BlockThreshold.LOW_AND_ABOVE
                ),
            )
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
    fun getGenerativeText(
        synopsis: String,
        textSize: Int,
        language: String
    ) {
        _progressBarVisibility.postValue(true)

        val prompt =
            "Write a story based on the synopsis ```$synopsis``` with $textSize words in the language $language."

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(prompt)

                _progressBarVisibility.postValue(false)
                _responseText.postValue(response.text)

                Log.d("MainViewModel", "GenerateContentResponse: ${response.text}")
            } catch (e: Exception) {
                _progressBarVisibility.postValue(false)
                _error.postValue(e.message)

                Log.d("MainViewModel", "Exception: $e")
            }
        }
    }
}