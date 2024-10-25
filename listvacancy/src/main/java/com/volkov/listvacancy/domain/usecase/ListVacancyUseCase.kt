package com.volkov.listvacancy.domain.usecase

import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.repository.ListVacancyRepository

class ListVacancyUseCase(private val repository: ListVacancyRepository) {

    suspend fun getVacancies(): List<ListVacancyDomainModel> {
        return repository.getVacancies()
    }

    suspend fun getOffers(): List<ListOfferDomainModel> {
        return repository.getOffers()
    }

}