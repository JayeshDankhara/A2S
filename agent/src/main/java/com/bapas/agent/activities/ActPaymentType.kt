package com.bapas.agent.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bapas.agent.R
import com.bapas.agent.databinding.ActPaymentTypeBinding
import com.bapas.agent.model.AddCardModel
import com.bapas.agent.model.CreateCardReqData
import com.bapas.agent.model.CreateCardRespo
import com.bapas.agent.rest.ApisManager
import com.bapas.agent.utility.Logger
import com.bapas.agent.utility.Utils
import com.bapas.agent.utility.Utils.parseArray
import com.bapas.agent.utility.setSafeOnClickListener
import com.bapas.agent.utility.showDialog
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ActPaymentType : ActAgentBase(), PaymentResultListener {
    private val co = Checkout()
    private var arr: ArrayList<AddCardModel> = arrayListOf()
    private var cast: String = ""
    private var family: String = ""
    private var cardValidity: String = ""
    private var total: String = ""
    private lateinit var mBind: ActPaymentTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_payment_type)


        val ss = intent.getStringExtra("data").toString()


        val type =
            object : com.google.gson.reflect.TypeToken<java.util.ArrayList<AddCardModel>>() {}.type


        arr = parseArray(json = ss, typeToken = type)

        cast = intent.getStringExtra("cast").toString()
        family = intent.getStringExtra("family").toString()
        cardValidity = intent.getStringExtra("cardValidity").toString()
        total = intent.getStringExtra("total").toString()
        initClick()
    }

    private fun initClick() {
        mBind.rbFamily.setOnClickListener {
            startPayment(total)
        }
        mBind.rbCash.setSafeOnClickListener {
            showDialog(
                "Are you want to collect $total/- Rs  Cash",
                "Payment method cash",
                "ok", { i, l ->
                    i.dismiss()
                    PaymentSucessCard("0")
                }, "Cancel", { i, l ->
                    i.dismiss()
                }, 0
            )
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        try {
            val intent = Intent("OpenConf")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            Utils.showToast(this, "Payment Successful: $p0")
            PaymentSucessCard("1")
        } catch (e: Exception) {
            Log.e("tag", "Exception in onPaymentSuccess", e)
        }
    }


    private fun PaymentSucessCard(payment_type: String) {
        val cast1 = (cast).toRequestBody("text/plain".toMediaTypeOrNull())
        val family1 = family.toRequestBody("text/plain".toMediaTypeOrNull())
        val total1 = total.toRequestBody("text/plain".toMediaTypeOrNull())
        val cardValidity1 = cardValidity.toRequestBody("text/plain".toMediaTypeOrNull())
        val paymentType1 = payment_type.toRequestBody("text/plain".toMediaTypeOrNull())
//        val req = CreateCardReq()

        val arrrr: ArrayList<CreateCardReqData> = arrayListOf()



        for (i in 0 until arr.size) {
            val adharUri = Uri.parse(arr[i].adhar)
            val adharPicFile = File(adharUri?.path.toString())

            val photoUri = Uri.parse(arr[i].photo)
            val photoFile = File(photoUri.path.toString())

            val cc = CreateCardReqData(
                arr[i].name.toRequestBody("text/plain".toMediaTypeOrNull()),
                arr[i].adhar_number.toRequestBody("text/plain".toMediaTypeOrNull()),
                MultipartBody.Part.createFormData(
                    "adhar", adharPicFile.name,
                    adharPicFile.asRequestBody("*/*".toMediaTypeOrNull())
                ),
                MultipartBody.Part.createFormData(
                    "adhar", photoFile.name,
                    photoFile.asRequestBody("*/*".toMediaTypeOrNull())
                ),
                arr[i].photoName.toRequestBody("text/plain".toMediaTypeOrNull()),
                arr[i].contact_number.toRequestBody("text/plain".toMediaTypeOrNull()),
                arr[i].gender.toRequestBody("text/plain".toMediaTypeOrNull()),
                arr[i].date_of_born.toRequestBody("text/plain".toMediaTypeOrNull()),
                arr[i].location.toRequestBody("text/plain".toMediaTypeOrNull())
            )
            arrrr.add(cc)
        }

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().createCard(
                    arrrr, cast1, family1, cardValidity1, total1, paymentType1,
                    object : Callback<CreateCardRespo> {
                        override fun onResponse(
                            call: Call<CreateCardRespo>,
                            response: Response<CreateCardRespo>
                        ) {
                            if (response.isSuccessful) {
                                Utils.closeAndroidevLoader()
                                val body = response.body()
                                Logger.e(Gson().toJson(body))
                                if (body != null) {
                                    val data = body.data
                                    if (data == null) {
                                        Utils.showToast(this@ActPaymentType, body.message)
                                    } else {
//                                        clearForm()
                                    }
                                } else {
                                    Utils.showToast(
                                        this@ActPaymentType,
                                        body?.message.toString()
                                    )
                                }
                            } else {
                                Utils.showToast(
                                    this@ActPaymentType,
                                    response.message().toString()
                                )
                                Utils.closeAndroidevLoader()
                            }
                        }

                        override fun onFailure(call: Call<CreateCardRespo>, t: Throwable) {
                            Utils.closeAndroidevLoader()
                            Utils.showToast(this@ActPaymentType, t.message.toString())
                        }
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.e("onDestroy")
    }


/*

    private fun callAPICreate(
        ctx: FragmentActivity,
        name1: RequestBody,
        adhar1: RequestBody,
        family: RequestBody,
        cast1: RequestBody,
        contact_numner: RequestBody,
        adharPic: MultipartBody.Part,
        ProfilePic: MultipartBody.Part,
        location1: RequestBody,
        gender1: RequestBody,
        date1: RequestBody,
        cardValidity1: RequestBody,
        total1: RequestBody,
        payment_type: RequestBody,
    ) {
        Utils.showAndroidevLoader(ctx)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().createCard(
                    name1,
                    adhar1,
                    family,
                    cast1,
                    contact_numner,
                    adharPic,
                    ProfilePic,
                    location1,
                    gender1,
                    date1,
                    cardValidity1, total1, payment_type,
                    object : Callback<CreateCardRespo> {
                        override fun onResponse(
                            call: Call<CreateCardRespo>,
                            response: Response<CreateCardRespo>
                        ) {
                            if (response.isSuccessful) {
                                Utils.closeAndroidevLoader()
                                val body = response.body()
                                Logger.e(Gson().toJson(body))
                                if (body != null) {
                                    val data = body.data
                                    if (data == null) {
                                        Utils.showToast(ctx, body.message)
                                    } else {
//                                        clearForm()
                                    }
                                } else {
                                    Utils.showToast(ctx, body?.message.toString())
                                }
                            } else {
                                Utils.showToast(ctx, response.message().toString())
                                Utils.closeAndroidevLoader()
                            }
                        }

                        override fun onFailure(call: Call<CreateCardRespo>, t: Throwable) {
                            Utils.closeAndroidevLoader()
                            Utils.showToast(ctx, t.message.toString())
                        }
                    })
            }
        }


    }

*/

    override fun onPaymentError(code: Int, response: String?) {
        try {
            Utils.showToast(this, "Payment failed: $code $response")
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Exception in onPaymentError", e)
        }
    }

    fun startPayment(remainAmt: String) {
        co.setKeyID("rzp_test_ExZ4ffICh7s06n")
        try {
            val options = JSONObject()
            co.setImage(R.drawable.ic_avatar)
            options.put("name", getString(R.string.app_name))
            options.put("description", "Hospital Management")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", getDrawable(R.drawable.logo))
            options.put("currency", "INR")
//            options.put("order_id", oid);
            val t = remainAmt.toDouble() * 100
            options.put("amount", t)
            val preFill = JSONObject()
            preFill.put("email", "")
            preFill.put("contact", "")
            options.put("prefill", preFill)
            co.open(this, options)
//            val d = Gson().toJson(options)
//            co.onSuccess(d)
        } catch (e: java.lang.Exception) {
            Utils.closeAndroidevLoader()
            Utils.showToast(this, "Error in payment: " + e.message)
        }
    }


}