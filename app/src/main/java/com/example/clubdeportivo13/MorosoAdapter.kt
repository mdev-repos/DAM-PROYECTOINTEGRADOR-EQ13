// Archivo: MorosoAdapter.kt
package com.example.clubdeportivo13

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// El constructor recibe la lista de datos que creamos con ClubDataSource
class MorosoAdapter(private val morososList: List<PersonaMorosa>) :
    RecyclerView.Adapter<MorosoAdapter.MorosoViewHolder>() {

    // 1. Define el ViewHolder: Mantiene la referencia a los componentes del item_moroso.xml
    inner class MorosoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre_moroso)
        val tvDni: TextView = itemView.findViewById(R.id.tv_dni_moroso)

        // Función para "conectar" los datos (PersonaMorosa) con la vista
        fun bind(moroso: PersonaMorosa) {
            tvNombre.text = moroso.nombreCompleto
            tvDni.text = "${moroso.dni}"
        }
    }

    // 2. Crea el layout del ítem para cada elemento (se infla item_moroso.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MorosoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_moroso, parent, false)
        return MorosoViewHolder(view)
    }

    // 3. Conecta el objeto de datos con el ViewHolder (llama a la función bind)
    override fun onBindViewHolder(holder: MorosoViewHolder, position: Int) {
        val moroso = morososList[position]
        holder.bind(moroso)
    }

    // 4. Indica cuántos ítems hay en total
    override fun getItemCount(): Int = morososList.size
}