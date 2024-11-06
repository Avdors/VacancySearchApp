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
    private val listVacOfferDataMapper: ListVacOfferDataMaper
) : ListVacancyRepository {


    override fun getVacanciesFromDB(): Flow<List<ListVacancyDomainModel>> {
        return localDataSource.getVacanciesFlowFromDB()
            .map { vacancies ->
                vacancies.map { listVacancyDataMapper.mapToDomainFromDB(it) }
            }
    }

    override suspend fun getVacanciesFromRemote(): List<ListVacancyDomainModel> {
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
        return cachedVacancies
    }


    override suspend fun getOffersFromDB(): Flow<List<ListOfferDomainModel>> {
        return localDataSource.getOffersFromDb()
            .map { offers ->
                offers.map { listVacOfferDataMapper.mapOfferToDomainFromDB(it) }
            }
    }

    override suspend fun saveFavorite(vacancy: ListVacancyDomainModel) {
        // маппим доменную модель в модель БД
        localDataSource.upsertFavoriteDb(listVacancyDataMapper.mapFavoriteToDatabase(vacancy))
    }

    override suspend fun deleteFavorite(vacancy: ListVacancyDomainModel) {
        // маппим доменную модель в модель БД
        localDataSource.deleteFavoriteDb(listVacancyDataMapper.mapFavoriteToDatabase(vacancy))
    }

    // здесь загружаем данные из сети и сохраняем в БД
    override suspend fun launchVacancyNetworkLoad() {
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

                    // Обработка вакансий
                    val vacancies = response.vacancies?.filterNotNull()?.map { vacancy ->
                        listVacancyDataMapper.mapToDomain(vacancy)
                    } ?: emptyList()

                    if (vacancies.isNotEmpty()) {
                        localDataSource.saveVacanciesToDb(vacancies.map {
                            listVacancyDataMapper.mapToDatabase(it)
                        })
                        Log.d(
                            "ListVacancyRepositoryImpl",
                            "Вакансии успешно загружены и сохранены в кеш"
                        )
                    }

                    // Обработка offers
                    val offers = response.offers?.filterNotNull()?.map { offer ->
                        listVacOfferDataMapper.mapOfferToDomain(offer)
                    } ?: emptyList()

                    if (offers.isNotEmpty()) {
                        localDataSource.saveOffersToDb(offers.map {
                            listVacOfferDataMapper.mapOfferToDatabase(it)
                        })
                        Log.d(
                            "ListVacancyRepositoryImpl",
                            "Offers успешно загружены и сохранены в кеш"
                        )
                    }

                    // Если успешно, выходим из цикла
                    break
                } catch (e: Exception) {
                    retries--

                    if (retries > 0) {
                        // Ожидаем 7 секунд перед повторной попыткой
                        delay(7000)
                    } else {
                        Log.e("ListVacancyRepositoryImpl", "Ошибка загрузки данных: ${e.message}")
                    }
                }
            }
        }
    }
}