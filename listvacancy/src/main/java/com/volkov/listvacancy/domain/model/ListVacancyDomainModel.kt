package com.volkov.listvacancy.domain.model

class ListVacancyDomainModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: ListAddressDomainModel,
    val company: String,
    val experience: ListExperienceDomainModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: ListSalaryDomainModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

data class ListAddressDomainModel(val town: String, val street: String, val house: String)
data class ListExperienceDomainModel(val previewText: String, val text: String)
data class ListSalaryDomainModel(val full: String, val short: String)