package com.bapas.agent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bapas.agent.BaseFragment
import com.bapas.agent.R
import com.bapas.agent.adapter.AdapterHealthCard
import com.bapas.agent.databinding.FragAgentCardDetailsBinding
import com.bapas.agent.model.CardListData
import com.bapas.agent.model.CardListReq
import com.bapas.agent.model.CardListRespo
import com.bapas.agent.rest.ApisManager
import com.bapas.agent.utility.Logger
import com.bapas.agent.utility.Utils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragAgentCardDetails : BaseFragment() {
    private lateinit var mBind: FragAgentCardDetailsBinding
    private var adapter: AdapterHealthCard? = null
    private var arrList: ArrayList<CardListData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind =
            DataBindingUtil.inflate(inflater, R.layout.frag_agent_card_details, container, false)
        onClickItem()
        initRCV()
        callAPICardList()
        return mBind.root
    }

    private fun initRCV() {
        mBind.rcvCard.layoutManager =
            LinearLayoutManager(
                this@FragAgentCardDetails.requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        adapter = AdapterHealthCard(arrList) { pos: Int, type ->
        }
        mBind.rcvCard.adapter = adapter

    }


    private fun callAPICardList() {
        val rq = CardListReq()
        rq.user_type = "1"
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
                                    if (data.isNotEmpty()) {
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

    private fun onClickItem() {


    }
}