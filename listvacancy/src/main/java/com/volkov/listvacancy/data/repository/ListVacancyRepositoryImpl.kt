package com.volkov.listvacancy.data.repository

import android.util.Log
import com.volkov.listvacancy.data.LocalDataSource
import com.volkov.listvacancy.data.RemoteDataSource
import com.volkov.listvacancy.domain.mapper.ListVacOfferDataMaper
import com.volkov.listvacancy.domain.mapper.ListVacancyDataMapper
import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.repository.ListVacancyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListVacancyRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val listVacancyDataMapper: ListVacancyDataMapper,
    private val listVacOfferDataMaper: ListVacOfferDataMaper
) : ListVacancyRepository {
    override fun getVacanciesFromDB(): Flow<List<ListVacancyDomainModel>> {
        return localDataSource.getVacanciesFlowFromDB()
            .map { vacancies ->
                vacancies.map { listVacancyDataMapper.mapToDomainFromDB(it) }
            }
        // Параллельно запускаем загрузку данных из сети для обновления кеша
        launchVacancyNetworkLoad()
    }

    override suspend fun getVacanciesFromRemote(): List<ListVacancyDomainModel> {
        launchVacancyNetworkLoad()
        val cachedVacancies = localDataSource.getVacanciesFromDb().map { vacancy ->
            listVacancyDataMapper.mapToDomainFromDB(vacancy)
        }
        return cachedVacancies

    }

    override suspend fun getVacancies(): List<ListVacancyDomainModel> {
        // Сначала загружаем данные из кеша (БД) и возвращаем их
        // мапим из БД в доменную модель
        val cachedVacancies = localDataSource.getVacanciesFromDb().map { vacancy ->
            listVacancyDataMapper.mapToDomainFromDB(vacancy)
        }
        // Параллельно запускаем загрузку данных из сети для обновления кеша
        launchVacancyNetworkLoad()
        // Возвращаем данные из кеша, если они есть
        return cachedVacancies
    }

    private fun launchVacancyNetworkLoad() {
        CoroutineScope(Dispatchers.IO).launch {
            // при отсутствии интернета, повторяем попытку загрузки через 7 секунд
            var retries = 5
            while (retries > 0) {
                try {
                    val response = remoteDataSource.getData()
                    Log.d(
                        "ListVacancyRepositoryImpl",
                        "domainVacancies: ${response.vacancies?.size}"
                    )
                    val vacancies = response.vacancies?.filterNotNull()?.map { vacancy ->
                        listVacancyDataMapper.mapToDomain(vacancy)
                    } ?: emptyList()

                    if (vacancies.isNotEmpty()) {
                        localDataSource.saveVacanciesToDb(vacancies.map {
                            listVacancyDataMapper.mapToDatabase(
                                it
                            )
                        })
                        Log.d(
                            "ListVacancyRepositoryImpl",
                            "Данные из сети успешно загружены и сохранены в кеш"
                        )
                    }
                    // Если успешно, выходим из цикла
                    break
                } catch (e: Exception) {

                    retries--

                    if (retries > 0) {
                        // Ожидаем 7 секунд перед повторной попыткой
                        delay(7000)
                    }
                }
            }
        }
    }


    override suspend fun getOffers(): List<ListOfferDomainModel> {
        // Сразу загружаем данные из кеша (БД)
        // маппим из БД в доменную модель
        val cachedOffers = localDataSource.getOffersFromDb().map { offer ->
            listVacOfferDataMaper.mapOfferToDomainFromDB(offer)
        }
        // Параллельно запускаем загрузку данных из сети для обновления кеша
        launchVacancyNetworkLoad()

        return cachedOffers
    }

    override suspend fun saveFavorite(vacancy: ListVacancyDomainModel) {
        // маппим доменную модель в модель БД
        localDataSource.upsertFavoriteDb(listVacancyDataMapper.mapFavoriteToDatabase(vacancy))
    }

    override suspend fun deleteFavorite(vacancy: ListVacancyDomainModel) {
        // маппим доменную модель в модель БД
        localDataSource.deleteFavoriteDb(listVacancyDataMapper.mapFavoriteToDatabase(vacancy))
    }
}