package com.example.asm_androidnetworking_huydqph14281.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.asm_androidnetworking_huydqph14281.AppConfig.AppConfig
import com.example.asm_androidnetworking_huydqph14281.Interface.GetClassroom
import com.example.asm_androidnetworking_huydqph14281.R
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.DataClassmateResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClassroomsAdapter(
    private val context: Context?,
    private var dataClass: MutableList<Classroom> = mutableListOf(),
) : RecyclerView.Adapter<ClassroomsAdapter.ClassViewholder>() {

    class ClassViewholder(private val view: View) : RecyclerView.ViewHolder(view) {
        val tvMalop: TextView by lazy { view.findViewById<TextView>(R.id.tvMalop) }
        val tvTenLop: TextView by lazy { view.findViewById<TextView>(R.id.tvTenLop) }
        val imgEditClass: ImageView by lazy { view.findViewById<ImageView>(R.id.imgEditClass) }
        val imgDeleteClass: ImageView by lazy { view.findViewById<ImageView>(R.id.imgDeleteClass) }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewholder {
        val adapter = LayoutInflater.from(parent.context).inflate(R.layout.row_class, parent, false)
        return ClassViewholder(adapter)
    }

    override fun getItemCount() = dataClass.size

    override fun onBindViewHolder(holder: ClassViewholder, position: Int) {
        var item = dataClass[position]
        holder.tvMalop.text = "Ma lop:"+item.maLop
        holder.tvTenLop.text = "Ten lop: "+item.tenLop
        holder.imgEditClass.setOnClickListener {

        }
        holder.imgDeleteClass.setOnClickListener {
            val buider = AlertDialog.Builder(context!!)
            buider.setTitle("Xóa lớp")
            buider.setMessage("Bạn có muốn xóa lớp:" + item.maLop + "?")
                .setPositiveButton("Có") { _, _ ->
                    val client = AppConfig.builder.create(GetClassroom::class.java)
                    var service: Call<DataClassmateResponse> =
                        client.postDelete(item.id, item.maLop, item.tenLop)
                    service.enqueue(object : Callback<DataClassmateResponse> {
                        override fun onResponse(
                            call: Call<DataClassmateResponse>,
                            response: Response<DataClassmateResponse>,
                        ) {
                            if (response.isSuccessful) {

                            } else {
                                return
                            }
                        }

                        override fun onFailure(call: Call<DataClassmateResponse>, t: Throwable) {

                        }
                    })
                    dataClass.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemChanged(holder.adapterPosition)
                    notifyDataSetChanged()
                }.setNegativeButton("Không") { _, _ ->
                }
                .show()
        }
        holder.imgEditClass.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.dialog_edit_class)
            val view: View by lazy { dialog.findViewById<View>(R.id.view) }
            val btnEdit: MaterialButton by lazy { dialog.findViewById<MaterialButton>(R.id.btnEdit) }
            val tilEditIdClass: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilEditIdClass) }
            val tilEditNameClass: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilEditNameClass) }
            tilEditIdClass.editText?.setText(item.maLop)
            tilEditNameClass.editText?.setText(item.tenLop)
            btnEdit.setOnClickListener {
                var tilMaLop = tilEditIdClass.editText?.text.toString()
                var tilTenLop = tilEditNameClass.editText?.text.toString()

                if (tilMaLop.isEmpty()){
                    Toast.makeText(context,"Khong duoc de trong ten lop",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (tilTenLop.isEmpty()){
                    Toast.makeText(context,"Khong duoc de trong ten lop",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                var client = AppConfig.builder.create(GetClassroom::class.java)
                var server: Call<DataClassmateResponse> =
                    client.postEdit(item.id, tilMaLop, tilTenLop)
                server.enqueue(object : Callback<DataClassmateResponse> {
                    override fun onResponse(
                        call: Call<DataClassmateResponse>,
                        response: Response<DataClassmateResponse>,
                    ) {
                        if (response.isSuccessful) {
                            var body = response.body()
                            var result = response.message()
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "loi", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }

                    override fun onFailure(call: Call<DataClassmateResponse>, t: Throwable) {


                    }


                })

                dialog.dismiss()
                dataClass = fetchData()
                notifyDataSetChanged()

            }
            dialog.show()
        }

    }

    private fun fetchData(): MutableList<Classroom> {
        val client = AppConfig.builder.create(GetClassroom::class.java)
        var service: Call<List<Classroom>> = client.getAllClass()
        var list :MutableList<Classroom> = mutableListOf()
        service.enqueue(object : Callback<List<Classroom>> {
            override fun onResponse(
                call: Call<List<Classroom>>,
                response: Response<List<Classroom>>,
            ) {
              val listCalss : List<Classroom>? = response.body()
                for(clasRoom in listCalss!!){
                    list.add(clasRoom)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Classroom>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
            return list

    }


}