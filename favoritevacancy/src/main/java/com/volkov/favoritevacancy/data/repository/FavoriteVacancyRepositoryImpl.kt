package com.volkov.favoritevacancy.data.repository

import com.example.core.data.database.dao.FavoritesDao
import com.volkov.favoritevacancy.domain.mapper.FavoriteVacancyDataMaper
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import com.volkov.favoritevacancy.domain.repository.FavoriteVacancyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteVacancyRepositoryImpl(
    private val vacancyDataMaper: FavoriteVacancyDataMaper,
    private val favoritesDao: FavoritesDao
) : FavoriteVacancyRepository {
    override suspend fun getVacancies(): Flow<List<FavoriteVacancyDomainModel>> {
        return favoritesDao.getItemList().map { vacancies ->
            vacancies.map { vacancy -> vacancyDataMaper.mapToDomainFromDB(vacancy) }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveFavorite(vacancy: FavoriteVacancyDomainModel) =
        withContext(Dispatchers.IO) {
            favoritesDao.upsertItem(vacancyDataMaper.mapToDatabase(vacancy))
        }

    override suspend fun deleteFavorite(vacancy: FavoriteVacancyDomainModel) =
        withContext(Dispatchers.IO) {
            favoritesDao.delete(vacancyDataMaper.mapToDatabase(vacancy))
        }
}