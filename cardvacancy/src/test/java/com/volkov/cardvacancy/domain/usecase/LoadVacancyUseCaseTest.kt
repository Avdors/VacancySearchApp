package com.volkov.cardvacancy.domain.usecase

import com.volkov.cardvacancy.domain.model.CardDomainAddressModel
import com.volkov.cardvacancy.domain.model.CardDomainExperienceModel
import com.volkov.cardvacancy.domain.model.CardDomainSalaryModel
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.repository.CardVacancyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test

class LoadVacancyUseCaseTest {

    private lateinit var repository: CardVacancyRepository
    private lateinit var useCase: LoadVacancyUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = LoadVacancyUseCase(repository)
    }

    @Test
    fun `loadVacancyById should return vacancy from repository`() = runTest {
        // Arrange
        val vacancyId = "1"
        val expectedVacancy = CardVacancyDomainModel(
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

        coEvery { repository.loadVacancyById(vacancyId) } returns flowOf(expectedVacancy)

        // Act
        val result = useCase.loadVacancyById(vacancyId).first()

        // Assert
        assertEquals(expectedVacancy, result)
        coVerify { repository.loadVacancyById(vacancyId) }
    }

    @Test
    fun `loadVacancyByIdFavorite should return favorite vacancy from repository`() = runTest {
        // Arrange
        val vacancyId = "1"
        val expectedFavoriteVacancy = CardVacancyDomainModel(
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

        coEvery { repository.loadVacancyByIdFromFavorites(vacancyId) } returns flowOf(
            expectedFavoriteVacancy
        )

        // Act
        val result = useCase.loadVacancyByIdFavorite(vacancyId).first()

        // Assert
        assertEquals(expectedFavoriteVacancy, result)
        coVerify { repository.loadVacancyByIdFromFavorites(vacancyId) }
    }
}