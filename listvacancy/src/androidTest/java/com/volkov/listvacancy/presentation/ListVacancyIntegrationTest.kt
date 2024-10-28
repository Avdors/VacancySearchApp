package com.volkov.listvacancy.presentation


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.volkov.listvacancy.data.LocalDataSource
import com.volkov.listvacancy.data.RemoteDataSource
import com.volkov.listvacancy.data.model.ModelListResponseOfferAndVacancies
import com.volkov.listvacancy.data.repository.ListVacancyRepositoryImpl
import com.volkov.listvacancy.domain.mapper.ListVacancyDataMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class ListVacancyIntegrationTest {

    private lateinit var repository: ListVacancyRepositoryImpl
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private val mapper = ListVacancyDataMapper()

    @Before
    fun setUp() {
        // Создаем моки для источников данных
        localDataSource = mockk()
        remoteDataSource = mockk()
        startKoin {
            modules(
                module {
                    single { mapper }
                    single { localDataSource }
                    single { remoteDataSource }
                }
            )
        }
        // Инициализация репозитория с моками
        repository = ListVacancyRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            listVacancyDataMapper = mapper,
            listVacOfferDataMaper = mockk()
        )

        // Настраиваем поведение моков
        coEvery { localDataSource.getVacanciesFromDb() } returns emptyList()

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun load_vacancies_from_JSON_and_verify_display() = runTest {
        // Загружаем JSON из assets
        val jsonString = InstrumentationRegistry.getInstrumentation().context.assets
            .open("vacancies.json")
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
        Log.d("ListVacancyIntegrationTest", "Decoded jsonString: $jsonString")
        val data = Json.decodeFromString<ModelListResponseOfferAndVacancies>(jsonString)
        Log.d("ListVacancyIntegrationTest", "Decoded JSON: $data")
        Log.d("ListVacancyIntegrationTest", "Decoded to string ${data.toString()}")
        // Задаем поведение для remoteDataSource
        coEvery { remoteDataSource.getData() } returns data

        // Загружаем вакансии из репозитория
        val vacancies = repository.getVacancies()

        // Проверяем, что список вакансий не пуст
        assertThat(vacancies).isNotEmpty()

        // Проверяем корректность первой вакансии
        val vacancy = vacancies.first()
        assertThat(vacancy.id).isEqualTo("cbf0c984-7c6c-4ada-82da-e29dc698bb50")
        assertThat(vacancy.title).isEqualTo("Android Developer")
        assertThat(vacancy.company).isEqualTo("Tech Corp")
        assertThat(vacancy.address.town).isEqualTo("City")
        assertThat(vacancy.salary.full).isEqualTo("1000 USD")

        // Проверяем, что метод getData был вызван один раз
        coVerify { remoteDataSource.getData() }
    }
}