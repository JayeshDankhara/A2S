package com.bapas.a2s.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActCardDetailsBinding

class ActCardDetails : ActBase() {
    private lateinit var mBind: ActCardDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_card_details)
        onClickItem()
    }

    private fun onClickItem() {


    }
}