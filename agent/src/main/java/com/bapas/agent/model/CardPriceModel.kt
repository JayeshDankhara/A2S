package com.bapas.agent.model


data class CardPriceReq(
    var card_type: String = "",
    var cast: String = "",
)

data class CardPriceRespo(
    var status: Boolean = false,
    var data: ArrayList<CardPriceData> = arrayListOf(),
    var message: String = "",
)

data class CardPriceData(
    var id: String = "",
    var uuid: String = "",
    var card_type: String = "",
    var year: String = "",
    var charge: String = "",
    var gst: String = "",
    var price: String = "",
    var created_at: String = "",
    var updated_at: String = "",
)