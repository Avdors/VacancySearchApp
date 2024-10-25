package com.volkov.core.utils

import android.content.Context
import com.volkov.core.R

class WordDeclension() {

    fun getHumanCountString(count: Int, context: Context): String {
        val remainder10 = count % 10
        val remainder100 = count % 100

        return when {
            remainder100 in 11..14 -> context.getString(
                R.string.human_one,
                count
            ) // Для чисел 11-14
            remainder10 == 1 -> context.getString(
                R.string.human_one,
                count
            ) // Для чисел, оканчивающихся на 1 (кроме 11)
            remainder10 in 2..4 -> context.getString(
                R.string.human_few,
                count
            ) // Для чисел, оканчивающихся на 2, 3, 4 (кроме 12, 13, 14)
            else -> context.getString(R.string.human_one, count) // Для всех остальных чисел
        }
    }

    fun getVacancyCountString(count: Int, context: Context): String {
        val remainder10 = count % 10
        val remainder100 = count % 100

        return when {
            remainder100 in 11..14 -> context.getString(
                R.string.vacancy_many,
                count
            ) // Для чисел 11-14
            remainder10 == 1 -> context.getString(
                R.string.vacancy_one,
                count
            )  // Для чисел, оканчивающихся на 1 (кроме 11)
            remainder10 in 2..4 -> context.getString(
                R.string.vacancy_few,
                count
            )  // Для чисел, оканчивающихся на 2, 3, 4 (кроме 12, 13, 14)
            else -> context.getString(R.string.vacancy_many, count)  // Для всех остальных чисел
        }
    }

}