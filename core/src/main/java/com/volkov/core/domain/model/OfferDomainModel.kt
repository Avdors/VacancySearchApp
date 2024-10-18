package com.example.core.domain.model

class OfferDomainModel(
    val id: String? = null,  // id может отсутствовать в некоторых объектах, поэтому он nullable
    val title: String,
    val link: String,
    val button: Button? = null // button присутствует не во всех объектах offers
) {

}
class Button(
    val text: String
){

}