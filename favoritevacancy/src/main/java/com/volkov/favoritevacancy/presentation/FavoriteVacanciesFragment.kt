package com.volkov.favoritevacancy.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.volkov.core.utils.SpacesItemDecoration
import com.volkov.core.utils.WordDeclension
import com.volkov.favoritevacancy.R
import com.volkov.favoritevacancy.databinding.FragmentFavoriteVacanciesBinding
import com.volkov.favoritevacancy.presentation.adapter.FavoriteVacancyAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteVacanciesFragment : Fragment() {
    private val vacancyViewModel: FavoriteVacancyViewModel by viewModel()
    private var binding: FragmentFavoriteVacanciesBinding? = null
    private lateinit var vacancyAdapter: FavoriteVacancyAdapter
    private val wordDeclension = WordDeclension()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteVacanciesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация RecyclerView
        val vacancyRecyclerView = binding?.favoritesRecyclerVacancy
        vacancyRecyclerView?.layoutManager = LinearLayoutManager(context)

        // расстояние между карточками через ItemDecoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp8)
        vacancyRecyclerView?.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // Инициализация адаптера
        vacancyAdapter = FavoriteVacancyAdapter(
            emptyList(),
            onVacancyClick = { vacancy ->
                // Переход к CardVacancyFragment

            },
            // клик по кнопке избранное
            onFavoriteClick = { vacancy ->
                // jobsViewModel.toggleFavorite(vacancy)
                vacancyViewModel.updateFavorites(vacancy = vacancy)
                vacancyAdapter.updateVacancies(
                    vacancyViewModel.vacancies.value,
                    vacancyViewModel.vacancies.value.size
                )
            },

            onApplyClick = { vacancy ->

            }
        )
        vacancyRecyclerView?.adapter = vacancyAdapter

        // Наблюдаю за списком vacancies
        lifecycleScope.launch {
            vacancyViewModel.vacancies.collect { vacancies ->

                val vacancy = wordDeclension.getVacancyCountString(vacancies.size, requireContext())

                binding?.quantityVacancy?.text = "$vacancy"

                vacancyAdapter.updateVacancies(vacancies, vacancies.size)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}