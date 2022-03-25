package com.bapas.a2s.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bapas.a2s.R
import com.bapas.a2s.databinding.ItemHealthCardBinding


class AdapterHealthCard(
    private var totalWt: ArrayList<String>,
    private val listener: (Int, String) -> Unit
) :
    RecyclerView.Adapter<AdapterHealthCard.MyViewHolder>() {
    lateinit var bind: ItemHealthCardBinding

    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var txtDiamondWeight: TextView = bind.tvName

    }

    fun addAll(arrList: ArrayList<String>) {
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
            val ctx = holder.itemView.context
            txtDiamondWeight.setTextColor(ctx.resources.getColor(R.color.white))
            txtDiamondWeight.text = totalWt[position]
        }
    }

    override fun getItemCount(): Int {
        return totalWt.size
    }
}