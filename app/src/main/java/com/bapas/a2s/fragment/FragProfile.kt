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
import com.bapas.a2s.activities.ActLogin
import com.bapas.a2s.activities.ActProfile
import com.bapas.a2s.databinding.FragProfileBinding
import com.bapas.a2s.utility.setSafeOnClickListener
import com.bapas.a2s.utility.showDialog
import com.pixplicity.easyprefs.library.Prefs

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
        mBind.lnrProfile.setSafeOnClickListener {
            startActivity(Intent(this.requireActivity(), ActProfile::class.java))
        }
        mBind.lnrLogout.setSafeOnClickListener {
            this.requireContext().showDialog(
                "Are you want to logout?", "Alert", "Logout",
                { i, i2 ->
                    Prefs.clear()
                    i.dismiss()
                    startActivity(Intent(this.requireActivity(), ActLogin::class.java))
                    this.requireActivity().finishAffinity()

                },
                "Cancel",
                { i, i2 ->
                    i.dismiss()
                },
            )

        }
        return mBind.root
    }
}