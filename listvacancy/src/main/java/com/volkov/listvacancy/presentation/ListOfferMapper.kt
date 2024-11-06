package com.volkov.listvacancy.presentation

import com.volkov.listvacancy.domain.model.ListButton
import com.volkov.listvacancy.domain.model.ListOfferDomainModel
import com.volkov.listvacancy.presentation.model.ButtonModel
import com.volkov.listvacancy.presentation.model.ListOfferModel

object ListOfferMapper {

    fun mapToUIModel(domainModel: ListOfferDomainModel): ListOfferModel {
        return ListOfferModel(
            id = domainModel.id,
            title = domainModel.title,
            link = domainModel.link,
            button = domainModel.button?.let { ButtonModel(it.text) }
        )
    }

    fun mapToDomainModel(uiModel: ListOfferModel): ListOfferDomainModel {
        return ListOfferDomainModel(
            id = uiModel.id,
            title = uiModel.title,
            link = uiModel.link,
            button = uiModel.button?.let { ListButton(it.text) }
        )

    }

    fun mapToUIModelList(domainModels: List<ListOfferDomainModel>): List<ListOfferModel> {
        return domainModels.map { ListOfferMapper.mapToUIModel(it) }
    }
}