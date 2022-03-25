package com.bapas.a2s.activities

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActHelpCenterBinding

class ActHelpCenter : ActBase() {
    private lateinit var mBind: ActHelpCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_help_center)
        onClickItem()
    }

    private fun onClickItem() {
        mBind.imgBack.setOnClickListener { this.onBackPressed() }
        mBind.fbChat.setOnClickListener {
            startActivity(Intent(this, ActInbox::class.java))
        }
    }
}