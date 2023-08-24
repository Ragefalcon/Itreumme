package ru.ragefalcon.tutatores.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.ragefalcon.sharedcode.extensions.MyColorARGB
import ru.ragefalcon.tutatores.extensions.toIntColor


class TypeRasxodAdapter(
    context: Context,
    val objects: List<Pair<String,String>>?,
    val textViewResourceId: Int = android.R.layout.simple_list_item_1,
    val textDropViewResourceId: Int = android.R.layout.simple_list_item_1
) :

    ArrayAdapter<Pair<String,String>?>(context, textViewResourceId, objects!!) {
    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        return getCustomViewDrop(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }


    fun getCustomView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(parent?.context)
        val row: View = inflater.inflate(textViewResourceId, parent, false)// android.R.layout.simple_list_item_1, parent, false)
        val label = row.findViewById(R.id.text1) as TextView
        label.setTextColor(MyColorARGB.MYBEG.toIntColor())
        objects?.let{label.setText(let{objects[position]}.second)}
//        val icon: ImageView = row.findViewById(R.id.icon) as ImageView
//        if (dayOfWeek.get(position) === "Котопятница"
//            || dayOfWeek.get(position) === "Субкота"
//        ) {
//            icon.setImageResource(R.drawable.paw_on)
//        } else {
//            icon.setImageResource(R.drawable.ic_launcher)
//        }
        return row
    }

    fun getCustomViewDrop(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        val inflater = LayoutInflater.from(parent?.context)
        val row: View = inflater.inflate(textDropViewResourceId, parent, false)// android.R.layout.simple_list_item_1, parent, false)
        val label = row.findViewById(R.id.text1) as TextView
        objects?.let{label.setText(let{objects[position]}.second)}
        return row
    }
}