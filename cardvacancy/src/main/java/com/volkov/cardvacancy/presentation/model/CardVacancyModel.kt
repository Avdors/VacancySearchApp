package com.volkov.cardvacancy.presentation.model

class CardVacancyModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: CardAddressModel,
    val company: String,
    val experience: CardExperienceModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: CardSalaryModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

class CardAddressModel(val town: String, val street: String, val house: String)
class CardExperienceModel(val previewText: String, val text: String)
class CardSalaryModel(val full: String, val short: String)