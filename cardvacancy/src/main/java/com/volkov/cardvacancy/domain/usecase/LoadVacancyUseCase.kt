package com.volkov.cardvacancy.domain.usecase

import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.repository.CardVacancyRepository
import kotlinx.coroutines.flow.Flow

class LoadVacancyUseCase(private val repository: CardVacancyRepository) {
    suspend fun loadVacancyById(vacancyId: String): Flow<CardVacancyDomainModel> {
        return repository.loadVacancyById(vacancyId)
    }

    suspend fun loadVacancyByIdFavorite(vacancyId: String): Flow<CardVacancyDomainModel> {
        return repository.loadVacancyByIdFromFavorites(vacancyId)
    }

}