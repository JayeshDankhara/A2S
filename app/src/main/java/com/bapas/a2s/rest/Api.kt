package com.bapas.a2s.rest

import com.bapas.a2s.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {
    /*  @POST("api/v1/login")
      fun login(@Body re: LoginReq): Call<LoginRespo>*/

    @POST("api/v1/card-list")
    fun cardList(
        @Header("Authorization") header: String,
        @Body user_type: CardListReq
    ): Call<CardListRespo>


    @POST("api/v1/districts")
    fun districts(@Header("Authorization") header: String): Call<DistrictRespo>

    @POST("api/v1/talukas")
    fun talukas(
        @Header("Authorization") header: String,
        @Body req: TalukasReq
    ): Call<TalukasRespo>

    @POST("api/v1/hospitals")
    fun hospitals(
        @Header("Authorization") header: String,
        @Body req: HospitalReq
    ): Call<HospitalRespo>


    @POST("api/v1/get-otp")
    fun getOTP(@Body rq: GetOtpReq): Call<GetOtpRespo>

    @POST("api/v1/otp-verify")
    fun otpVerify(@Body rq: VerifyOtpReq): Call<VerifyOtpRespo>

/*    @GET("api/admin/faq")
    fun faq(): Call<FaqRespo>

    @GET("api/admin/notification")
    fun notification(
        @Header("Authorization") header: String,
    ): Call<NotificationRespo>*/

/*
    @POST("api/admin/mybooking")
    fun mybooking(
        @Header("Authorization") header: String,
        @Body re: MyBookingReq

    ): Call<MyBookingRespo>
*/


}