package com.bapas.a2s.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bapas.a2s.R
import com.bapas.a2s.adapter.AdapterHealthCard
import com.bapas.a2s.databinding.FragHomeBinding
import com.bapas.a2s.model.CardListData
import com.bapas.a2s.model.CardListReq
import com.bapas.a2s.model.CardListRespo
import com.bapas.a2s.rest.ApisManager
import com.bapas.a2s.utility.Logger
import com.bapas.a2s.utility.Utils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragHome : Fragment() {
    private lateinit var mBind: FragHomeBinding
    private var adapter: AdapterHealthCard? = null
    private var arrList: ArrayList<CardListData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DataBindingUtil.inflate(inflater, R.layout.frag_home, container, false)
        initRCV()
        callAPICardList()
        return mBind.root
    }

    private fun initRCV() {
        mBind.rcvCard.layoutManager =
            LinearLayoutManager(
                this@FragHome.requireActivity(), LinearLayoutManager.VERTICAL, false
            )

        adapter = AdapterHealthCard(arrList) { pos: Int, type ->
        }
        mBind.rcvCard.adapter = adapter

    }

    private fun callAPICardList() {
        val rq = CardListReq()
        rq.user_type = "2"
        this.requireActivity().let { ctx ->
            Utils.showAndroidevLoader(ctx)
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    ApisManager().cardList(rq, object : Callback<CardListRespo> {
                        override fun onResponse(
                            call: Call<CardListRespo>,
                            response: Response<CardListRespo>
                        ) {
                            if (response.isSuccessful) {
                                Utils.closeAndroidevLoader()
                                val body = response.body()
                                Logger.e(Gson().toJson(body))
                                if (body != null) {
                                    val data = body.data
                                    if (data != null && data.isNotEmpty()) {
                                        adapter?.addAll(data)
                                    } else Utils.showToast(ctx, body.message)
                                } else {
                                    Utils.showToast(ctx, body?.message.toString())
                                }
                            } else {
                                Utils.showToast(ctx, response.message().toString())
                                Utils.closeAndroidevLoader()
                            }
                        }

                        override fun onFailure(call: Call<CardListRespo>, t: Throwable) {
                            Utils.closeAndroidevLoader()
                            Utils.showToast(ctx, t.message.toString())
                        }
                    })
                }
            }
        }
    }


}