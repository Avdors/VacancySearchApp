package com.volkov.favoritevacancy.presentation.model

data class FavoriteVacancyModel(
    val id: String,
    val lookingNumber: Int,
    val title: String,
    val address: FavoriteAddressModel,
    val company: String,
    val experience: FavoriteExperienceModel,
    val publishedDate: String,
    val isFavorite: Boolean,
    val salary: FavoriteSalaryModel,
    val schedules: List<String>,
    val appliedNumber: Int,
    val description: String,
    val responsibilities: String,
    val questions: List<String>
) {

}

class FavoriteAddressModel(val town: String, val street: String, val house: String)
class FavoriteExperienceModel(val previewText: String, val text: String)
class FavoriteSalaryModel(val full: String, val short: String)