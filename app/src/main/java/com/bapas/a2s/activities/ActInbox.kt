package com.bapas.a2s.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ActInboxBinding

class ActInbox : ActBase() {
    private lateinit var mBind: ActInboxBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.act_inbox)
        onCliCkItem()
    }

    private fun onCliCkItem() {
        mBind.imgBack.setOnClickListener {
            this.onBackPressed()
        }

    }
}