package com.volkov.cardvacancy.data.repository

import com.example.core.data.database.dao.FavoritesDao
import com.example.core.data.database.dao.VacancyDao
import com.volkov.cardvacancy.data.mapper.CardVacancyMapper
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.repository.CardVacancyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CardVacancyRepositoryImpl(
    private val vacancyDao: VacancyDao,
    private val favoritesDao: FavoritesDao,
    private val vacancyMapper: CardVacancyMapper
) : CardVacancyRepository {
    override suspend fun toggleFavorite(vacancyModel: CardVacancyDomainModel) {
        if (vacancyModel.isFavorite) {
            val favorite = vacancyMapper.mapToFavoriteModel(vacancyModel)
            favoritesDao.upsertItem(favorite)
        } else {
            val favorite = vacancyMapper.mapToFavoriteModel(vacancyModel)
            favoritesDao.delete(favorite)
        }
    }

    override suspend fun loadVacancyById(vacancyId: String): Flow<CardVacancyDomainModel> {
        return flow {
            val vacancy = vacancyDao.getVacancyById(vacancyId)
            val mappedVacancy = vacancyMapper.mapToDomain(vacancy)
            emit(mappedVacancy)
        }
    }

    override suspend fun loadVacancyByIdFromFavorites(vacancyId: String): Flow<CardVacancyDomainModel> {
        return flow {
            val favoriteVacancy = favoritesDao.getVacancyByIdfromFavorite(vacancyId)
            val mappedVacancy = vacancyMapper.mapToDomainFavorite(favoriteVacancy)
            emit(mappedVacancy)
        }
    }
}