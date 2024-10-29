package com.volkov.favoritevacancy.data.repository


import com.example.core.data.database.dao.FavoritesDao
import com.example.core.data.database.model.AddressFModelDataBase
import com.example.core.data.database.model.ExperienceFModelDataBase
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.SalaryFModelDataBase
import com.google.common.truth.Truth.assertThat
import com.volkov.favoritevacancy.domain.mapper.FavoriteVacancyDataMaper
import com.volkov.favoritevacancy.domain.model.FavoriteAddressDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteExperienceDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteSalaryDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteVacancyRepositoryImplTest {
    private lateinit var repository: FavoriteVacancyRepositoryImpl
    private lateinit var vacancyDataMaper: FavoriteVacancyDataMaper
    private lateinit var favoritesDao: FavoritesDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        vacancyDataMaper = mockk()
        favoritesDao = mockk()
        repository = FavoriteVacancyRepositoryImpl(vacancyDataMaper, favoritesDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getVacancies returns mapped data from DAO`() = runTest {
        val dbVacancies = listOf(
            FavoriteVacModelDataBase(
                id = "1", lookingNumber = 100, title = "Vacancy 1",
                address = AddressFModelDataBase("City", "Street", "1"),
                company = "Company 1", experience = ExperienceFModelDataBase("1 year", "2 years"),
                publishedDate = "2024-10-05", isFavorite = false,
                salary = SalaryFModelDataBase("1000 USD", "1K"),
                schedules = emptyList(), appliedNumber = 10,
                description = "description", responsibilities = "responsibilities",
                questions = emptyList()
            ),
            FavoriteVacModelDataBase(
                id = "2", lookingNumber = 100, title = "Vacancy 2",
                address = AddressFModelDataBase("City", "Street", "2"),
                company = "Company 1", experience = ExperienceFModelDataBase("1 year", "2 years"),
                publishedDate = "2024-10-05", isFavorite = false,
                salary = SalaryFModelDataBase("1000 USD", "1K"),
                schedules = emptyList(), appliedNumber = 10,
                description = "description", responsibilities = "responsibilities",
                questions = emptyList()
            )
        )

        val domainVacancies = listOf(
            FavoriteVacancyDomainModel(
                id = "1",
                lookingNumber = 100,
                title = "Vacancy 1",
                address = FavoriteAddressDomainModel("City", "Street", "1"),
                company = "Company 1",
                experience = FavoriteExperienceDomainModel("1 year", "2 years"),
                publishedDate = "2024-10-05",
                isFavorite = false,
                salary = FavoriteSalaryDomainModel("1000 USD", "1K"),
                schedules = emptyList(),
                appliedNumber = 10,
                description = "description",
                responsibilities = "responsibilities",
                questions = emptyList()
            ),
            FavoriteVacancyDomainModel(
                id = "2",
                lookingNumber = 100,
                title = "Vacancy 2",
                address = FavoriteAddressDomainModel("City", "Street", "2"),
                company = "Company 1",
                experience = FavoriteExperienceDomainModel("1 year", "2 years"),
                publishedDate = "2024-10-05",
                isFavorite = false,
                salary = FavoriteSalaryDomainModel("1000 USD", "1K"),
                schedules = emptyList(),
                appliedNumber = 10,
                description = "description",
                responsibilities = "responsibilities",
                questions = emptyList()
            )
        )

        // Настройка моков
        every { favoritesDao.getItemList() } returns flowOf(dbVacancies)
        dbVacancies.forEachIndexed { index, dbModel ->
            every { vacancyDataMaper.mapToDomainFromDB(dbModel) } returns domainVacancies[index]
        }

        // Тестирование функции
        val result = repository.getVacancies().first()
        assertThat(result).isEqualTo(domainVacancies)

        // Проверка вызова методов
        verify(exactly = 1) { favoritesDao.getItemList() }
        dbVacancies.forEach { verify(exactly = 1) { vacancyDataMaper.mapToDomainFromDB(it) } }
    }

    @Test
    fun `test saveFavorite calls DAO with mapped data`() = runTest {
        val domainModel = FavoriteVacancyDomainModel(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = FavoriteAddressDomainModel("City", "Street", "1"),
            company = "Company 1", experience = FavoriteExperienceDomainModel("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = false,
            salary = FavoriteSalaryDomainModel("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )
        val dbModel = FavoriteVacModelDataBase(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = AddressFModelDataBase("City", "Street", "1"),
            company = "Company 1", experience = ExperienceFModelDataBase("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = false,
            salary = SalaryFModelDataBase("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )

        // Настройка моков
        every { vacancyDataMaper.mapToDatabase(domainModel) } returns dbModel
        coEvery { favoritesDao.upsertItem(dbModel) } just runs

        // Вызов функции
        repository.saveFavorite(domainModel)

        // Проверка вызова методов
        coVerify { vacancyDataMaper.mapToDatabase(domainModel) }
        coVerify { favoritesDao.upsertItem(dbModel) }
    }

    @Test
    fun `test deleteFavorite calls DAO with mapped data`() = runTest {
        val domainModel = FavoriteVacancyDomainModel(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = FavoriteAddressDomainModel("City", "Street", "1"),
            company = "Company 1", experience = FavoriteExperienceDomainModel("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = false,
            salary = FavoriteSalaryDomainModel("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )
        val dbModel = FavoriteVacModelDataBase(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = AddressFModelDataBase("City", "Street", "1"),
            company = "Company 1", experience = ExperienceFModelDataBase("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = false,
            salary = SalaryFModelDataBase("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )

        // Настройка моков
        every { vacancyDataMaper.mapToDatabase(domainModel) } returns dbModel
        coEvery { favoritesDao.delete(dbModel) } just runs

        // Вызов функции
        repository.deleteFavorite(domainModel)

        // Проверка вызова методов
        coVerify { vacancyDataMaper.mapToDatabase(domainModel) }
        coVerify { favoritesDao.delete(dbModel) }
    }
}