package com.volkov.favoritevacancy.domain.usecase

import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import com.volkov.favoritevacancy.domain.repository.FavoriteVacancyRepository
import kotlinx.coroutines.flow.Flow

class FavoriteVacanciesUseCase(private val repository: FavoriteVacancyRepository) {
    suspend fun getListVacancies(): Flow<List<FavoriteVacancyDomainModel>> {
        return repository.getVacancies()
    }

    suspend fun addToFavorites(vacancy: FavoriteVacancyDomainModel) {
        repository.saveFavorite(vacancy)
    }

    suspend fun removeFromFavorites(vacancy: FavoriteVacancyDomainModel) {
        repository.deleteFavorite(vacancy)
    }
}