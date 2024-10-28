package com.volkov.listvacancy.domain.usecase

import com.volkov.listvacancy.domain.model.ListAddressDomainModel
import com.volkov.listvacancy.domain.model.ListExperienceDomainModel
import com.volkov.listvacancy.domain.model.ListSalaryDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.repository.ListVacancyRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FavoriteUseCaseTest {

    private lateinit var useCase: FavoriteUseCase
    private lateinit var repository: ListVacancyRepository

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk(relaxed = true)
        // Инициализируем use case с замоканным репозиторием
        useCase = FavoriteUseCase(repository)
    }

    @Test
    fun `addToFavorite should call saveFavorite on repository`() = runBlocking {
        val vacancy = ListVacancyDomainModel(
            id = "1",
            lookingNumber = 5,
            title = "test title",
            address = ListAddressDomainModel("Town", "Street", "house"),
            company = "test company",
            experience = ListExperienceDomainModel("prewiew", "experience"),
            publishedDate = "20.10.2024",
            isFavorite = true,
            salary = ListSalaryDomainModel("test salary", "salary"),
            schedules = listOf("all day", "rotating"),
            appliedNumber = 7,
            description = "test description",
            responsibilities = "test responsibilities",
            questions = listOf("question1", "question2")
        )

        useCase.addToFavorite(vacancy)

        coVerify { repository.saveFavorite(vacancy) }


    }

    @Test
    fun `removeToFavorite should call saveFavorite on repository`() = runBlocking {
        val vacancy = ListVacancyDomainModel(
            id = "1",
            lookingNumber = 5,
            title = "test title",
            address = ListAddressDomainModel("Town", "Street", "house"),
            company = "test company",
            experience = ListExperienceDomainModel("prewiew", "experience"),
            publishedDate = "20.10.2024",
            isFavorite = true,
            salary = ListSalaryDomainModel("test salary", "salary"),
            schedules = listOf("all day", "rotating"),
            appliedNumber = 7,
            description = "test description",
            responsibilities = "test responsibilities",
            questions = listOf("question1", "question2")
        )


        useCase.removeFavorite(vacancy)
        coVerify { repository.deleteFavorite(vacancy) }


    }
}