package com.volkov.cardvacancy.data.mapper

import com.example.core.data.database.model.AddressFModelDataBase
import com.example.core.data.database.model.ExperienceFModelDataBase
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.SalaryFModelDataBase
import com.example.core.data.database.model.VacancyModelDataBase
import com.volkov.cardvacancy.domain.model.CardDomainAddressModel
import com.volkov.cardvacancy.domain.model.CardDomainExperienceModel
import com.volkov.cardvacancy.domain.model.CardDomainSalaryModel
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel

class CardVacancyMapper {
    fun mapToDomain(vacancy: VacancyModelDataBase): CardVacancyDomainModel {
        return CardVacancyDomainModel(
            id = vacancy.id,
            lookingNumber = vacancy.lookingNumber ?: 0,
            title = vacancy.title,
            address = CardDomainAddressModel(
                vacancy.address.town,
                vacancy.address.street,
                vacancy.address.house
            ),
            company = vacancy.company,
            experience = CardDomainExperienceModel(
                vacancy.experience.previewText,
                vacancy.experience.text
            ),
            publishedDate = vacancy.publishedDate,
            isFavorite = vacancy.isFavorite,
            salary = CardDomainSalaryModel(vacancy.salary.full, vacancy.salary.short ?: ""),
            schedules = vacancy.schedules,
            appliedNumber = vacancy.appliedNumber ?: 0,
            description = vacancy.description ?: "",
            responsibilities = vacancy.responsibilities ?: "",
            questions = vacancy.questions ?: emptyList()
        )
    }

    fun mapToDomainFavorite(vacancy: FavoriteVacModelDataBase): CardVacancyDomainModel {
        return CardVacancyDomainModel(
            id = vacancy.id,
            lookingNumber = vacancy.lookingNumber ?: 0,
            title = vacancy.title,
            address = CardDomainAddressModel(
                vacancy.address.town,
                vacancy.address.street,
                vacancy.address.house
            ),
            company = vacancy.company,
            experience = CardDomainExperienceModel(
                vacancy.experience.previewText,
                vacancy.experience.text
            ),
            publishedDate = vacancy.publishedDate,
            isFavorite = vacancy.isFavorite,
            salary = CardDomainSalaryModel(vacancy.salary.full, vacancy.salary.short ?: ""),
            schedules = vacancy.schedules,
            appliedNumber = vacancy.appliedNumber ?: 0,
            description = vacancy.description ?: "",
            responsibilities = vacancy.responsibilities ?: "",
            questions = vacancy.questions ?: emptyList()
        )
    }

    fun mapToFavoriteModel(vacancyModel: CardVacancyDomainModel): FavoriteVacModelDataBase {
        return FavoriteVacModelDataBase(
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
            salary = SalaryFModelDataBase(
                vacancyModel.salary.full,
                vacancyModel.salary.short ?: ""
            ),
            schedules = vacancyModel.schedules,
            appliedNumber = vacancyModel.appliedNumber ?: 0,
            description = vacancyModel.description ?: "",
            responsibilities = vacancyModel.responsibilities ?: "",
            questions = vacancyModel.questions ?: emptyList()
        )
    }
}