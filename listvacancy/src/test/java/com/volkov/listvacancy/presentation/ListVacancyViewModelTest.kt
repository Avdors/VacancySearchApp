package com.volkov.listvacancy.presentation


import com.google.common.truth.Truth.assertThat
import com.volkov.listvacancy.domain.model.ListAddressDomainModel
import com.volkov.listvacancy.domain.model.ListButton
import com.volkov.listvacancy.domain.model.ListExperienceDomainModel
import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.domain.model.ListSalaryDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.domain.usecase.FavoriteUseCase
import com.volkov.listvacancy.domain.usecase.ListVacancyUseCase
import com.volkov.listvacancy.presentation.model.AddressMainFragmentModel
import com.volkov.listvacancy.presentation.model.ExperienceMainFragmentModel
import com.volkov.listvacancy.presentation.model.ListVacancyModel
import com.volkov.listvacancy.presentation.model.SalaryMainFragmentModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListVacancyViewModelTest {

    private lateinit var listVacancyUseCase: ListVacancyUseCase
    private lateinit var favoriteUseCase: FavoriteUseCase
    private lateinit var viewModel: ListVacancyViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        listVacancyUseCase = mockk()
        favoriteUseCase = mockk()

        // Устанавливаем поведение для getVacanciesFromDB, если оно вызывается при инициализации
        val fakeVacanciesFlow = flowOf(
            listOf(
                ListVacancyDomainModel(
                    id = "1",
                    lookingNumber = 100,
                    title = "Vacancy 1",
                    address = ListAddressDomainModel("City", "Street", "1"),
                    company = "Company 1",
                    experience = ListExperienceDomainModel("1 year", "2 years"),
                    publishedDate = "2024-10-05",
                    isFavorite = false,
                    salary = ListSalaryDomainModel("1000 USD", "1K"),
                    schedules = emptyList(),
                    appliedNumber = 10,
                    description = "description",
                    responsibilities = "responsibilities",
                    questions = emptyList()
                )
            )
        )
        coEvery { listVacancyUseCase.getVacanciesFromDB() } returns fakeVacanciesFlow

        // Настройка моков для getOffers
        coEvery { listVacancyUseCase.getOffers() } returns listOf(
            ListOfferDomainModel("1", "Offer 1", "link1", ListButton("Button 1"))
        )

        viewModel = ListVacancyViewModel(listVacancyUseCase, favoriteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test loadVacancies successfully`() = runTest {
        advanceUntilIdle()

        // Проверяем, что список вакансий обновился
        val uiVacancies = viewModel.vacancies.value
        assertThat(uiVacancies).isNotEmpty()
        assertThat(uiVacancies[0].id).isEqualTo("1")

        coVerify { listVacancyUseCase.getVacanciesFromDB() }
    }

    @Test
    fun `test loadOffers successfully`() = runTest {
        viewModel.loadOffers()
        advanceUntilIdle()

        val uiOffers = viewModel.offers.value
        assertThat(uiOffers).isNotEmpty()
        assertThat(uiOffers[0].id).isEqualTo("1")

        coVerify { listVacancyUseCase.getOffers() }
    }

    @Test
    fun `test addToFavorites updates favorite status`() = runTest {
        // Создаем фейковую вакансию
        val vacancy = ListVacancyModel(
            id = "1",
            lookingNumber = 100,
            title = "Vacancy 1",
            address = AddressMainFragmentModel("City", "Street", "1"),
            company = "Company 1",
            experience = ExperienceMainFragmentModel("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = false,
            salary = SalaryMainFragmentModel("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )
        viewModel._vacancies.value = listOf(vacancy)

        // Настраиваем поведение для favoriteUseCase
        coEvery { favoriteUseCase.addToFavorite(any()) } just runs

        viewModel.addToFavorites(vacancy)
        advanceUntilIdle()

        val updatedVacancies = viewModel.vacancies.value
        assertThat(updatedVacancies[0].isFavorite).isTrue()

        coVerify { favoriteUseCase.addToFavorite(any()) }
    }
}