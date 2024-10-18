package com.example.core.domain.model

class VacancyDomainModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: AddressDomainModel,
    val company: String,
    val experience: ExperienceDomainModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: SalaryDomainModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
){

}

class AddressDomainModel(val town: String, val street: String, val house: String)
class ExperienceDomainModel(val previewText: String, val text: String)
class SalaryDomainModel(val full: String, val short: String)