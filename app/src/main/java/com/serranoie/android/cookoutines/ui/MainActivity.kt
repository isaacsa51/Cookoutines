package com.serranoie.android.cookoutines.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.serranoie.android.cookoutines.R
import com.serranoie.android.cookoutines.databinding.ActivityMainBinding
import com.serranoie.android.feature.onboarding.domain.GetOnboardingStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var getOnboardingStatusUseCase: GetOnboardingStatusUseCase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            getOnboardingStatusUseCase().collect { onboardingCompleted ->
                setStartDestination(onboardingCompleted)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            windowInsets
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onboardingFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.instructionsRecipeFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeMenu -> {
                    navigateToFragment(R.id.recipesListFragment)
                    true
                }
                R.id.searchMenu -> {
                    navigateToFragment(R.id.searchFragment)
                    true
                }
                R.id.savedMenu -> {
                    navigateToFragment(R.id.savedRecipesFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setStartDestination(onboardingCompleted: Boolean) {
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_graph)

        val startDest = if (onboardingCompleted) {
            R.id.nav_graph_main
        } else {
            R.id.onboardingFragment
        }

        navGraph.setStartDestination(startDest)

        navController.graph = navGraph
    }

    private fun navigateToFragment(destinationId: Int) {
        findNavController(R.id.nav_host_fragment).navigate(destinationId)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}