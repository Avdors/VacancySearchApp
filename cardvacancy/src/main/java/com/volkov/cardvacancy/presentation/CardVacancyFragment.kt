package com.volkov.cardvacancy.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volkov.cardvacancy.databinding.FragmentCardVacancyBinding
import com.volkov.jobresponse.presentation.ResponsDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardVacancyFragment : Fragment() {
    private var isFavoriteVacancy: Boolean = false
    private val сardVacancyViewModel: CardVacancyViewModel by viewModel()
    private var binding: FragmentCardVacancyBinding? = null
    private var vacancyId: String? = ""
    private lateinit var uiHandler: CardVacancyUIHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardVacancyBinding.inflate(inflater, container, false)
        uiHandler = CardVacancyUIHandler(binding!!)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем аргумент vacancyId
        vacancyId = arguments?.getString("vacancyId")
        Log.d("CardVacancyFragment", "CardVacancyFragment vacancyId $vacancyId")
        val favoritevacancy = arguments?.getBoolean("fromFavorites")
        // Вызываем загрузку вакансии по ID
        vacancyId?.let {
            if (favoritevacancy == true) {
                сardVacancyViewModel.loadVacancyFavorite(it)
            } else сardVacancyViewModel.loadVacancy(it)
        }

        // Подписываемся на изменения вакансии и обновляем UI с использованием repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                сardVacancyViewModel.vacancy.collect { vacancy ->
                    vacancy?.let {
                        uiHandler.updateUI(it)
                        uiHandler.updateQuestions(it.questions) { question ->
                            openRespons(question)
                        }
                    }
                }
            }
        }

        // Подписываемся на изменения состояния "избранное" с использованием repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                сardVacancyViewModel.isFavorite.collect { isFavorite ->
                    uiHandler.updateFavoriteIcon(isFavorite)
                }
            }
        }

        // Назад
        binding?.cardBackArrow?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // кнопка "Избранное"
        binding?.cardFavorit?.setOnClickListener {
            сardVacancyViewModel.toggleFavorite()
        }

        // Откликнутся
        binding?.responsButton?.setOnClickListener {
            //val vacancyId = vacancy.id
            openRespons()
        }

    }

    private fun openRespons(question: String = "") {
        val responseDialog = ResponsDialogFragment()
        val bundle = Bundle().apply {
            putString("questionText", question)
        }
        responseDialog.arguments = bundle
        responseDialog.show(requireActivity().supportFragmentManager, "ResponsDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}