package com.volkov.listvacancy.domain.usecase

import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.repository.ListVacancyRepository

class FavoriteUseCase(private val repository: ListVacancyRepository) {
    suspend fun addToFavorite(vacancy: ListVacancyDomainModel) {
        repository.saveFavorite(vacancy)
    }

    suspend fun removeFavorite(vacancy: ListVacancyDomainModel) {
        repository.deleteFavorite(vacancy)
    }
}