package com.volkov.favoritevacancy.domain.usecase

import com.volkov.favoritevacancy.domain.model.FavoriteAddressDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteExperienceDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteSalaryDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import com.volkov.favoritevacancy.domain.repository.FavoriteVacancyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test

class FavoriteVacanciesUseCaseTest {

    private lateinit var useCase: FavoriteVacanciesUseCase
    private lateinit var repository: FavoriteVacancyRepository

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk(relaxed = true)
        // Инициализируем use case с замоканным репозиторием
        useCase = FavoriteVacanciesUseCase(repository)
    }

    @Test
    fun `getListVacancies should call getVacancies on repository`() = runBlocking {
        val favoriteVacancies = listOf(
            FavoriteVacancyDomainModel(
                id = "1",
                lookingNumber = 5,
                title = "test title",
                address = FavoriteAddressDomainModel("Town", "Street", "house"),
                company = "test company",
                experience = FavoriteExperienceDomainModel("preview", "experience"),
                publishedDate = "20.10.2024",
                isFavorite = true,
                salary = FavoriteSalaryDomainModel("test salary", "salary"),
                schedules = listOf("all day", "rotating"),
                appliedNumber = 7,
                description = "test description",
                responsibilities = "test responsibilities",
                questions = listOf("question1", "question2")
            )
        )

        // Настройка моков
        val flowVacancies: Flow<List<FavoriteVacancyDomainModel>> = flowOf(favoriteVacancies)
        coEvery { repository.getVacancies() } returns flowVacancies

        // Вызов метода
        val result = useCase.getListVacancies().first()

        // Проверка, что метод вызван, и результат совпадает с ожиданием
        assertEquals(favoriteVacancies, result)
        coVerify { repository.getVacancies() }
    }

    @Test
    fun `addToFavorites should call saveFavorite on repository`() = runBlocking {
        val vacancy = FavoriteVacancyDomainModel(
            id = "1",
            lookingNumber = 5,
            title = "test title",
            address = FavoriteAddressDomainModel("Town", "Street", "house"),
            company = "test company",
            experience = FavoriteExperienceDomainModel("preview", "experience"),
            publishedDate = "20.10.2024",
            isFavorite = true,
            salary = FavoriteSalaryDomainModel("test salary", "salary"),
            schedules = listOf("all day", "rotating"),
            appliedNumber = 7,
            description = "test description",
            responsibilities = "test responsibilities",
            questions = listOf("question1", "question2")
        )

        useCase.addToFavorites(vacancy)

        coVerify { repository.saveFavorite(vacancy) }
    }

    @Test
    fun `removeFromFavorites should call deleteFavorite on repository`() = runBlocking {
        val vacancy = FavoriteVacancyDomainModel(
            id = "1",
            lookingNumber = 5,
            title = "test title",
            address = FavoriteAddressDomainModel("Town", "Street", "house"),
            company = "test company",
            experience = FavoriteExperienceDomainModel("preview", "experience"),
            publishedDate = "20.10.2024",
            isFavorite = true,
            salary = FavoriteSalaryDomainModel("test salary", "salary"),
            schedules = listOf("all day", "rotating"),
            appliedNumber = 7,
            description = "test description",
            responsibilities = "test responsibilities",
            questions = listOf("question1", "question2")
        )

        useCase.removeFromFavorites(vacancy)

        coVerify { repository.deleteFavorite(vacancy) }
    }
}