package com.volkov.listvacancy.domain.repository

import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel

interface ListVacancyRepository {
    suspend fun getVacancies(): List<ListVacancyDomainModel>
    suspend fun getOffers(): List<ListOfferDomainModel>
    suspend fun saveFavorite(vacancy: ListVacancyDomainModel)
    suspend fun deleteFavorite(vacancy: ListVacancyDomainModel)
}