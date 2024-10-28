package com.volkov.listvacancy.domain.repository

import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import kotlinx.coroutines.flow.Flow

interface ListVacancyRepository {
    fun getVacanciesFromDB(): Flow<List<ListVacancyDomainModel>>
    suspend fun getVacanciesFromRemote(): List<ListVacancyDomainModel>
    suspend fun getVacancies(): List<ListVacancyDomainModel>
    suspend fun getOffers(): List<ListOfferDomainModel>
    suspend fun saveFavorite(vacancy: ListVacancyDomainModel)
    suspend fun deleteFavorite(vacancy: ListVacancyDomainModel)
}