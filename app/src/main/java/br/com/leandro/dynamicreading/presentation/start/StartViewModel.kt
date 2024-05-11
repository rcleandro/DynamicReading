package br.com.leandro.dynamicreading.presentation.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.leandro.dynamicreading.model.SearchHistory

/**
 * A [ViewModel] subclass that manages and stores UI-related data for the StartFragment.
 *
 * This class is responsible for the following functionalities:
 * - Storing and updating the search history.
 *
 * The class uses a MutableLiveData object to hold the search history and exposes it as a LiveData
 * object for other classes to observe.
 *
 * @property _searchHistory The MutableLiveData object that holds the search history.
 * @property searchHistory The public LiveData object that other classes can observe.
 */
class StartViewModel : ViewModel() {

    private val _searchHistory = MutableLiveData<List<SearchHistory>>()
    val searchHistory: LiveData<List<SearchHistory>>
        get() = _searchHistory

    /**
     * Updates the search history.
     *
     * @param value The new search history.
     */
    fun setSearchHistory(value: List<SearchHistory>) {
        _searchHistory.postValue(value)
    }
}