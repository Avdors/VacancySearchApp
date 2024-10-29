package com.volkov.favoritevacancy.domain.mapper

import com.example.core.data.database.model.AddressFModelDataBase
import com.example.core.data.database.model.ExperienceFModelDataBase
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.SalaryFModelDataBase
import com.volkov.favoritevacancy.domain.model.FavoriteAddressDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteExperienceDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteSalaryDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel

class FavoriteVacancyDataMaper {

    fun mapToDomainFromDB(vacancy: FavoriteVacModelDataBase): FavoriteVacancyDomainModel {
        return FavoriteVacancyDomainModel(
            id = vacancy.id,
            lookingNumber = vacancy.lookingNumber ?: 0,
            title = vacancy.title,
            address = FavoriteAddressDomainModel(
                vacancy.address.town,
                vacancy.address.street,
                vacancy.address.house
            ),
            company = vacancy.company,
            experience = FavoriteExperienceDomainModel(
                vacancy.experience.previewText,
                vacancy.experience.text
            ),
            publishedDate = vacancy.publishedDate,
            isFavorite = vacancy.isFavorite,
            salary = FavoriteSalaryDomainModel(
                vacancy.salary.full,
                vacancy.salary.short ?: ""
            ),
            schedules = vacancy.schedules,
            appliedNumber = vacancy.appliedNumber ?: 0,
            description = vacancy.description ?: "",
            responsibilities = vacancy.responsibilities ?: "",
            questions = vacancy.questions.orEmpty()
        )
    }

    // Маппинг доменной модели в модель базы данных
    fun mapToDatabase(vacancy: FavoriteVacancyDomainModel): FavoriteVacModelDataBase {
        return FavoriteVacModelDataBase(
            id = vacancy.id,
            lookingNumber = vacancy.lookingNumber,
            title = vacancy.title,
            address = AddressFModelDataBase(
                vacancy.address.town,
                vacancy.address.street,
                vacancy.address.house
            ),
            company = vacancy.company,
            experience = ExperienceFModelDataBase(
                vacancy.experience.previewText,
                vacancy.experience.text
            ),
            publishedDate = vacancy.publishedDate,
            isFavorite = vacancy.isFavorite,
            salary = SalaryFModelDataBase(vacancy.salary.full, vacancy.salary.short),
            schedules = vacancy.schedules,
            appliedNumber = vacancy.appliedNumber,
            description = vacancy.description,
            responsibilities = vacancy.responsibilities,
            questions = vacancy.questions
        )
    }

}