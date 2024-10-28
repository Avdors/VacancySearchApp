package com.volkov.listvacancy.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.volkov.core.utils.SpacesItemDecoration
import com.volkov.core.utils.WordDeclension
import com.volkov.listvacancy.R
import com.volkov.listvacancy.databinding.FragmentListVacanciesBinding
import com.volkov.listvacancy.presentation.adapter.ListVacOfferAdapter
import com.volkov.listvacancy.presentation.adapter.ListVacancyAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListVacanciesFragment : Fragment() {
    private val jobsViewModel: ListVacancyViewModel by viewModel()
    private val wordDeclension = WordDeclension()
    private lateinit var offerAdapter: ListVacOfferAdapter
    private lateinit var vacancyAdapter: ListVacancyAdapter
    private var isFullListDisplayed = false
    private var binding: FragmentListVacanciesBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListVacanciesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация RecyclerView
        val offerRecyclerView = binding?.recyclerOffer
        offerRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val vacancyRecyclerView = binding?.recyclerVacancy
        vacancyRecyclerView?.layoutManager = LinearLayoutManager(context)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp8)
        vacancyRecyclerView?.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // Инициализация адаптера
        offerAdapter = ListVacOfferAdapter(emptyList()) { link ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
        offerRecyclerView?.adapter = offerAdapter

        // Создам адаптер для вакансий с логикой обработки кнопки "Еще вакансий"
        vacancyAdapter = ListVacancyAdapter(
            emptyList(),
            onVacancyClick = { vacancy -> },
            onFavoriteClick = { vacancy ->
                jobsViewModel.updateFavorites(vacancy = vacancy)
                vacancyAdapter.updateVacancies(
                    jobsViewModel.vacancies.value,
                    isFullListDisplayed,
                    jobsViewModel.vacancies.value.size
                )
            },
            // клик по кнопке избранное
            onShowMoreClick = {
                // Логика загрузки всех вакансий при нажатии на кнопку "Еще вакансий"
                isFullListDisplayed = true
                vacancyAdapter.updateVacancies(
                    jobsViewModel.vacancies.value,
                    true,
                    jobsViewModel.vacancies.value.size
                )
                // Скрываю верхний список предложений
                offerRecyclerView?.visibility = View.GONE
                binding?.quantityVacancy?.visibility = View.VISIBLE
                binding?.twAccordanceWith?.visibility = View.VISIBLE
                // Изменяю drawable в EditText на стрелку назад
                binding?.fieldSearchEt?.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.backarrow,
                    0,
                    0,
                    0
                )
                // Устанавливаю обработчик нажатия на EditText для возврата списка к первоначальному состоянию
                binding?.fieldSearchEt?.setOnClickListener {
                    if (isFullListDisplayed) {
                        resetVacancyList(binding?.recyclerOffer)
                    }

                }
            },
            onApplyClick = { vacancy -> }
        )
        vacancyRecyclerView?.adapter = vacancyAdapter

        // Наблюдаю за списком offers
        lifecycleScope.launch {
            jobsViewModel.offers.collect { offers ->
                offerAdapter.updateOffers(offers)  // Обновление данных в адаптере
            }
        }

        // Наблюдаю за списком vacancies
        lifecycleScope.launch {
            jobsViewModel.vacancies.collect { vacancies ->
                val totalvacancy = vacancies.size

                val vacancy =
                    wordDeclension.getVacancyCountString(totalvacancy.toInt(), requireContext())

                binding?.quantityVacancy?.text = "$vacancy"
                // Изначально показываю только первые 3 вакансии и кнопку "Еще вакансий"
                Log.d("ListVacanciesFragment", "Vacancies: ${vacancies.size}")
                if (vacancies.size > 3 && !isFullListDisplayed) {
                    vacancyAdapter.updateVacancies(vacancies.take(3), false, totalvacancy)
                } else {
                    vacancyAdapter.updateVacancies(vacancies, true, totalvacancy)
                }
                // Если хотите быть уверены, что обновления применились
                vacancyAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun resetVacancyList(offerRecyclerView: RecyclerView?) {
        isFullListDisplayed = false
        // Отображаю только первые 3 вакансии
        vacancyAdapter.updateVacancies(
            jobsViewModel.vacancies.value.take(3),
            false,
            jobsViewModel.vacancies.value.size
        )
        offerRecyclerView?.visibility = View.VISIBLE

        // Скрываю количество вакансий и текст "Соответствие"
        binding?.quantityVacancy?.visibility = View.GONE
        binding?.twAccordanceWith?.visibility = View.GONE
        // Возвращаю иконку поиска в EditText
        binding?.fieldSearchEt?.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.search_icon,
            0,
            0,
            0
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}