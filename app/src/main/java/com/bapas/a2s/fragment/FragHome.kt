package com.bapas.a2s.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bapas.a2s.R
import com.bapas.a2s.adapter.AdapterHealthCard
import com.bapas.a2s.databinding.FragHomeBinding

class FragHome : Fragment() {
    private lateinit var mBind: FragHomeBinding
    private var adapter: AdapterHealthCard? = null
    private var arrList: ArrayList<String> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.frag_home, container, false)
        initRCV()
        return mBind.root
    }

    private fun initRCV() {
        mBind.rcvCard.layoutManager =
            LinearLayoutManager(
                this@FragHome.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )

        arrList.add("Card 1")
        arrList.add("Card 2")
        arrList.add("Card 3")

        adapter = AdapterHealthCard(arrList) { pos: Int, type ->
        }
        mBind.rcvCard.adapter = adapter

    }


}