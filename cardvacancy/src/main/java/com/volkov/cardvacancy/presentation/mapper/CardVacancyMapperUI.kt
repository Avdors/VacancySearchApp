package com.volkov.cardvacancy.presentation.mapper

import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.presentation.model.CardAddressModel
import com.volkov.cardvacancy.presentation.model.CardExperienceModel
import com.volkov.cardvacancy.presentation.model.CardSalaryModel
import com.volkov.cardvacancy.presentation.model.CardVacancyModel

object CardVacancyMapperUI {

    // Маппинг доменной модели в UI модель для отображения в карточке вакансии
    fun mapToUIModel(domainModel: CardVacancyDomainModel): CardVacancyModel {
        return CardVacancyModel(
            id = domainModel.id,
            lookingNumber = domainModel.lookingNumber,
            title = domainModel.title,
            address = CardAddressModel(
                domainModel.address.town,
                domainModel.address.street,
                domainModel.address.house
            ),
            company = domainModel.company,
            experience = CardExperienceModel(
                domainModel.experience.previewText,
                domainModel.experience.text
            ),
            publishedDate = domainModel.publishedDate,
            isFavorite = domainModel.isFavorite,
            salary = CardSalaryModel(
                domainModel.salary.full,
                domainModel.salary.short
            ),
            schedules = domainModel.schedules,
            appliedNumber = domainModel.appliedNumber,
            description = domainModel.description,
            responsibilities = domainModel.responsibilities,
            questions = domainModel.questions
        )
    }

}