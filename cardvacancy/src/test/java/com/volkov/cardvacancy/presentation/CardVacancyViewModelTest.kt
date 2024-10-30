package com.volkov.cardvacancy.presentation

import app.cash.turbine.test
import com.volkov.cardvacancy.domain.model.CardDomainAddressModel
import com.volkov.cardvacancy.domain.model.CardDomainExperienceModel
import com.volkov.cardvacancy.domain.model.CardDomainSalaryModel
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.usecase.FavoriteCardVacancyUseCase
import com.volkov.cardvacancy.domain.usecase.LoadVacancyUseCase
import com.volkov.cardvacancy.presentation.mapper.CardVacancyMapperUI
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CardVacancyViewModelTest {

    private lateinit var viewModel: CardVacancyViewModel
    private lateinit var loadVacancyUseCase: LoadVacancyUseCase
    private lateinit var favoriteUseCase: FavoriteCardVacancyUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loadVacancyUseCase = mockk()
        favoriteUseCase = mockk()
        viewModel = CardVacancyViewModel(loadVacancyUseCase, favoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadVacancy should update vacancy and isFavorite states`() = runTest {
        // Arrange
        val vacancyId = "1"
        val vacancyDomain = CardVacancyDomainModel(
            id = vacancyId,
            lookingNumber = 100,
            title = "Vacancy 1",
            address = CardDomainAddressModel("City", "Street", "1"),
            company = "Company 1",
            experience = CardDomainExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = true,
            salary = CardDomainSalaryModel("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )

        coEvery { loadVacancyUseCase.loadVacancyById(vacancyId) } returns flowOf(vacancyDomain)

        // Act
        viewModel.loadVacancy(vacancyId)
        advanceUntilIdle()

        // Assert
        viewModel.vacancy.test {
            assertEquals(CardVacancyMapperUI.mapToUIModel(vacancyDomain), awaitItem())
        }
        viewModel.isFavorite.test {
            assertEquals(true, awaitItem())
        }
        coVerify { loadVacancyUseCase.loadVacancyById(vacancyId) }
    }

    @Test
    fun `loadVacancyFavorite should update vacancy and isFavorite states`() = runTest {
        // Arrange
        val vacancyId = "1"
        val favoriteVacancyDomain = CardVacancyDomainModel(
            id = vacancyId,
            lookingNumber = 100,
            title = "Vacancy 1",
            address = CardDomainAddressModel("City", "Street", "1"),
            company = "Company 1",
            experience = CardDomainExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = true,
            salary = CardDomainSalaryModel("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )

        coEvery { loadVacancyUseCase.loadVacancyByIdFavorite(vacancyId) } returns flowOf(
            favoriteVacancyDomain
        )

        // Act
        viewModel.loadVacancyFavorite(vacancyId)
        advanceUntilIdle()

        // Assert
        viewModel.vacancy.test {
            assertEquals(CardVacancyMapperUI.mapToUIModel(favoriteVacancyDomain), awaitItem())
        }
        viewModel.isFavorite.test {
            assertEquals(true, awaitItem())
        }
        coVerify { loadVacancyUseCase.loadVacancyByIdFavorite(vacancyId) }
    }

    @Test
    fun `toggleFavorite should update isFavorite state and call favoriteUseCase`() = runTest {
        // Arrange
        val vacancyId = "1"
        val vacancyDomain = CardVacancyDomainModel(
            id = vacancyId,
            lookingNumber = 100,
            title = "Vacancy 1",
            address = CardDomainAddressModel("City", "Street", "1"),
            company = "Company 1",
            experience = CardDomainExperienceModel("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = false,
            salary = CardDomainSalaryModel("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )

        coEvery { loadVacancyUseCase.loadVacancyById(vacancyId) } returns flowOf(vacancyDomain)
        coEvery { favoriteUseCase.togleFavorite(any()) } returns Unit

        // Act
        viewModel.loadVacancy(vacancyId)
        advanceUntilIdle()  // Ensure loading finishes

        viewModel.toggleFavorite()
        advanceUntilIdle()  // Ensure toggling finishes

        // Assert
        viewModel.isFavorite.test {
            assertEquals(true, awaitItem())
        }
        coVerify { favoriteUseCase.togleFavorite(vacancyDomain.copy(isFavorite = true)) }
    }
}