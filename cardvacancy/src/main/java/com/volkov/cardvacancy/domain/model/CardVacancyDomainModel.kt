package com.volkov.cardvacancy.domain.model

data class CardVacancyDomainModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: CardDomainAddressModel,
    val company: String,
    val experience: CardDomainExperienceModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: CardDomainSalaryModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

class CardDomainAddressModel(val town: String, val street: String, val house: String)
class CardDomainExperienceModel(val previewText: String, val text: String)
class CardDomainSalaryModel(val full: String, val short: String)