package com.bapas.agent.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bapas.agent.BaseFragment
import com.bapas.agent.BuildConfig
import com.bapas.agent.R
import com.bapas.agent.activities.ActPaymentType
import com.bapas.agent.adapter.AdapterCardDetails
import com.bapas.agent.databinding.FragAgentHomeBinding
import com.bapas.agent.model.AddCardModel
import com.bapas.agent.model.CardPriceData
import com.bapas.agent.model.CardPriceReq
import com.bapas.agent.model.CardPriceRespo
import com.bapas.agent.rest.ApisManager
import com.bapas.agent.utility.Logger
import com.bapas.agent.utility.Utils.showToast
import com.bapas.agent.utility.gone
import com.bapas.agent.utility.imagepicker.ImagePicker
import com.bapas.agent.utility.setSafeOnClickListener
import com.bapas.agent.utility.visible
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FragAgentHome : BaseFragment() {
    private lateinit var mBind: FragAgentHomeBinding

    //    private var mCameraUriPhoto: Uri? = null
//    private var mCameraUriAdhar: Uri? = null
    private var adapter: AdapterCardDetails? = null
    private var CAMERA_IMAGE_REQ_CODE = 101
    private var arrList: ArrayList<AddCardModel> = arrayListOf()
    private var oneYearP = ""
    private var twoYearP = ""
    private var pos: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.frag_agent_home, container, false)
        onClickItem()
        initRCV()
        return mBind.root
    }

    private fun initRCV() {
        arrList.add(AddCardModel("Main Member"))
        mBind.rcvCardDetails.layoutManager =
            LinearLayoutManager(
                this@FragAgentHome.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        adapter = AdapterCardDetails(this, arrList) { pos: Int, type ->
        }
        mBind.rcvCardDetails.adapter = adapter

    }

    private fun callApiPrice(cardType: String, cast: String) {
        val req = CardPriceReq()
        req.card_type = cardType
        req.cast = cast

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                ApisManager().cardPrice(
                    req, object : Callback<CardPriceRespo> {
                        override fun onResponse(
                            call: Call<CardPriceRespo>,
                            response: Response<CardPriceRespo>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()
                                Logger.e(Gson().toJson(body))
                                if (body != null) {
                                    val data1: ArrayList<CardPriceData> = body.data
                                    if (data1.isNotEmpty()) {
                                        mBind.apply {
                                            for (i in 0 until data1.size) {
                                                val pricedata = data1[i]
                                                if (pricedata.year == "1") {
                                                    oneYearP = pricedata.price
                                                    tvOne.text =
                                                        pricedata.charge + " + GST" + pricedata.gst + "%" + " = " + pricedata.price
                                                } else if (pricedata.year == "2") {
                                                    tvTwo.text =
                                                        pricedata.charge + " + GST" + pricedata.gst + "%" + " = " + pricedata.price
                                                    twoYearP = pricedata.price
                                                }
                                            }
                                        }
                                        if (mBind.rbOneY.isChecked) updateTv(oneYearP)
                                        else if (mBind.rbTwoY.isChecked) updateTv(twoYearP)
                                    }
                                }
                            } else {
                                showToast(
                                    this@FragAgentHome.requireActivity(),
                                    response.message().toString()
                                )
                            }
                        }

                        override fun onFailure(call: Call<CardPriceRespo>, t: Throwable) {
                            showToast(this@FragAgentHome.requireActivity(), t.message.toString())
                        }
                    })
            }
        }
    }

    private fun updateTv(price: String) {
        mBind.tvTotal.text = price
    }

    private fun onClickItem() {
        this.requireActivity().let { ctx ->

            mBind.apply {
                btnAddOther.setSafeOnClickListener {
                    if (arrList.size < 7) {
                        arrList.add(AddCardModel("Member " + (arrList.size + 1)))
                        adapter?.notifyDataSetChanged()
                    } else {
                        showToast(ctx, "Add Max Card")
                    }
                }

                rbOneY.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        rbTwoY.isChecked = false
                        updateTv(oneYearP)
                    }
                }
                rbTwoY.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        rbOneY.isChecked = false
                        updateTv(twoYearP)
                    }
                }
                rbBPL.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        if (rbIndi.isChecked) {
                            callApiPrice("individual", "bpl")
                        } else {
                            callApiPrice("family", "bpl")
                        }
                    }
                }
                rbAPL.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        if (rbIndi.isChecked) {
                            callApiPrice("individual", "apl")
                        } else {
                            callApiPrice("family", "apl")
                        }
                    }
                }
                rbOther.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        if (rbIndi.isChecked) {
                            callApiPrice("individual", "other")
                        } else {
                            callApiPrice("family", "other")
                        }
                    }
                }
                rbIndi.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        btnAddOther.gone()
                        when {
                            rbBPL.isChecked -> {
                                callApiPrice("individual", "bpl")
                            }
                            rbAPL.isChecked -> {
                                callApiPrice("individual", "apl")
                            }
                            rbOther.isChecked -> {
                                callApiPrice("individual", "other")
                            }
                        }
                    }
                }
                rbFamily.setOnCheckedChangeListener { p0, p1 ->
                    if (p1) {
                        btnAddOther.visible()
                        when {
                            rbBPL.isChecked -> {
                                callApiPrice("family", "bpl")
                            }
                            rbAPL.isChecked -> {
                                callApiPrice("family", "apl")
                            }
                            rbOther.isChecked -> {
                                callApiPrice("family", "other")
                            }
                        }
                    }
                }


                btnSubmit.setSafeOnClickListener {
                    var cast = ""
                    when {
                        rbAPL.isChecked -> {
                            cast = "apl"
                        }
                        rbBPL.isChecked -> {
                            cast = "bpl"
                        }
                        rbOther.isChecked -> {
                            cast = "other"
                        }
                    }
                    var family = ""
                    when {
                        rbFamily.isChecked -> {
                            family = "family"
                        }
                        rbIndi.isChecked -> {
                            family = "individual"
                        }
                    }
                    var cardValidity = ""
                    when {
                        rbOneY.isChecked -> {
                            cardValidity = "1"
                        }
                        rbTwoY.isChecked -> {
                            cardValidity = "2"
                        }
                    }

                    val total = tvTotal.text.toString()

                    val arr = adapter?.getAllData()
                    if (arr!!.isEmpty()) {
                        return@setSafeOnClickListener
                    } else if (cast.isEmpty()) {
                        showToast(ctx, "Select Cast")
                        return@setSafeOnClickListener
                    } else if (family.isEmpty()) {
                        showToast(ctx, "Select Family Plans")
                        return@setSafeOnClickListener
                    } else if (cardValidity.isEmpty()) {
                        showToast(ctx, "Select Plan Year")
                        return@setSafeOnClickListener
                    } else if (total.isEmpty() || total == "0") {
                        showToast(ctx, "Invalid Amount")
                    } else {

                        val intent = Intent(
                            this@FragAgentHome.requireContext(), ActPaymentType::class.java
                        )

                        intent.putExtra("data", Gson().toJson(arr))
                        intent.putExtra("cast", cast)
                        intent.putExtra("family", family)
                        intent.putExtra("cardValidity", cardValidity)
                        intent.putExtra("total", total)
                        startActivity(intent)
                    }
                }
            }
        }
    }


    private fun clearForm() {
        mBind.apply {
            rbBPL.isChecked = false
            rbAPL.isChecked = false
            rbOther.isChecked = false

            rbIndi.isChecked = false
            rbFamily.isChecked = false
//            rbFamilyPlus.isChecked = false

            /*    edtName.setText("")
                edtAadhar.setText("")
                tvAdharPhoto.text = ""
                tvPhoto.text = ""
                edtLocation.setText("")
                rbGenderMale.isChecked = false
                rbGenderFemale.isChecked = false
    //            rbGenderOther.isChecked = false
                btnDatePicker.setText("")*/
        }
    }

    fun startCamera(type: String, position: Int, ctx: FragAgentHome) {
        this.pos = position
        if (type == "1") CAMERA_IMAGE_REQ_CODE = 101
        else if (type == "2") CAMERA_IMAGE_REQ_CODE = 102

        Dexter.withContext(ctx.requireActivity())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    showPictureDialog(type, ctx)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    ctx.requireActivity().let {
                        showToast(it, it.getString(R.string.missing_permissions))
                    }
                }
            }).check()
    }

    private fun showPictureDialog(type: String, ctx: FragAgentHome) {
        if (type == "1") {
            CAMERA_IMAGE_REQ_CODE = 101
        } else if (type == "2") {
            CAMERA_IMAGE_REQ_CODE = 102
        }
        val pictureDialog = AlertDialog.Builder(ctx.requireActivity())
        val arrOption = arrayOf("Camera", "Gallery")
        pictureDialog.setTitle("Select From")
        pictureDialog.setItems(arrOption) { _, i ->
            if (i == 0) {
                ctx.let {
                    ImagePicker.with(ctx)
                        .cameraOnly()
                        .saveDir(File(it.requireActivity().filesDir, BuildConfig.PROFILE))
                        .setPosition(pos)
                        .maxResultSize(810, 1440)
                        .start(CAMERA_IMAGE_REQ_CODE)
                }
            } else if (i == 1) {
                ImagePicker.with(ctx)
                    .crop()
                    .galleryOnly()
                    .galleryMimeTypes(
                        mimeTypes = arrayOf("image/png", "image/jpg", "image/jpeg", "image/gif")
                    ).setPosition(pos)
                    .maxResultSize(810, 1440)
                    .start(CAMERA_IMAGE_REQ_CODE)
            }
        }
        pictureDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                val pos1 = data.extras!![ImagePicker.EXTRA_POS].toString().toInt()
                when (requestCode) {
                    101 -> {
                        adapter?.addPhoto(uri, pos1)
                    }
                    102 -> {
                        adapter?.addAdhar(uri, pos1)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(this.requireActivity(), ImagePicker.getError(data))
            }
            else -> {
                showToast(this.requireActivity(), "Task Cancelled")
            }
        }
    }
}