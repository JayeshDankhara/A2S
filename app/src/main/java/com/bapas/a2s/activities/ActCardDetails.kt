package com.bapas.a2s.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActCardDetailsBinding
import com.bapas.a2s.model.*
import com.bapas.a2s.rest.ApisManager
import com.bapas.a2s.utility.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActCardDetails : ActBase() {

    private lateinit var mBind: ActCardDetailsBinding

    lateinit var disAdapter: ArrayAdapter<String>
    lateinit var talAdapter: ArrayAdapter<String>
    lateinit var hosAdapter: ArrayAdapter<String>

    private val disArr = ArrayList<String>()
    private val talukaArr = ArrayList<String>()
    private val hosArr = ArrayList<String>()

    private val hasMapDis = HashMap<String, DistrictData>()
    private val hasMapTaluka = HashMap<String, TalukasData>()
    private val hasMapHos = HashMap<String, HospitalData>()

    private var selectedDis: DistrictData? = null
    private var selectedTal: TalukasData? = null
    private var selectedHos: HospitalData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_card_details)
        callApiDistrict()
        setSpinner()
        onClickItem()
    }


    private fun setSpinner() {
        /*  val adapterDis = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, disArr)
          adapterDis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
          mBind.spDis.adapter = adapterDis*/
        mBind.spDis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long
            ) {
                val toString = mBind.spDis.selectedItem.toString()
                selectedDis = hasMapDis[toString]

                callApiGetTaluka(selectedDis?.id.toString())

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }



        mBind.spTal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long
            ) {

                val toString = mBind.spTal.selectedItem.toString()
                selectedTal = hasMapTaluka[toString]

                callApiGetHospital(selectedDis?.id.toString(), selectedTal?.id.toString())

                //                selectedItem = items[position]
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        mBind.spHos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                position: Int,
                id: Long
            ) {
                val toString = mBind.spHos.selectedItem.toString()
                selectedHos = hasMapHos[toString]
                if (selectedHos?.id.toString().isNotEmpty()) {
                    mBind.cardHos.visible()
                } else {
                    mBind.cardHos.gone()
                }
                showHospitalDetails(selectedHos)
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }


    }

    private fun showHospitalDetails(selectedHos: HospitalData?) {
        mBind.tvHosName.text = selectedHos?.name
        mBind.tvHosEmail.text = selectedHos?.email
        mBind.tvHosAddress.text = selectedHos?.address
        mBind.tvHosCell.text = selectedHos?.cell
        mBind.tvHosPhone.text = selectedHos?.phone

    }

    private fun callApiDistrict() {

        this.let { ctx ->
//            Utils.showAndroidevLoader(ctx)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    ApisManager().districts(object : Callback<DistrictRespo> {
                        override fun onResponse(
                            call: Call<DistrictRespo>,
                            response: Response<DistrictRespo>
                        ) {
                            if (response.isSuccessful) {
                                Utils.closeAndroidevLoader()
                                val body = response.body()
                                Logger.e(Gson().toJson(body))
                                if (body != null) {
                                    val data = body.data

                                    hasMapDis.clear()
                                    disArr.clear()

                                    disArr.add("Select City")
                                    hasMapDis.put("Select City", DistrictData())

                                    if (data != null && data.isNotEmpty()) {


                                        for (i in 0 until data.size) {
                                            val obj = data[i]
                                            disArr.add(obj.name)
                                            hasMapDis.put(obj.name, obj)
                                        }

                                        disAdapter = ArrayAdapter(
                                            this@ActCardDetails,
                                            R.layout.item_spinner_small_size,
                                            disArr
                                        )
                                        mBind.spDis.adapter = disAdapter

                                    }
                                } else {
                                    Utils.showToast(ctx, body?.message.toString())
                                }
                            } else {
                                Utils.showToast(ctx, response.message().toString())
                                Utils.closeAndroidevLoader()
                            }
                        }

                        override fun onFailure(call: Call<DistrictRespo>, t: Throwable) {
                            Utils.closeAndroidevLoader()
                            Utils.showToast(ctx, t.message.toString())
                        }
                    })
                }
            }
        }
    }

    private fun callApiGetTaluka(disId: String) {
        val req = TalukasReq(disId)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().talukas(req, object : Callback<TalukasRespo> {
                    override fun onResponse(
                        call: Call<TalukasRespo>,
                        response: Response<TalukasRespo>
                    ) {
                        if (response.isSuccessful) {
                            Utils.closeAndroidevLoader()
                            val body = response.body()
                            if (body != null) {
                                val data = body.data

                                hasMapTaluka.clear()
                                talukaArr.clear()

                                talukaArr.add("Select Taluka")
                                hasMapDis.put("Select Taluka", DistrictData())

                                if (data != null && data.isNotEmpty()) {


                                    for (i in 0 until data.size) {
                                        val obj = data[i]
                                        talukaArr.add(obj.name)
                                        hasMapTaluka.put(obj.name, obj)
                                    }

                                    talAdapter = ArrayAdapter(
                                        this@ActCardDetails,
                                        R.layout.item_spinner_small_size,
                                        talukaArr
                                    )
                                    mBind.spTal.visible()
                                    mBind.spTal.adapter = talAdapter

                                } else {
                                    mBind.spTal.gone()
                                    mBind.spHos.gone()
                                    mBind.cardHos.gone()
                                }
                            } else {
                                Utils.showToast(this@ActCardDetails, body?.message.toString())
                            }
                        } else {
                            Utils.showToast(this@ActCardDetails, response.message().toString())
                            Utils.closeAndroidevLoader()
                        }
                    }

                    override fun onFailure(call: Call<TalukasRespo>, t: Throwable) {
                        Utils.closeAndroidevLoader()
                        Utils.showToast(this@ActCardDetails, t.message.toString())
                    }
                })
            }
        }
    }

    private fun callApiGetHospital(disId: String, talId: String) {
        val req = HospitalReq(disId, talId)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().hospitals(req, object : Callback<HospitalRespo> {
                    override fun onResponse(
                        call: Call<HospitalRespo>,
                        response: Response<HospitalRespo>
                    ) {
                        if (response.isSuccessful) {
                            Utils.closeAndroidevLoader()
                            val body = response.body()
                            Logger.e(Gson().toJson(body))
                            if (body != null) {
                                val data = body.data

                                hosArr.clear()
                                hasMapHos.clear()

                                hosArr.add("Select Hospital")
                                hasMapHos["Select Hospital"] = HospitalData()

                                if (data != null && data.isNotEmpty()) {
                                    for (i in 0 until data.size) {
                                        val obj = data[i]
                                        hosArr.add(obj.name)
                                        hasMapHos.put(obj.name, obj)
                                    }

                                    hosAdapter = ArrayAdapter(
                                        this@ActCardDetails,
                                        R.layout.item_spinner_small_size,
                                        hosArr
                                    )
                                    mBind.spHos.visible()
                                    mBind.spHos.adapter = hosAdapter

                                } else {
                                    mBind.spHos.gone()
                                    mBind.cardHos.gone()
                                }
                            } else {
                                Utils.showToast(this@ActCardDetails, body?.message.toString())
                            }
                        } else {
                            Utils.showToast(this@ActCardDetails, response.message().toString())
                            Utils.closeAndroidevLoader()
                        }
                    }

                    override fun onFailure(call: Call<HospitalRespo>, t: Throwable) {
                        Utils.closeAndroidevLoader()
                        Utils.showToast(this@ActCardDetails, t.message.toString())
                    }
                })
            }
        }

    }


    private fun onClickItem() {
        mBind.imgBack.setSafeOnClickListener {
            this.onBackPressed()
        }

    }
}