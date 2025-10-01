package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import android.media.Image
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.databinding.ActivityEnvioBinding

class CpViewHolder(view: View):RecyclerView.ViewHolder(view){
    private lateinit var binding : ActivityEnvioBinding
    fun bind(datos_cp: String){

        binding.edttxtDatosAdic.setText(datos_cp)
      //  binding.RecViewEst.add
    }
}
