package com.bapas.a2s.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bapas.a2s.R
import com.bapas.a2s.activities.ActCardDetails
import com.bapas.a2s.activities.ActHelpCenter
import com.bapas.a2s.databinding.FragProfileBinding

class FragProfile : Fragment() {
    private lateinit var mBind: FragProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.frag_profile, container, false)
        mBind.lnrHelpCenter.setOnClickListener {
            startActivity(Intent(this.requireActivity(), ActHelpCenter::class.java))
        }
        mBind.lnrCardDetails.setOnClickListener {
            startActivity(Intent(this.requireActivity(), ActCardDetails::class.java))

        }
        return mBind.root
    }
}