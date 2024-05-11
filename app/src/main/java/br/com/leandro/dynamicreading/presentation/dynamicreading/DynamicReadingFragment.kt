package br.com.leandro.dynamicreading.presentation.dynamicreading

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import br.com.leandro.dynamicreading.presentation.MainViewModel
import br.com.leandro.dynamicreading.model.SettingsPreferences
import br.com.leandro.dynamicreading.databinding.FragmentDynamicReadingBinding
import br.com.leandro.dynamicreading.extensions.isDarkTheme
import br.com.leandro.dynamicreading.extensions.vibrate

/**
 * A [Fragment] subclass responsible for displaying the highlighted text dynamically.
 *
 * This class is responsible for the following functionalities:
 * - Setting up the user interface for the dynamic reading screen.
 * - Handling user interactions such as touch events.
 * - Observing and displaying the highlighted word and text from the ViewModel.
 * - Applying gradient to the text based on the current highlighted word.
 *
 * The class uses a ViewModel [DynamicReadingViewModel] to observe and manage UI-related data
 * in a lifecycle conscious way.
 *
 * @property binding The binding object that gives access to all views in the fragment layout.
 * @property viewModel The ViewModel that is used to observe and manage UI-related data.
 * @property activityViewModel The ViewModel that is shared between fragments in the activity.
 */
class DynamicReadingFragment : Fragment() {

    private lateinit var binding: FragmentDynamicReadingBinding
    private val viewModel: DynamicReadingViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDynamicReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.highlightedScrollView.isScrollable = false

        activityViewModel.generatedText.observe(viewLifecycleOwner) { text ->
            val wordsPerMinute = SettingsPreferences(requireActivity()).getWordsPerMinutePreference()
            viewModel.startDynamicReading(text, wordsPerMinute)
        }

        viewModel.highlightedWord.observe(viewLifecycleOwner) { word ->
            binding.highlightedWord.text = word
        }

        viewModel.spannableText.observe(viewLifecycleOwner) { spannableText ->
            binding.highlightedText.text = spannableText
        }

        viewModel.index.observe(viewLifecycleOwner) { index ->
            applyGradientToText(index)
        }

        setupTouchListener()
    }

    /**
     * Defines a touch listener for the root view.
     *
     * When the user touches the screen (ACTION_DOWN), the dynamic reading process is paused
     * and a 100ms vibration is triggered.
     * When the user stops touching the screen (ACTION_UP), the dynamic reading process resumes
     * and a 50ms vibration is triggered.
     *
     * @return Always returns true, indicating that the touch event has been consumed.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        binding.root.setOnTouchListener { _, event ->
            /**
             * @param event The touch event that occurred.
             */
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewModel.isPaused.set(true)
                    requireContext().vibrate(100)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    viewModel.isPaused.set(false)
                    requireContext().vibrate(50)
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Applies a gradient to the text based on the current highlighted word's position.
     * The gradient is applied such that the highlighted word is at the center of the gradient.
     *
     * @param index The index of the current highlighted word in the text.
     */
    private fun applyGradientToText(index: Int) {
        binding.highlightedScrollView.post {
            val layout = binding.highlightedText.layout
            val line = layout.getLineForOffset(index)
            val offset = layout.getPrimaryHorizontal(index).toInt()

            // Calculate the vertical position of the line
            val y = layout.getLineTop(line)
            val scrollViewHeight = binding.highlightedScrollView.height

            // Calculate the desired scroll position to keep the word in the middle
            val scrollToY = y - scrollViewHeight / 2

            // Scroll to desired position
            binding.highlightedScrollView.scrollTo(offset, scrollToY)

            // Calculate the top and bottom position of the gradient
            val topGradient = layout.getLineTop((line - 4).coerceAtLeast(0))
            val bottomGradient = layout.getLineBottom((line + 4)
                .coerceAtMost(layout.lineCount - 1))

            // Define the color of the gradient
            val color = if (requireContext().isDarkTheme()) Color.WHITE else Color.BLACK

            // Create the gradient
            val shader = LinearGradient(
                0f, topGradient.toFloat(), 0f, bottomGradient.toFloat(),
                intArrayOf(Color.TRANSPARENT, color, Color.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )

            // Apply the gradient
            binding.highlightedText.paint.shader = shader
            binding.highlightedText.invalidate()
        }
    }
}