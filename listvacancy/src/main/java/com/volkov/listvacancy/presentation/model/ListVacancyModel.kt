package com.volkov.listvacancy.presentation.model

data class ListVacancyModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: AddressMainFragmentModel,
    val company: String,
    val experience: ExperienceMainFragmentModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: SalaryMainFragmentModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

class AddressMainFragmentModel(val town: String, val street: String, val house: String)
class ExperienceMainFragmentModel(val previewText: String, val text: String)
class SalaryMainFragmentModel(val full: String, val short: String)