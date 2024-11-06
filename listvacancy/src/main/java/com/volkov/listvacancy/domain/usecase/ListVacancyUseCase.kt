package com.volkov.listvacancy.domain.usecase

import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.repository.ListVacancyRepository
import kotlinx.coroutines.flow.Flow

class ListVacancyUseCase(private val repository: ListVacancyRepository) {
    fun getVacanciesFromDB(): Flow<List<ListVacancyDomainModel>> {
        return repository.getVacanciesFromDB()
    }

    suspend fun getVacanciesFromRemote(): List<ListVacancyDomainModel> {
        return repository.getVacanciesFromRemote()
    }
    suspend fun getVacancies(): List<ListVacancyDomainModel> {
        return repository.getVacancies()
    }

    suspend fun getOffersFromDB(): Flow<List<ListOfferDomainModel>> {
        return repository.getOffersFromDB()
    }

    suspend fun launchNetworkLoad() {
        repository.launchVacancyNetworkLoad()
    }

}