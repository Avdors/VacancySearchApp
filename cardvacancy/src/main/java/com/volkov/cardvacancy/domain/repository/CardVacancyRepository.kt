package com.volkov.cardvacancy.domain.repository

import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import kotlinx.coroutines.flow.Flow

interface CardVacancyRepository {

    suspend fun toggleFavorite(vacancyModel: CardVacancyDomainModel)
    suspend fun loadVacancyById(vacancyId: String): Flow<CardVacancyDomainModel>
    suspend fun loadVacancyByIdFromFavorites(vacancyId: String): Flow<CardVacancyDomainModel>
}