package com.volkov.cardvacancy.presentation.model

data class CardVacancyModel(
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

data class CardAddressModel(val town: String, val street: String, val house: String)
data class CardExperienceModel(val previewText: String, val text: String)
data class CardSalaryModel(val full: String, val short: String)