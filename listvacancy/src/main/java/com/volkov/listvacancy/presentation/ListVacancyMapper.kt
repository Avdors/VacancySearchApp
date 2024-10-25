package com.volkov.listvacancy.presentation

import com.volkov.listvacancy.domain.model.ListAddressDomainModel
import com.volkov.listvacancy.domain.model.ListExperienceDomainModel
import com.volkov.listvacancy.domain.model.ListSalaryDomainModel
import com.volkov.listvacancy.domain.model.ListVacancyDomainModel
import com.volkov.listvacancy.presentation.model.AddressMainFragmentModel
import com.volkov.listvacancy.presentation.model.ExperienceMainFragmentModel
import com.volkov.listvacancy.presentation.model.ListVacancyModel
import com.volkov.listvacancy.presentation.model.SalaryMainFragmentModel

object ListVacancyMapper {
    fun mapToUIModel(domainModel: ListVacancyDomainModel): ListVacancyModel {
        return ListVacancyModel(
            id = domainModel.id,
            lookingNumber = domainModel.lookingNumber,
            title = domainModel.title,
            address = AddressMainFragmentModel(
                domainModel.address.town,
                domainModel.address.street,
                domainModel.address.house
            ),
            company = domainModel.company,
            experience = ExperienceMainFragmentModel(
                domainModel.experience.previewText,
                domainModel.experience.text
            ),
            publishedDate = domainModel.publishedDate,
            isFavorite = domainModel.isFavorite,
            salary = SalaryMainFragmentModel(
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

    fun mapToUIModelList(domainModels: List<ListVacancyDomainModel>): List<ListVacancyModel> {
        return domainModels.map { mapToUIModel(it) }
    }

    // Маппинг из UI-модели в доменную модель для работы с use cases
    // если ставим отметку избранного то в мапинг передаем true
    fun mapToDomainModel(
        uiModel: ListVacancyModel,
        isFavoriteVal: Boolean = false
    ): ListVacancyDomainModel {
        val isFavoriteForVacancy = if (isFavoriteVal) {
            true
        } else {
            uiModel.isFavorite
        }

        return ListVacancyDomainModel(
            id = uiModel.id,
            lookingNumber = uiModel.lookingNumber,
            title = uiModel.title,
            address = ListAddressDomainModel(
                uiModel.address.town,
                uiModel.address.street,
                uiModel.address.house
            ),
            company = uiModel.company,
            experience = ListExperienceDomainModel(
                uiModel.experience.previewText,
                uiModel.experience.text
            ),
            publishedDate = uiModel.publishedDate,
            isFavorite = isFavoriteForVacancy,
            salary = ListSalaryDomainModel(
                uiModel.salary.full,
                uiModel.salary.short
            ),
            schedules = uiModel.schedules,
            appliedNumber = uiModel.appliedNumber,
            description = uiModel.description,
            responsibilities = uiModel.responsibilities,
            questions = uiModel.questions
        )
    }
}