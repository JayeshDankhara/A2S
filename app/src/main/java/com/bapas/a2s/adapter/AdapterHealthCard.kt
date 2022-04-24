package com.bapas.a2s.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ItemHealthCardBinding
import com.bapas.a2s.model.CardListData
import com.bapas.a2s.utility.Utils

class AdapterHealthCard(
    private var totalWt: ArrayList<CardListData>,
    private val listener: (Int, String) -> Unit
) :
    RecyclerView.Adapter<AdapterHealthCard.MyViewHolder>() {
    lateinit var bind: ItemHealthCardBinding

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvName: TextView = bind.tvName
        var imgPhoto = bind.imgPhoto
        var tvNumber = bind.tvNumber
        var tvGender = bind.tvGender
        var tvBod = bind.tvBod
        var tvLocation = bind.tvLocation
        var tvCardNumber = bind.tvCardNumber
    }

    fun addAll(arrList: ArrayList<CardListData>) {
        this.totalWt = arrList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        bind = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_health_card, parent, false
        )
        return MyViewHolder(bind.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            val data = totalWt[position]
            val ctx = holder.itemView.context

            tvName.text = data.name
            tvCardNumber.text = data.serial_number
            tvLocation.text = data.location
            tvBod.text = data.date_of_born
            tvGender.text = data.gender
            tvNumber.text = data.contact_number
            Utils.loadImgGlide(ctx, data.photo, imgPhoto)
        }
    }

    override fun getItemCount(): Int {
        return totalWt.size
    }
}