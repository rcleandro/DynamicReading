package br.com.leandro.dynamicreading.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.leandro.dynamicreading.R
import br.com.leandro.dynamicreading.model.SettingsPreferences
import br.com.leandro.dynamicreading.databinding.ActivityMainBinding

/**
 * MainActivity is an [AppCompatActivity] subclass that serves as the entry point of the
 * application.
 *
 * This class is responsible for the following functionalities:
 * - Setting up the user interface for the main screen.
 * - Initializing the [MainViewModel] and observing LiveData from it.
 * - Handling navigation within the application using a [NavController].
 * - Setting up the action bar with the NavController using the setupActionBarWithNavController
 * method.
 * - Controlling the visibility of a progress bar based on the progressBarVisibility LiveData
 * from the ViewModel.
 *
 * The [MainViewModel] is initialized and its methods are called to set the initial values for
 * words per minute and number of words from the saved preferences.
 *
 * @property appBarConfiguration The configuration object for the app bar.
 * @property binding The binding object that gives access to all views in the activity layout.
 * @property viewModel The ViewModel that is used to observe and manage UI-related data.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        viewModel.setWordsPerMinute(SettingsPreferences(this).getWordsPerMinutePreference())
        viewModel.setNumberOfWords(SettingsPreferences(this).getNumberOfWordsPreference())
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}