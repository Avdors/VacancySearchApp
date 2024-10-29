package com.volkov.cardvacancy.data.model

class CardVacancyDataModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: CardDataAddressModel,
    val company: String,
    val experience: CardDataExperienceModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: CardDataSalaryModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {
}

class CardDataAddressModel(val town: String, val street: String, val house: String)
class CardDataExperienceModel(val previewText: String, val text: String)
class CardDataSalaryModel(val full: String, val short: String)