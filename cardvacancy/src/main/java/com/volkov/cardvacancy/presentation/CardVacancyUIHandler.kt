package com.volkov.cardvacancy.presentation

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.volkov.cardvacancy.R
import com.volkov.cardvacancy.databinding.FragmentCardVacancyBinding
import com.volkov.cardvacancy.presentation.model.CardVacancyModel

class CardVacancyUIHandler(private val binding: FragmentCardVacancyBinding) {

    fun updateUI(vacancy: CardVacancyModel) {
        binding.cardVacancyTitle.text = vacancy.title
        binding.cardVacancySalary.text = vacancy.salary.full
        binding.cardVacancyExperience.text = "Требуемый опыт + ${vacancy.experience.text}"
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


    }

    fun updateQuestions(questions: List<String>?, onClick: (String) -> Unit) {
        val cardListQuestionLayout = binding.cardListQuestion
        cardListQuestionLayout.removeAllViews()
        questions?.forEach { question ->
            val button = Button(binding.root.context).apply {
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
                    onClick(question)
                }
            }
            cardListQuestionLayout.addView(button)
        }
    }

    fun updateFavoriteIcon(isFavorite: Boolean) {
        val favoriteIconRes = if (isFavorite) {
            R.drawable.fill_heart_icon
        } else {
            R.drawable.heart_icon
        }
        binding.cardFavorit.setImageResource(favoriteIconRes)
    }
}