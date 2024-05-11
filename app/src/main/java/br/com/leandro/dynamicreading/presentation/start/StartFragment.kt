package br.com.leandro.dynamicreading.presentation.start

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.leandro.dynamicreading.presentation.MainViewModel
import br.com.leandro.dynamicreading.R
import br.com.leandro.dynamicreading.model.SearchHistory
import br.com.leandro.dynamicreading.model.SearchHistoryDatabase
import br.com.leandro.dynamicreading.model.SettingsPreferences
import br.com.leandro.dynamicreading.databinding.FragmentStartBinding
import br.com.leandro.dynamicreading.presentation.recyclerview.adapter.NonScrollableLinearLayoutManager
import br.com.leandro.dynamicreading.presentation.recyclerview.adapter.SearchHistoryAdapter
import br.com.leandro.dynamicreading.extensions.hideKeyboard
import br.com.leandro.dynamicreading.extensions.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.GregorianCalendar

/**
 * A [Fragment] subclass that represents the start screen of the application.
 *
 * This class is responsible for the following functionalities:
 * - Setting up the user interface for the start screen.
 * - Initializing the [StartViewModel] and observing LiveData from it.
 * - Handling user interactions with the UI elements.
 * - Managing the RecyclerView that displays the search history.
 * - Generating text based on the user's input and starting the dynamic reading.
 *
 * @property binding The binding object that gives access to all views in the fragment layout.
 * @property viewModel The ViewModel that is used to observe and manage UI-related data.
 * @property activityViewModel The ViewModel that is shared between multiple fragments.
 * @property adapter The adapter for the RecyclerView that displays the search history.
 * @property layoutManager The layout manager for the RecyclerView.
 * @property db The database that stores the search history.
 */
class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private val viewModel: StartViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: SearchHistoryAdapter
    private lateinit var layoutManager: NonScrollableLinearLayoutManager
    private lateinit var db: SearchHistoryDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sliderInterval.addOnChangeListener { _, value, _ ->
            onSliderIntervalChanged(value)
        }

        binding.sliderNumberOfWords.addOnChangeListener { _, value, _ ->
            onSliderNumberOfWordsChanged(value)
        }

        binding.startButton.setOnClickListener { onStartButtonClicked() }
        binding.deleteAllButton.setOnClickListener { onDeleteAllHistoryButtonClicked() }

        activityViewModel.wordsPerMinute.observe(viewLifecycleOwner) { wordsPerMinute ->
            binding.textViewInterval.text =
                getString(R.string.words_per_minute, wordsPerMinute.toString())
            binding.sliderInterval.value = wordsPerMinute.toFloat()
        }

        activityViewModel.numberOfWords.observe(viewLifecycleOwner) { numberOfWords ->
            binding.textViewNumberOfWords.text =
                getString(R.string.number_of_words, numberOfWords.toString())
            binding.sliderNumberOfWords.value = numberOfWords.toFloat()
        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            setAdapter(history)
        }
    }

    override fun onResume() {
        super.onResume()

        loadHistoryDatabase()
    }

    /**
     * Sets up the adapter for the RecyclerView that displays the search history.
     *
     * @param searchHistory The search history to display in the RecyclerView.
     */
    private fun setAdapter(searchHistory: List<SearchHistory>?) {
        if (searchHistory.isNullOrEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.textViewHistory.visibility = View.GONE
            binding.deleteAllButton.visibility = View.GONE
            return
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textViewHistory.visibility = View.VISIBLE
            binding.deleteAllButton.visibility = View.VISIBLE
        }

        adapter = SearchHistoryAdapter()
        adapter.onItemClickListener = { history -> startDynamicReading(history) }
        adapter.onItemLongClickListener = { history ->
            onDeleteHistoryButtonClicked(history)
        }
        layoutManager = NonScrollableLinearLayoutManager(Activity())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        adapter.addHeadersAndSubmitList(searchHistory.reversed())
    }

    /**
     * Loads the search history from the database.
     */
    private fun loadHistoryDatabase() {
        db = SearchHistoryDatabase.build(requireContext())
        lifecycleScope.launch(Dispatchers.IO) {
            db.searchHistoryDAO().getAll().let {
                viewModel.setSearchHistory(it)
            }
        }
    }

    /**
     * Generates a text based on the user's input and starts the dynamic reading.
     *
     * @param synopsis The user's input to generate the text from.
     */
    private fun generateText(synopsis: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val numbersOfWords = binding.sliderNumberOfWords.value.toInt()
            val language = getString(R.string.language)
            val text = activityViewModel.getGenerativeText(synopsis, numbersOfWords, language)

            text?.let {
                val history = SearchHistory(
                    date = GregorianCalendar.getInstance().time,
                    synopsis = synopsis,
                    text = it,
                    numberOfWords = numbersOfWords,
                    language = language
                )

                db.searchHistoryDAO().insert(history)

                withContext(Dispatchers.Main) {
                    startDynamicReading(history)
                }
            } ?: requireContext().toast(getString(R.string.error_generating_text))
        }
    }

    /**
     * Called when the value of the slider for the words per minute setting changes.
     *
     * @param value The new value of the slider.
     */
    private fun onSliderIntervalChanged(value: Float) {
        activityViewModel.setWordsPerMinute(value.toInt())
        SettingsPreferences(requireContext()).setWordsPerMinutePreference(value.toInt())
    }

    /**
     * Called when the value of the slider for the number of words setting changes.
     *
     * @param value The new value of the slider.
     */
    private fun onSliderNumberOfWordsChanged(value: Float) {
        activityViewModel.setNumberOfWords(value.toInt())
        SettingsPreferences(requireContext()).setNumberOfWordsPreference(value.toInt())
    }

    /**
     * Called when the start button is clicked. Generates a text based on the user's input and
     * starts the dynamic reading.
     */
    private fun onStartButtonClicked() {
        val synopsis = binding.inputText.text.toString()
        if (synopsis.isEmpty()) {
            requireContext().toast(getString(R.string.error_empty_text))
            return
        }

        view?.hideKeyboard()

        generateText(synopsis)
    }

    /**
     * Called when the delete history button is clicked. Deletes the given search history from
     * the database.
     */
    private fun onDeleteHistoryButtonClicked(history: SearchHistory) {
        val alert = Dialog(requireContext(), R.style.dialogTheme)
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.setContentView(R.layout.question_dialog)
        alert.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        val question: TextView = alert.findViewById(R.id.question)
        val buttonYes: Button = alert.findViewById(R.id.buttonYes)
        val buttonNo: Button = alert.findViewById(R.id.buttonNo)

        question.text = getString(R.string.question_delete)

        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alert.show()

        buttonYes.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.searchHistoryDAO().delete(history)
                loadHistoryDatabase()
            }
        }
        buttonNo.setOnClickListener { alert.dismiss() }
    }

    /**
     * Called when the delete all history button is clicked. Deletes all search history from
     * the database.
     */
    private fun onDeleteAllHistoryButtonClicked() {
        val alert = Dialog(requireContext(), R.style.dialogTheme)
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.setContentView(R.layout.question_dialog)
        alert.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val buttonYes: Button = alert.findViewById(R.id.buttonYes)
        val buttonNo: Button = alert.findViewById(R.id.buttonNo)

        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        alert.show()

        buttonYes.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.searchHistoryDAO().deleteAll()
                loadHistoryDatabase()
            }
            alert.dismiss()
        }
        buttonNo.setOnClickListener { alert.dismiss() }
    }

    /**
     * Starts the dynamic reading with the given search history.
     *
     * @param history The search history to start the dynamic reading with.
     */
    private fun startDynamicReading(history: SearchHistory) {
        activityViewModel.setGeneratedText(history.text)
        val action = StartFragmentDirections
            .actionStartFragmentToDynamicReadingFragment()
        findNavController().navigate(action)
    }
}