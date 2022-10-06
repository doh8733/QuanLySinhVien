package com.example.asm_androidnetworking_huydqph14281.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.asm_androidnetworking_huydqph14281.R
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.Student

open class CategoryAdapter(context: Context, resource: Int, objects: MutableList<Classroom>):
    ArrayAdapter<Classroom>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_selected,parent,false)
        val tvSelected : TextView by lazy { convertView.findViewById(R.id.tvSelected) }
        val classroom : Classroom = this.getItem(position)!!
        if (classroom != null){
            tvSelected.text = classroom.maLop
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        val tvViewCategory : TextView by lazy { convertView.findViewById(R.id.tvCategory) }
        val classroom : Classroom = this.getItem(position)!!
        if (classroom != null){
            tvViewCategory.text = classroom.maLop
        }
        return convertView
    }


}