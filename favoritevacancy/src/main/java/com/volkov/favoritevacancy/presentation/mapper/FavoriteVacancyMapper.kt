package com.volkov.favoritevacancy.presentation.mapper

import com.volkov.favoritevacancy.domain.model.FavoriteAddressDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteExperienceDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteSalaryDomainModel
import com.volkov.favoritevacancy.domain.model.FavoriteVacancyDomainModel
import com.volkov.favoritevacancy.presentation.model.FavoriteAddressModel
import com.volkov.favoritevacancy.presentation.model.FavoriteExperienceModel
import com.volkov.favoritevacancy.presentation.model.FavoriteSalaryModel
import com.volkov.favoritevacancy.presentation.model.FavoriteVacancyModel

object FavoriteVacancyMapper {
    fun mapToUIModel(domainModel: FavoriteVacancyDomainModel): FavoriteVacancyModel {
        return FavoriteVacancyModel(
            id = domainModel.id,
            lookingNumber = domainModel.lookingNumber,
            title = domainModel.title,
            address = FavoriteAddressModel(
                domainModel.address.town,
                domainModel.address.street,
                domainModel.address.house
            ),
            company = domainModel.company,
            experience = FavoriteExperienceModel(
                domainModel.experience.previewText,
                domainModel.experience.text
            ),
            publishedDate = domainModel.publishedDate,
            isFavorite = domainModel.isFavorite,
            salary = FavoriteSalaryModel(
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

    fun mapToDomainModel(uiModel: FavoriteVacancyModel): FavoriteVacancyDomainModel {
        return FavoriteVacancyDomainModel(
            id = uiModel.id,
            lookingNumber = uiModel.lookingNumber,
            title = uiModel.title,
            address = FavoriteAddressDomainModel(
                uiModel.address.town,
                uiModel.address.street,
                uiModel.address.house
            ),
            company = uiModel.company,
            experience = FavoriteExperienceDomainModel(
                uiModel.experience.previewText,
                uiModel.experience.text
            ),
            publishedDate = uiModel.publishedDate,
            isFavorite = uiModel.isFavorite,
            salary = FavoriteSalaryDomainModel(
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

    fun mapToUIModelList(domainModels: List<FavoriteVacancyDomainModel>): List<FavoriteVacancyModel> {
        return domainModels.map { mapToUIModel(it) }
    }
}