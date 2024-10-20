package com.volkov.listvacancy.domain.model

class ListOfferDomainModel(
    val id: String? = null,  // id может отсутствовать в некоторых объектах, поэтому он nullable
    val title: String,
    val link: String,
    val button: ListButton? = null // button присутствует не во всех объектах offers
) {

}

class ListButton(
    val text: String
) {

}