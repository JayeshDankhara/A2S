package com.bapas.a2s.rest

import com.bapas.a2s.model.*
import com.bapas.a2s.utility.Utils
import retrofit2.Callback

class ApisManager {
    fun cardList(req: CardListReq, listener: Callback<CardListRespo>) {
        ApiClient.create().cardList("Bearer " + Utils.getUserData()?.api_token, req)
            .enqueue(listener)
    }

    fun getOTP(rq: GetOtpReq, listener: Callback<GetOtpRespo>) {
        ApiClient.create().getOTP(rq).enqueue(listener)
    }

    fun otpVerify(rq: VerifyOtpReq, listener: Callback<VerifyOtpRespo>) {
        ApiClient.create().otpVerify(rq).enqueue(listener)
    }

    fun districts(listener: Callback<DistrictRespo>) {
        ApiClient.create().districts("Bearer " + Utils.getUserData()?.api_token).enqueue(listener)
    }

    fun talukas(rq: TalukasReq, listener: Callback<TalukasRespo>) {
        ApiClient.create().talukas("Bearer " + Utils.getUserData()?.api_token, rq).enqueue(listener)
    }

    fun hospitals(rq: HospitalReq, listener: Callback<HospitalRespo>) {
        ApiClient.create().hospitals("Bearer " + Utils.getUserData()?.api_token, rq)
            .enqueue(listener)
    }


}
