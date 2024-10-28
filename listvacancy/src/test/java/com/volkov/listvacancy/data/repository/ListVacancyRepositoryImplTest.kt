package com.volkov.listvacancy.data.repository

import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.OfferModelDataBase
import com.example.core.data.database.model.VacancyModelDataBase
import com.google.common.truth.Truth.assertThat
import com.volkov.listvacancy.data.LocalDataSource
import com.volkov.listvacancy.data.RemoteDataSource
import com.volkov.listvacancy.domain.mapper.ListVacOfferDataMaper
import com.volkov.listvacancy.domain.mapper.ListVacancyDataMapper
import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


import org.junit.After
import org.junit.Before
import kotlin.test.Test

class ListVacancyRepositoryImplTest {
    private lateinit var repository: ListVacancyRepositoryImpl
    private val remoteDataSource: RemoteDataSource = mockk()
    private val localDataSource: LocalDataSource = mockk()
    private val listVacancyDataMapper: ListVacancyDataMapper = mockk()
    private val listVacOfferDataMaper: ListVacOfferDataMaper = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = ListVacancyRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            listVacancyDataMapper = listVacancyDataMapper,
            listVacOfferDataMaper = listVacOfferDataMaper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getVacancies returns cached vacancies and triggers network load`() = runTest {
        val cachedVacancies = listOf(mockk<VacancyModelDataBase>())
        val domainVacancies = listOf(mockk<ListVacancyDomainModel>())

        coEvery { localDataSource.getVacanciesFromDb() } returns cachedVacancies
        every { listVacancyDataMapper.mapToDomainFromDB(any()) } returns domainVacancies.first()

        // Act: вызов тестируемого метода
        val result = repository.getVacancies()

        // Assert: проверка результата и вызовов
        assertThat(result).isEqualTo(domainVacancies)
        coVerify { localDataSource.getVacanciesFromDb() }
        coVerify { listVacancyDataMapper.mapToDomainFromDB(any()) }
        // Опционально: проверка вызова network load
        coVerify { remoteDataSource.getData() }
    }

    @Test
    fun `getOffers returns cached offers and triggers network load`() = runTest {
        val cachedOffers = listOf(mockk<OfferModelDataBase>())
        val domainOffers = listOf(mockk<ListOfferDomainModel>())

        coEvery { localDataSource.getOffersFromDb() } returns cachedOffers
        every { listVacOfferDataMaper.mapOfferToDomainFromDB(any()) } returns domainOffers.first()

        // Act
        val result = repository.getOffers()

        // Assert
        assertThat(result).isEqualTo(domainOffers)
        coVerify { localDataSource.getOffersFromDb() }
        coVerify { listVacOfferDataMaper.mapOfferToDomainFromDB(any()) }
        coVerify { remoteDataSource.getData() }
    }

    @Test
    fun `saveFavorite maps and saves favorite vacancy`() = runTest {
        val domainVacancy = mockk<ListVacancyDomainModel>()
        val dbVacancy = mockk<FavoriteVacModelDataBase>()

        every { listVacancyDataMapper.mapFavoriteToDatabase(domainVacancy) } returns dbVacancy // возвращает объект доменной модели
        coEvery { localDataSource.upsertFavoriteDb(dbVacancy) } just Runs // так как данный метод ничего не возвращает

        // Act
        repository.saveFavorite(domainVacancy)

        // Assert
        coVerify { listVacancyDataMapper.mapFavoriteToDatabase(domainVacancy) }
        coVerify { localDataSource.upsertFavoriteDb(dbVacancy) }
    }

    @Test
    fun `deleteFavorite maps and deletes favorite vacancy`() = runTest {
        val domainVacancy = mockk<ListVacancyDomainModel>()
        val dbVacancy = mockk<FavoriteVacModelDataBase>()

        every { listVacancyDataMapper.mapFavoriteToDatabase(domainVacancy) } returns dbVacancy
        coEvery { localDataSource.deleteFavoriteDb(dbVacancy) } just Runs

        // Act
        repository.deleteFavorite(domainVacancy)

        // Assert
        coVerify { listVacancyDataMapper.mapFavoriteToDatabase(domainVacancy) }
        coVerify { localDataSource.deleteFavoriteDb(dbVacancy) }
    }
}