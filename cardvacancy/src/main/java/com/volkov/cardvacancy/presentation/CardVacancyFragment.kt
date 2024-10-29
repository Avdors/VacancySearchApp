package com.volkov.cardvacancy.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volkov.cardvacancy.R
import com.volkov.cardvacancy.databinding.FragmentCardVacancyBinding
import com.volkov.cardvacancy.presentation.model.CardVacancyModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardVacancyFragment : Fragment() {
    private var isFavoriteVacancy: Boolean = false
    private val сardVacancyViewModel: CardVacancyViewModel by viewModel()
    lateinit var binding: FragmentCardVacancyBinding
    private var vacancyId: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCardVacancyBinding.inflate(inflater, container, false)
        return binding.root
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
                    vacancy?.let { updateUI(vacancy) }
                }
            }
        }

        // Подписываемся на изменения состояния "избранное" с использованием repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                сardVacancyViewModel.isFavorite.collect { isFavorite ->
                    updateFavoriteIcon(isFavorite)
                }
            }
        }

        // Назад
        binding.cardBackArrow.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // кнопка "Избранное"
        binding.cardFavorit.setOnClickListener {
            сardVacancyViewModel.toggleFavorite()
        }

        // Откликнутся
        binding.responsButton.setOnClickListener {
            //val vacancyId = vacancy.id
            openRespons()
        }

    }

    private fun openRespons(question: String = "") {

    }

    // Функция для обновления UI вакансии
    private fun updateUI(vacancy: CardVacancyModel) {
        binding.cardVacancyTitle.text = vacancy.title
        binding.cardVacancySalary.text = vacancy.salary.full
        binding.cardVacancyExperience.text = "Требуемый опыт: ${vacancy.experience.text}"
        binding.cardVacancyPublishedDate.text = vacancy.schedules
            .mapIndexed { index, schedule ->
                if (index == 0) schedule.replaceFirstChar { char -> char.uppercaseChar() }
                else schedule
            }
            .joinToString(", ")

        if (vacancy.appliedNumber != null) {
            binding.eyeFirstLine.text = "${vacancy.appliedNumber} человек уже откликнулись"
        } else {
            binding.eyeFirstLine.visibility = View.GONE
        }

        if (vacancy.lookingNumber != null) {
            binding.secondLine.text = "${vacancy.lookingNumber} человека сейчас смотрят"
        } else {
            binding.secondLine.visibility = View.GONE
        }

        binding.cardCompany.text = vacancy.company
        binding.cardAdress.text =
            "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
        binding.cardDescription.text = vacancy.description
        binding.cardTaskInfo.text = vacancy.responsibilities

        // Обновляем вопросы
        updateQuestions(vacancy.questions)
    }

    // Функция для обновления состояния "избранное"
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        isFavoriteVacancy = isFavorite
        val favoriteIconRes = if (isFavorite) {
            R.drawable.fill_heart_icon
        } else {
            R.drawable.heart_icon
        }
        binding.cardFavorit.setImageResource(favoriteIconRes)
    }


    // Функция для обновления списка вопросов
    private fun updateQuestions(questions: List<String>?) {
        val cardListQuestionLayout = binding.cardListQuestion
        cardListQuestionLayout.removeAllViews()
        questions?.forEach { question ->
            val button = Button(requireContext()).apply {
                text = question
                setBackgroundResource(R.drawable.back_grey2_corner24)
                setTextAppearance(R.style.RegularText_14size)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(
                        0,
                        resources.getDimensionPixelSize(R.dimen.dp16),
                        0,
                        0
                    )
                }
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.dp16),
                    resources.getDimensionPixelSize(R.dimen.dp8),
                    resources.getDimensionPixelSize(R.dimen.dp16),
                    resources.getDimensionPixelSize(R.dimen.dp8)
                )
                transformationMethod = null
                setOnClickListener {
                    openRespons(question)
                }
            }
            cardListQuestionLayout.addView(button)
        }
    }

}