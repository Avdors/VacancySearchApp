package com.volkov.listvacancy.data

import com.example.core.data.database.dao.FavoritesDao
import com.example.core.data.database.dao.OfferDao
import com.example.core.data.database.dao.VacancyDao
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.OfferModelDataBase
import com.example.core.data.database.model.VacancyModelDataBase
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val vacancyDao: VacancyDao,
    private val offerDao: OfferDao,
    private val favoritesDao: FavoritesDao
) {

    suspend fun getVacanciesFromDb(): List<VacancyModelDataBase> {
        return vacancyDao.getAllVacancies()
    }
    fun getVacanciesFlowFromDB(): Flow<List<VacancyModelDataBase>> {
        return vacancyDao.getAllVacanciesFlow()
    }

    suspend fun saveVacanciesToDb(vacancies: List<VacancyModelDataBase>) {
        vacancyDao.insertVacancies(vacancies)
    }

    fun getOffersFromDb(): Flow<List<OfferModelDataBase>> {
        return offerDao.getAllOffersFlow()
    }

    suspend fun saveOffersToDb(offers: List<OfferModelDataBase>) {
        offerDao.insertOffers(offers)
    }

    suspend fun upsertFavoriteDb(item: FavoriteVacModelDataBase) {
        favoritesDao.upsertItem(item)
    }

    suspend fun deleteFavoriteDb(item: FavoriteVacModelDataBase) {
        favoritesDao.delete(item)
    }

}