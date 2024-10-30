package com.volkov.cardvacancy.domain.usecase

import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.repository.CardVacancyRepository

class FavoriteCardVacancyUseCase(private val repository: CardVacancyRepository) {

    suspend fun togleFavorite(vacancyModel: CardVacancyDomainModel) {
        repository.toggleFavorite(vacancyModel)
    }
}
