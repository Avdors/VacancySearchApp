package com.volkov.cardvacancy.data.repository

import com.example.core.data.database.dao.FavoritesDao
import com.example.core.data.database.dao.VacancyDao
import com.example.core.data.database.model.AddressFModelDataBase
import com.example.core.data.database.model.AddressModelDataBase
import com.example.core.data.database.model.ExperienceFModelDataBase
import com.example.core.data.database.model.ExperienceModelDataBase
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.SalaryFModelDataBase
import com.example.core.data.database.model.SalaryModelDataBase
import com.example.core.data.database.model.VacancyModelDataBase
import com.volkov.cardvacancy.data.mapper.CardVacancyMapper
import com.volkov.cardvacancy.domain.model.CardDomainAddressModel
import com.volkov.cardvacancy.domain.model.CardDomainExperienceModel
import com.volkov.cardvacancy.domain.model.CardDomainSalaryModel
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test

class CardVacancyRepositoryImplTest {

    private lateinit var vacancyDao: VacancyDao
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var vacancyMapper: CardVacancyMapper
    private lateinit var repository: CardVacancyRepositoryImpl

    @Before
    fun setUp() {
        vacancyDao = mockk()
        favoritesDao = mockk()
        vacancyMapper = mockk()
        repository = CardVacancyRepositoryImpl(vacancyDao, favoritesDao, vacancyMapper)
    }

    @Test
    fun `toggleFavorite adds to favorites when isFavorite is true`() = runTest {
        // Arrange
        val vacancyModel = CardVacancyDomainModel(
            id = "1",
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
        val favorite = FavoriteVacModelDataBase(
            id = vacancyModel.id,
            lookingNumber = vacancyModel.lookingNumber,
            title = vacancyModel.title,
            address = AddressFModelDataBase(
                vacancyModel.address.town,
                vacancyModel.address.street,
                vacancyModel.address.house
            ),
            company = vacancyModel.company,
            experience = ExperienceFModelDataBase(
                vacancyModel.experience.previewText,
                vacancyModel.experience.text
            ),
            publishedDate = vacancyModel.publishedDate,
            isFavorite = vacancyModel.isFavorite,
            salary = SalaryFModelDataBase(vacancyModel.salary.short, vacancyModel.salary.full),
            schedules = vacancyModel.schedules,
            appliedNumber = vacancyModel.appliedNumber,
            description = vacancyModel.description,
            responsibilities = vacancyModel.responsibilities,
            questions = vacancyModel.questions
        )

        coEvery { vacancyMapper.mapToFavoriteModel(vacancyModel) } returns favorite
        coEvery { favoritesDao.upsertItem(favorite) } returns Unit

        // Act
        repository.toggleFavorite(vacancyModel)

        // Assert
        coVerify { favoritesDao.upsertItem(favorite) }
    }

    @Test
    fun `toggleFavorite removes from favorites when isFavorite is false`() = runTest {
        // Arrange
        val vacancyModel = CardVacancyDomainModel(
            id = "1",
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
        val favorite = FavoriteVacModelDataBase(
            id = vacancyModel.id,
            lookingNumber = vacancyModel.lookingNumber,
            title = vacancyModel.title,
            address = AddressFModelDataBase(
                vacancyModel.address.town,
                vacancyModel.address.street,
                vacancyModel.address.house
            ),
            company = vacancyModel.company,
            experience = ExperienceFModelDataBase(
                vacancyModel.experience.previewText,
                vacancyModel.experience.text
            ),
            publishedDate = vacancyModel.publishedDate,
            isFavorite = vacancyModel.isFavorite,
            salary = SalaryFModelDataBase(vacancyModel.salary.short, vacancyModel.salary.full),
            schedules = vacancyModel.schedules,
            appliedNumber = vacancyModel.appliedNumber,
            description = vacancyModel.description,
            responsibilities = vacancyModel.responsibilities,
            questions = vacancyModel.questions
        )

        coEvery { vacancyMapper.mapToFavoriteModel(vacancyModel) } returns favorite
        coEvery { favoritesDao.delete(favorite) } returns Unit

        // Act
        repository.toggleFavorite(vacancyModel)

        // Assert
        coVerify { favoritesDao.delete(favorite) }
    }

    @Test
    fun `loadVacancyById returns mapped vacancy from VacancyDao`() = runBlocking {
        // Arrange
        val vacancyId = "1"
        val vacancyEntity = VacancyModelDataBase(
            id = vacancyId,
            lookingNumber = 100,
            title = "Vacancy 1",
            address = AddressModelDataBase("City", "Street", "1"),
            company = "Company 1",
            experience = ExperienceModelDataBase("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = true,
            salary = SalaryModelDataBase("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )
        val domainModel = CardVacancyDomainModel(
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

        coEvery { vacancyDao.getVacancyById(vacancyId) } returns vacancyEntity
        coEvery { vacancyMapper.mapToDomain(vacancyEntity) } returns domainModel

        // Act
        val result = repository.loadVacancyById(vacancyId).first()

        // Assert
        assertEquals(domainModel, result)
        coVerify { vacancyDao.getVacancyById(vacancyId) }
        coVerify { vacancyMapper.mapToDomain(vacancyEntity) }
    }

    @Test
    fun `loadVacancyByIdFromFavorites returns mapped vacancy from FavoritesDao`() = runBlocking {
        // Arrange
        val vacancyId = "1"
        val favoriteEntity = FavoriteVacModelDataBase(
            id = vacancyId,
            lookingNumber = 100,
            title = "Vacancy 1",
            address = AddressFModelDataBase("City", "Street", "1"),
            company = "Company 1",
            experience = ExperienceFModelDataBase("1 year", "2 years"),
            publishedDate = "2024-10-05",
            isFavorite = true,
            salary = SalaryFModelDataBase("1000 USD", "1K"),
            schedules = emptyList(),
            appliedNumber = 10,
            description = "description",
            responsibilities = "responsibilities",
            questions = emptyList()
        )
        val domainModel = CardVacancyDomainModel(
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

        coEvery { favoritesDao.getVacancyByIdfromFavorite(vacancyId) } returns favoriteEntity
        coEvery { vacancyMapper.mapToDomainFavorite(favoriteEntity) } returns domainModel

        // Act
        val result = repository.loadVacancyByIdFromFavorites(vacancyId).first()

        // Assert
        assertEquals(domainModel, result)
        coVerify { favoritesDao.getVacancyByIdfromFavorite(vacancyId) }
        coVerify { vacancyMapper.mapToDomainFavorite(favoriteEntity) }
    }
}
