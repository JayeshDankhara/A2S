package com.bapas.agent.rest

import com.bapas.agent.model.*
import com.bapas.agent.utility.Utils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback

class ApisManager {
//    ApiClient.create().logout("Bearer " + Utils.getUserData()?.api_token).enqueue(listener)

    fun login(rq: LoginReq, listener: Callback<LoginRespo>) {
        ApiClient.create().login(rq).enqueue(listener)
    }

    fun cardList(re: CardListReq, listener: Callback<CardListRespo>) {
        ApiClient.create().cardList("Bearer " + Utils.getUserData()?.api_token, re)
            .enqueue(listener)
    }

    fun cardPrice(re: CardPriceReq, listener: Callback<CardPriceRespo>) {
        ApiClient.create().cardPrice("Bearer " + Utils.getUserData()?.api_token, re)
            .enqueue(listener)
    }

    fun createCard(
        name: RequestBody,
        adhar_number: RequestBody,
        family: RequestBody,
        cast: RequestBody,
        contact_number: RequestBody,
        adhar: MultipartBody.Part,
        photo: MultipartBody.Part,
        location: RequestBody,
        gender: RequestBody,
        date: RequestBody,
        cardValidity1: RequestBody,
        total1: RequestBody,
        payment_type: RequestBody,
        listener: Callback<CreateCardRespo>
    ) {
        ApiClient.create().createCard(
            "Bearer " + Utils.getUserData()?.api_token,
            name, adhar_number,
            family, cast, contact_number,
            adhar, photo, location, gender, date, cardValidity1, total1, payment_type
        ).enqueue(listener)
    }

    fun createCard(
        reqData: ArrayList<CreateCardReqData>,
        cast: RequestBody,
        family: RequestBody,
        cardValidity: RequestBody,
        total: RequestBody,
        payment_type: RequestBody,
        listener: Callback<CreateCardRespo>
    ) {
        ApiClient.create().createCard(
            "Bearer " + Utils.getUserData()?.api_token,
            reqData, cast, family, cardValidity, total, payment_type
        ).enqueue(listener)
    }

}
