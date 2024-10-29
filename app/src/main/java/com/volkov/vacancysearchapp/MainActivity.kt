package com.volkov.vacancysearchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.volkov.listvacancy.presentation.ListVacancyViewModel
import com.volkov.vacancysearchapp.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val listVacancyViewModel: ListVacancyViewModel by viewModel()
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
            when (item.itemId) {
                R.id.listVacanciesFragment -> navController.navigate(R.id.listVacanciesFragment)
                R.id.favoriteVacanciesFragment -> navController.navigate(R.id.favoriteVacanciesFragment)
                else -> navController.navigate(R.id.inProgressFragment)
            }
            true
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}