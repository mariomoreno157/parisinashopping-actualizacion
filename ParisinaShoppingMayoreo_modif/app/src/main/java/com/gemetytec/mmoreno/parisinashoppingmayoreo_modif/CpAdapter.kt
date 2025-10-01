package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CpAdapter (private val datos_cp : List<String>):RecyclerView.Adapter<CpViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CpViewHolder {
        TODO("Not yet implemented")
        val layoutInflater = LayoutInflater.from(parent.context)
        return CpViewHolder(layoutInflater.inflate(R.layout.activity_envio,parent,false))
    }

    override fun onBindViewHolder(holder: CpViewHolder, position: Int) {
        TODO("Not yet implemented")
        val item : String = datos_cp[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = datos_cp.size


}