package com.bapas.agent.rest

import com.bapas.agent.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("api/v1/login")
    fun login(@Body re: LoginReq): Call<LoginRespo>

    @POST("api/v1/card-list")
    fun cardList(
        @Header("Authorization") header: String,
        @Body user_type: CardListReq
    ): Call<CardListRespo>

    @POST("api/v1/card-price")
    fun cardPrice(
        @Header("Authorization") header: String,
        @Body user_type: CardPriceReq
    ): Call<CardPriceRespo>

    @Multipart
    @POST("api/v1/create-card")
    fun createCard(
        @Header("Authorization") header: String,
        @Part("name") name: RequestBody,
        @Part("adhar_number") adhar_number: RequestBody,
        @Part("family") family: RequestBody,
        @Part("cast") cast: RequestBody,
        @Part("contact_number") contact_number: RequestBody,
        @Part adhar: MultipartBody.Part,
        @Part photo: MultipartBody.Part,
        @Part("location") location: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("date_of_born") date: RequestBody,
        @Part("card_validity") cardValidity1: RequestBody,
        @Part("total") total1: RequestBody,
        @Part("payment_type ") payment_type: RequestBody,
    ): Call<CreateCardRespo>

    @Multipart
    @POST("api/v1/create-individual-card")
    fun createCard(
        @Header("Authorization") header: String,
        @Part("data") reqData: ArrayList<CreateCardReqData>,
        @Part("cast") cast: RequestBody,
        @Part("family") family: RequestBody,
        @Part("cardValidity") cardValidity: RequestBody,
        @Part("total") total: RequestBody,
        @Part("payment_type") payment_type: RequestBody,
    ): Call<CreateCardRespo>
}