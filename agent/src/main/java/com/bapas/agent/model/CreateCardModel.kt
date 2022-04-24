package com.bapas.agent.model

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

data class CreateCardReq(
    var data: ArrayList<CreateCardReqData> = arrayListOf(),
    var cast: RequestBody? = null,
    var family: RequestBody? = null,
    var cardValidity: RequestBody? = null,
    var total: RequestBody? = null,
    var payment_type: RequestBody? = null,
)

data class CreateCardReqData(
    @Part("name") var name: RequestBody,
    @Part("adhar_number") var adhar_number: RequestBody,
    @Part var adhar: MultipartBody.Part,
    @Part var photo: MultipartBody.Part,
    @Part("photoName") var photoName: RequestBody,
    @Part("contact_number") var contact_number: RequestBody,
    @Part("gender") var gender: RequestBody,
    @Part("date_of_born") var date_of_born: RequestBody,
    @Part("location") var location: RequestBody,
)


data class CreateCardRespo(
    var status: Boolean = false,
    var data: CreateCardData? = null,
    var message: String = "",
)

data class CreateCardData(
    var agent_id: String = "",
    var name: String = "",
    var adhar_number: String = "",
    var photo: String = "",
    var adhar: String = "",
    var serial_number: String = "",
    var family: String = "",
    var cast: String = "",
    var contact_number: String = "",
    var type: String = "",
    var uuid: String = "",
    var updated_at: String = "",
    var created_at: String = "",
    var id: String = "",
)