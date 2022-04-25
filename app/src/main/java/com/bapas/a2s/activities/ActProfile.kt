package com.bapas.a2s.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActProfileBinding
import com.bapas.a2s.model.VerifyOtpData
import com.bapas.a2s.utility.Utils
import com.bapas.a2s.utility.setSafeOnClickListener

class ActProfile : ActBase() {
    private var userData: VerifyOtpData? = null
    private lateinit var mBind: ActProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_profile)
        userData = Utils.getUserData()
        setData()
        setEditEnble(true)
        onClickItem()
    }

    private fun setEditEnble(b: Boolean) {
        if(b)
        {
//            mBind.edtAadhar.isEnabled = false
            mBind.edtEmail.isEnabled = false
            mBind.edtMobile.isEnabled = false
//            mBind.edtPanCard.isEnabled = false
            mBind.edtName.isEnabled = false
        }
    }

    private fun onClickItem() {
        mBind.btnBack.setSafeOnClickListener {
            this.onBackPressed()
        }
    }

    private fun setData() {
//        mBind.edtAadhar.setText(userData?.adhar_card)
        mBind.edtEmail.setText(userData?.email)
        mBind.edtMobile.setText(userData?.contact_number)
//        mBind.edtPanCard.setText(userData?.pan_card)
        mBind.edtName.setText(userData?.name)

    }
}