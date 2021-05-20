package com.example.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.AddHappyPlaceActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.models.HappyPlaceModel
import com.example.happyplaces.R
import kotlinx.android.synthetic.main.item_happy_place.view.*

open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    /**
     * Infla las vistas de elementos que están diseñadas en el archivo de diseño xml
     *
     * crear un nuevo
     * inicializa algunos campos privados para ser utilizados por RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )
    }

    /**
     * Vincula cada elemento de ArrayList a una vista
     *
     * Se llama cuando RecyclerView necesita un nuevo {@link ViewHolder} del tipo especificado para representar
     * un artículo.
     *
     * Este nuevo ViewHolder debe construirse con una nueva Vista que pueda representar los elementos
     * del tipo dado. Puede crear una nueva vista manualmente o inflarla desde un XML
     * archivo de diseño.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }


      //Obtiene el número de elementos de la lista

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * función para editar el detalle agregado del lugar feliz y pasar los detalles existentes a través de la intención.
     */
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(
            intent,
            requestCode
        ) // La actividad se inicia con requestCode

        notifyItemChanged(position) // Notifique a los observadores registrados que el artículo en la posición ha cambiado.
    }

    /**
     * Una función para eliminar el detalle del lugar feliz agregado del almacenamiento local.
     */
    fun removeAt(position: Int) {

        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     *función para vincular el onclickListener.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    /**
     * Un ViewHolder describe una vista de elemento y metadatos sobre su lugar dentro de RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}