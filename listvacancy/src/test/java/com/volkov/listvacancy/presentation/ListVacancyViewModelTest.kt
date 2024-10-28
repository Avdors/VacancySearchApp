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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain


import org.junit.After
import org.junit.Before
import org.junit.Test

class ListVacancyViewModelTest {

    // Моки для use case
    private lateinit var listVacancyUseCase: ListVacancyUseCase
    private lateinit var favoriteUseCase: FavoriteUseCase

    // ViewModel
    private lateinit var viewModel: ListVacancyViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        listVacancyUseCase = mockk()
        favoriteUseCase = mockk()

        viewModel = ListVacancyViewModel(listVacancyUseCase, favoriteUseCase)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

    }

    @Test
    fun `test loadVacancies successfully`() = runTest {
        // Создаем фейковые данные для моков
        val fakeVacancies = listOf(
            ListVacancyDomainModel(
                "1",
                100,
                "Vacancy 1",
                ListAddressDomainModel("City", "Street", "1"),
                "Company 1",
                ListExperienceDomainModel("1 year", "2 years"),
                "2024-10-05",
                false,
                ListSalaryDomainModel("1000 USD", "1K"),
                emptyList(),
                10,
                "description",
                "responsibilities",
                emptyList()
            )
        )

        // Задаем поведение для listVacancyUseCase
        coEvery { listVacancyUseCase.getVacancies() } returns fakeVacancies

        // Вызываем метод загрузки вакансий
        viewModel.loadVacancies()

        // Проверяем, что результат успешно загружен и маппинг данных выполнен
        val uiVacancies = viewModel.vacancies.first()
        assertThat(uiVacancies).isNotEmpty()
        assertThat(uiVacancies[0].id).isEqualTo("1")

        // Проверяем, что мок вызван
        coVerify { listVacancyUseCase.getVacancies() }
    }


    @Test
    fun `test loadOffers successfully`() = runTest {
        // Создаем фейковые данные для моков
        val fakeOffers = listOf(
            ListOfferDomainModel("1", "Offer 1", "link1", ListButton("Button 1"))
        )

        // Задаем поведение для listVacancyUseCase
        coEvery { listVacancyUseCase.getOffers() } returns fakeOffers

        // Вызываем метод загрузки предложений
        viewModel.loadOffers()

        // Проверяем, что результат успешно загружен и маппинг данных выполнен
        val uiOffers = viewModel.offers.first()
        assertThat(uiOffers).isNotEmpty()
        assertThat(uiOffers[0].id).isEqualTo("1")

        // Проверяем, что мок вызван
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
        // Задаем начальный список вакансий в ViewModel
        viewModel._vacancies.value = listOf(vacancy)

        // Настраиваем поведение для favoriteUseCase
        coEvery { favoriteUseCase.addToFavorite(any()) } just runs

        // Вызываем метод добавления в избранное
        viewModel.addToFavorites(vacancy)

        // Проверяем, что статус обновился на избранное
        val updatedVacancies = viewModel.vacancies.first()
        assertThat(updatedVacancies[0].isFavorite).isTrue()

        // Проверяем, что useCase вызван с правильными данными
        coVerify { favoriteUseCase.addToFavorite(any()) }
    }
}