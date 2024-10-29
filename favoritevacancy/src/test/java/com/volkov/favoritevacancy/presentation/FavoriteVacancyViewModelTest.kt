package com.volkov.favoritevacancy.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.volkov.favoritevacancy.domain.model.FavoriteAddressDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteExperienceDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteSalaryDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import com.volkov.favoritevacancy.domain.usecase.FavoriteVacanciesUseCase
import com.volkov.favoritevacancy.presentation.mapper.FavoriteVacancyMapper
import com.volkov.favoritevacancy.presentation.model.FavoriteAddressModel
import com.volkov.favoritevacancy.presentation.model.FavoriteExperienceModel
import com.volkov.favoritevacancy.presentation.model.FavoriteSalaryModel
import com.volkov.favoritevacancy.presentation.model.FavoriteVacancyModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteVacancyViewModelTest {

    private lateinit var viewModel: FavoriteVacancyViewModel
    private lateinit var favoriteUseCase: FavoriteVacanciesUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        favoriteUseCase = mockk()

        // Предоставляем пустой ответ для getListVacancies, чтобы избежать ошибок в init-блоке ViewModel
        coEvery { favoriteUseCase.getListVacancies() } returns flowOf(emptyList())

        viewModel = FavoriteVacancyViewModel(favoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initializes and collects vacancies successfully`() = runTest {
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

        // Мокаем поведение метода getListVacancies
        coEvery { favoriteUseCase.getListVacancies() } returns flowOf(domainVacancies)

        // Вызываем loadVacancies() вручную для управления процессом загрузки в тесте
        viewModel.loadVacancies()

        // Дожидаемся завершения всех корутин
        advanceUntilIdle()

        // Проверяем содержимое потока
        viewModel.vacancies.test {
            val emittedVacancies = awaitItem()
            assertThat(emittedVacancies).hasSize(2)
            assertThat(emittedVacancies[0].title).isEqualTo("Vacancy 1")
            assertThat(emittedVacancies[1].title).isEqualTo("Vacancy 2")
        }
    }

    @Test
    fun `removeFromFavorites updates favorite status to false`() = runTest {
        val favoriteVacancy = FavoriteVacancyModel(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = FavoriteAddressModel("City", "Street", "1"),
            company = "Company 1", experience = FavoriteExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = true,
            salary = FavoriteSalaryModel("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )

        // Подготовим мок, чтобы гарантировать успешное выполнение удаления
        coEvery { favoriteUseCase.removeFromFavorites(any()) } just runs
        // Инициализируем начальные данные
        coEvery { favoriteUseCase.getListVacancies() } returns flowOf(
            listOf(FavoriteVacancyMapper.mapToDomainModel(favoriteVacancy))
        )

        viewModel.loadVacancies()
        advanceUntilIdle()

        // Начинаем тестирование потока
        viewModel.vacancies.test {
            // Подтверждаем, что начальное значение установлено
            val initialVacancies = awaitItem()
            assertThat(initialVacancies).hasSize(1)
            assertThat(initialVacancies[0].isFavorite).isTrue()

            // Выполняем обновление, которое изменит статус на "не избранное"
            viewModel.updateFavorites(favoriteVacancy)
            advanceUntilIdle()

            // Проверяем обновленное значение в потоке
            val updatedVacancies = awaitItem()
            assertThat(updatedVacancies[0].isFavorite).isFalse()

            // Проверка вызова use case для удаления из избранного
            coVerify { favoriteUseCase.removeFromFavorites(any()) }
        }
    }

    @Test
    fun `error is set on exception when adding to favorites`() = runTest {
        val vacancy = FavoriteVacancyModel(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = FavoriteAddressModel("City", "Street", "1"),
            company = "Company 1", experience = FavoriteExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = true,
            salary = FavoriteSalaryModel("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )

        coEvery { favoriteUseCase.addToFavorites(any()) } throws Exception("Test error")

        viewModel.updateFavorites(vacancy)

        assertThat(viewModel.error.value).isEqualTo("Ошибка добавления в избранное: Test error")
    }

    @Test
    fun `error is set on exception when removing from favorites`() = runTest {
        val vacancy = FavoriteVacancyModel(
            id = "1", lookingNumber = 100, title = "Vacancy 1",
            address = FavoriteAddressModel("City", "Street", "1"),
            company = "Company 1", experience = FavoriteExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05", isFavorite = true,
            salary = FavoriteSalaryModel("1000 USD", "1K"),
            schedules = emptyList(), appliedNumber = 10,
            description = "description", responsibilities = "responsibilities",
            questions = emptyList()
        )

        coEvery { favoriteUseCase.removeFromFavorites(any()) } throws Exception("Test error")

        viewModel.updateFavorites(vacancy)

        assertThat(viewModel.error.value).isEqualTo("Ошибка удаления из избранного: Test error")
    }
}