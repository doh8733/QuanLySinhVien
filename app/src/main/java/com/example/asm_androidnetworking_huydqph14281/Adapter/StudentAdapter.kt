package com.example.asm_androidnetworking_huydqph14281.Adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asm_androidnetworking_huydqph14281.AppConfig.AppConfig
import com.example.asm_androidnetworking_huydqph14281.Interface.GetClassroom
import com.example.asm_androidnetworking_huydqph14281.Interface.StudentServer
import com.example.asm_androidnetworking_huydqph14281.R
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.Student
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class StudentAdapter(
    private val context: Context,
    private var listStudent: MutableList<Student> = mutableListOf(),
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private lateinit var categoryAdapter: SpnCategory

    class StudentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val tvMaSv: TextView by lazy { view.findViewById<TextView>(R.id.tvMaSv) }
        val tvMLop: TextView by lazy { view.findViewById<TextView>(R.id.tvMLop) }
        val tvTen: TextView by lazy { view.findViewById<TextView>(R.id.tvTen) }
        val tvDate: TextView by lazy { view.findViewById<TextView>(R.id.tvDate) }
        val tvQueQuan: TextView by lazy { view.findViewById<TextView>(R.id.tvQueQuan) }
        val imgEdit: ImageView by lazy { view.findViewById<ImageView>(R.id.imgEdit) }
        val imgDelete: ImageView by lazy { view.findViewById<ImageView>(R.id.imgDelete) }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val adapter =
            LayoutInflater.from(parent.context).inflate(R.layout.row_student, parent, false)
        return StudentViewHolder(adapter)
    }


    override fun getItemCount() = listStudent.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        var item = listStudent[position]
        holder.tvMaSv.text = item.maSinhVien
        holder.tvMLop.text = item.maLop
        holder.tvDate.text = item.ngaySinh
        holder.tvTen.text = item.hoTen
        holder.tvQueQuan.text = item.queQuan

        holder.imgDelete.setOnClickListener {
            val buider = AlertDialog.Builder(context!!)
            buider.setTitle("Xoa Sinh Vien: " + item.hoTen)
            buider.setMessage("Ban co chac muon xoa: " + item.maSinhVien)
                .setPositiveButton("Co") { _, _ ->
                    val client = AppConfig.builder.create(StudentServer::class.java)
                    val server: Call<Student> = client.postDeleteStudent(item.id,
                        item.maSinhVien,
                        item.maLop,
                        item.hoTen,
                        item.ngaySinh,
                        item.queQuan)
                    server.enqueue(object : Callback<Student> {
                        override fun onResponse(call: Call<Student>, response: Response<Student>) {
                            if (response.isSuccessful) {
                            }
                        }

                        override fun onFailure(call: Call<Student>, t: Throwable) {
                        }

                    })
                    listStudent.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemChanged(holder.adapterPosition)
                    notifyDataSetChanged()
                }.setNegativeButton("Khong") { _, _ ->

                }
                .show()
        }
        holder.imgEdit.setOnClickListener {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_edit_student)
            val view: View by lazy { dialog.findViewById<View>(R.id.view) }
            val textView: TextView by lazy { dialog.findViewById<TextView>(R.id.textView) }
            val spnIdClass: Spinner by lazy { dialog.findViewById<Spinner>(R.id.spnIdClass) }
            val tilIDStudent: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilIDStudent) }
            val tilNameStudent: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilNameStudent) }
            val tilDate: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilDate) }
            val tilHomeTown: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilHomeTown) }
            val btnAddStudent: MaterialButton by lazy { dialog.findViewById<MaterialButton>(R.id.btnAddStudent) }
            val client = AppConfig.builder.create(StudentServer::class.java)

            val cal = java.util.Calendar.getInstance()
            val datePickerDialog =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd-MM-yyyy"
                    val sdf = SimpleDateFormat(myFormat, Locale.UK)
                    tilDate.editText?.setText(sdf.format(cal.time))

                }
            tilDate.editText?.setOnClickListener {
                DatePickerDialog(context,
                    datePickerDialog,
                    cal[java.util.Calendar.YEAR],
                    cal[java.util.Calendar.MONTH],
                    cal[java.util.Calendar.DAY_OF_MONTH]).show()

            }

            categoryAdapter = SpnCategory(context, R.layout.item_selected, listStudent)
            categoryAdapter.notifyDataSetChanged()
            spnIdClass.adapter = categoryAdapter
            tilIDStudent.editText?.setText(item.maSinhVien)
            tilNameStudent.editText?.setText(item.hoTen)
            tilDate.editText?.setText(item.ngaySinh)
            tilHomeTown.editText?.setText(item.queQuan)
            btnAddStudent.setOnClickListener {

                val snp = listStudent[spnIdClass.selectedItemPosition].maLop
                val mSinhVien = tilIDStudent.editText?.text.toString()
                val ten = tilNameStudent.editText?.text.toString()
                val ngaySinh = tilDate.editText?.text.toString()
                val queQuan = tilHomeTown.editText?.text.toString()
                if (mSinhVien.isEmpty()) {
                    Toast.makeText(context, "Khong duoc de trong ma sinh vien", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (ten.isEmpty()) {
                    Toast.makeText(context,
                        "Khong duoc de trong ma ho ten sinh vien",
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (ngaySinh.isEmpty()) {
                    Toast.makeText(context, "Khong duoc de trong ngay sinh", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else if (queQuan.isEmpty()) {
                    Toast.makeText(context, "Khong duoc de trong que quan", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                val server: Call<Student> =
                    client.postEditStudent(item.id, mSinhVien, snp, ten, ngaySinh, queQuan)
                server.enqueue(object : Callback<Student> {
                    override fun onResponse(call: Call<Student>, response: Response<Student>) {
                        if (response.isSuccessful) {
                            var result = response.message()
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Student>, t: Throwable) {
                    }

                })

                listStudent = fecthStudent()
                notifyDataSetChanged()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun fecthStudent(): MutableList<Student> {
        val client = AppConfig.builder.create(StudentServer::class.java)
        var service: Call<List<Student>> = client.getAllStudent()
        val listStudent: MutableList<Student> = mutableListOf()
        service.enqueue(object : Callback<List<Student>> {
            override fun onResponse(
                call: Call<List<Student>>,
                response: Response<List<Student>>,
            ) {

                if (response.isSuccessful) {
                    val tempListStudent: List<Student>? = response.body()
                    for (liststd in tempListStudent!!){
                        listStudent.add(liststd)
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(context, "Loi", Toast.LENGTH_SHORT).show()
            }

        })
        return listStudent
    }

    private fun fetchData() {
        val client = AppConfig.builder.create(StudentServer::class.java)
        var service: Call<List<Student>> = client.getAllStudent()
        service.enqueue(object : Callback<List<Student>> {
            override fun onResponse(
                call: Call<List<Student>>,
                response: Response<List<Student>>,
            ) {

                if (response.isSuccessful) {
                    listStudent = response.body() as MutableList<Student>

                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}


