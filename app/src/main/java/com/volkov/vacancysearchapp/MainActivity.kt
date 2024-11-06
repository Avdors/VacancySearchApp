package com.volkov.vacancysearchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.volkov.core.utils.SessionManager
import com.volkov.favoritevacancy.presentation.FavoriteVacancyViewModel
import com.volkov.vacancysearchapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val favoriteVacancyViewModel: FavoriteVacancyViewModel by viewModel()
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Устанавливаем Toolbar как ActionBar
        setSupportActionBar(binding?.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        binding?.bottomNav?.setupWithNavController(navController)

        // Подписываемся на обработку кликов по элементам меню
        binding?.bottomNav?.setOnItemSelectedListener { item ->
            if (isLoggedIn()) {
                when (item.itemId) {
                    R.id.listVacanciesFragment -> navController.navigate(R.id.listVacanciesFragment)
                    R.id.favoriteVacanciesFragment -> navController.navigate(R.id.favoriteVacanciesFragment)
                    else -> navController.navigate(R.id.inProgressFragment)
                }
            }
            true
        }



        lifecycleScope.launch(Dispatchers.IO) {
            favoriteVacancyViewModel.vacancies.collect { favorites ->
                updateFavoritesBadge(favorites.size)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun updateFavoritesBadge(count: Int) {
        val badge = binding?.bottomNav?.getOrCreateBadge(R.id.favoriteVacanciesFragment)
        if (count > 0) {
            badge?.isVisible = true
            badge?.number = count
            badge?.backgroundColor =
                resources.getColor(R.color.red, null) // Устанавливаем красный фон
        } else {
            badge?.isVisible = false
        }
    }
    private fun isLoggedIn(): Boolean {
        // Логика проверки авторизации пользователя (например, из SharedPreferences)
        return SessionManager.isLoggedIn
    }
}