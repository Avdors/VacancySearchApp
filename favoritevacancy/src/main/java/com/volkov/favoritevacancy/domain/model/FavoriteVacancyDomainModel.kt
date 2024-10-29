package com.volkov.favoritevacancy.domain.model

class FavoriteVacancyDomainModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: FavoriteAddressDomainModel,
    val company: String,
    val experience: FavoriteExperienceDomainModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: FavoriteSalaryDomainModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

class FavoriteAddressDomainModel(val town: String, val street: String, val house: String)
class FavoriteExperienceDomainModel(val previewText: String, val text: String)
class FavoriteSalaryDomainModel(val full: String, val short: String)