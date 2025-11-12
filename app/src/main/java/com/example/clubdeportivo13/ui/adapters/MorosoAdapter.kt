package com.example.clubdeportivo13.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo13.data.PersonaMorosa
import com.example.clubdeportivo13.R

class MorosoAdapter(private val morososList: List<PersonaMorosa>) :
    RecyclerView.Adapter<MorosoAdapter.MorosoViewHolder>() {

    inner class MorosoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre_moroso)
        val tvDni: TextView = itemView.findViewById(R.id.tv_dni_moroso)

        fun bind(moroso: PersonaMorosa) {
            tvNombre.text = moroso.nombreCompleto
            tvDni.text = "${moroso.dni}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MorosoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_moroso, parent, false)
        return MorosoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MorosoViewHolder, position: Int) {
        val moroso = morososList[position]
        holder.bind(moroso)
    }

    override fun getItemCount(): Int = morososList.size
}