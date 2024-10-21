package com.volkov.listvacancy.domain.mapper

import com.example.core.data.database.model.ButtonModDataBase
import com.example.core.data.database.model.OfferModelDataBase
import com.volkov.listvacancy.data.model.DataModelListVacancyOffer
import com.volkov.listvacancy.domain.model.ListButton
import com.volkov.listvacancy.domain.model.ListOfferDomainModel

class ListVacOfferDataMaper {
    fun mapOfferToDomain(offer: DataModelListVacancyOffer): ListOfferDomainModel {
        return ListOfferDomainModel(
            id = offer.id ?: "",
            title = offer.title ?: "",
            link = offer.link ?: "",
            button = offer.button?.let { ListButton(it.text.toString()) } // Если button существует, маппим его
        )

    }

    fun mapOfferToDomainFromDB(offer: OfferModelDataBase): ListOfferDomainModel {
        return ListOfferDomainModel(
            id = offer.id,
            title = offer.title,
            link = offer.link,
            button = offer.button?.let { ListButton(it.text) } // Если button существует, маппим его
        )

    }

    fun mapOfferToDatabase(offer: ListOfferDomainModel): OfferModelDataBase {
        return OfferModelDataBase(
            id = offer.id ?: "",
            title = offer.title,
            link = offer.link,
            button = offer.button?.let { ButtonModDataBase(it.text) }
        )
    }


}