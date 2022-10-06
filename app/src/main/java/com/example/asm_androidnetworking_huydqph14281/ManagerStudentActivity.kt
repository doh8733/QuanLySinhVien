package com.example.asm_androidnetworking_huydqph14281

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asm_androidnetworking_huydqph14281.Adapter.CategoryAdapter
import com.example.asm_androidnetworking_huydqph14281.Adapter.StudentAdapter
import com.example.asm_androidnetworking_huydqph14281.AppConfig.AppConfig
import com.example.asm_androidnetworking_huydqph14281.Interface.GetClassroom
import com.example.asm_androidnetworking_huydqph14281.Interface.StudentServer
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.Student
import com.example.asm_androidnetworking_huydqph14281.databinding.ActivityManagerStudentBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ManagerStudentActivity : AppCompatActivity() {
    lateinit var categoryAdapter: CategoryAdapter
    private lateinit var stdAdapter: StudentAdapter
    private var listSTD: MutableList<Student> = mutableListOf()
    private val rcvStudent: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcvStudent) }
    private val fabAdNewStudent: FloatingActionButton by lazy { findViewById<FloatingActionButton>(R.id.fabAdNewStudent) }
    private val mList: MutableList<Classroom> = mutableListOf<Classroom>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityManagerStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stdList : MutableList<Student> = mutableListOf()
        fecthStudent().observe(this){
            stdList.addAll(it)
            rcvStudent.layoutManager = LinearLayoutManager(this)
            stdAdapter =StudentAdapter(this,stdList)
            rcvStudent.adapter = stdAdapter
        }
        binding.fabAdNewStudent.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_add_student)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val view: View by lazy { dialog.findViewById<View>(R.id.view) }
            val textView: TextView by lazy { dialog.findViewById<TextView>(R.id.textView) }
            val spnIdClass: Spinner by lazy { dialog.findViewById<Spinner>(R.id.spnIdClass) }
            val tilIDStudent: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilIDStudent) }
            val tilNameStudent: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilNameStudent) }
            val tilDate: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilDate) }
            val tilHomeTown: TextInputLayout by lazy { dialog.findViewById<TextInputLayout>(R.id.tilHomeTown) }
            val btnAddStudent: MaterialButton by lazy { dialog.findViewById<MaterialButton>(R.id.btnAddStudent) }

            fetchData().observe(this) {
                mList.clear()
                mList.addAll(it)
                categoryAdapter = CategoryAdapter(this, R.layout.item_selected, mList)
                categoryAdapter.notifyDataSetChanged()
                spnIdClass.adapter = categoryAdapter

            }
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
                DatePickerDialog(this,
                    datePickerDialog,
                    cal[java.util.Calendar.YEAR],
                    cal[java.util.Calendar.MONTH],
                    cal[java.util.Calendar.DAY_OF_MONTH]).show()

            }

            btnAddStudent.setOnClickListener {
                var classR : Classroom
                val idStd = tilIDStudent.editText?.text.toString()
                val name = tilNameStudent.editText?.text.toString()
                var malop = mList[spnIdClass.selectedItemPosition].maLop
                var date = tilDate.editText?.text.toString()
                val home = tilHomeTown.editText?.text.toString()

                if (idStd.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong ma sinh vien",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (name.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong ma ho ten sinh vien",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (date.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong ngay sinh",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (home.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong que quan",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val client = AppConfig.builder.create(StudentServer::class.java)
                val server: Call<Student> = client.postDataStudent(idStd, malop, name, date, home)

                server.enqueue(object : Callback<Student> {
                    override fun onResponse(call: Call<Student>, response: Response<Student>) {

                        if (response.isSuccessful) {
                            val result = response.body().toString()
                            Toast.makeText(this@ManagerStudentActivity, result, Toast.LENGTH_SHORT).show()
                        }


                    }

                    override fun onFailure(call: Call<Student>, t: Throwable) {

                    }

                })
                fecthStudent().observe(this@ManagerStudentActivity){
                    stdList.clear()
                    stdList.addAll(it)
                    stdAdapter.notifyDataSetChanged()
                    rcvStudent.adapter = stdAdapter
                }
                dialog.dismiss()
            }

            dialog.show()

        }


    }

    private fun fecthStudent() : MutableLiveData<MutableList<Student>> {
        val client = AppConfig.builder.create(StudentServer::class.java)
        var service: Call<List<Student>> = client.getAllStudent()
        val liveStudent : MutableLiveData<MutableList<Student>> = MutableLiveData()
        val listStudent : MutableList<Student> = mutableListOf()
        service.enqueue(object : Callback<List<Student>> {
            override fun onResponse(
                call: Call<List<Student>>,
                response: Response<List<Student>>,
            ) {

                if (response.isSuccessful) {
                    val tempListStudent : List<Student>? = response.body()
                    for (std in tempListStudent!!){
                        listStudent.add(std)

                    }
                    liveStudent.postValue(listStudent)

                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
               Toast.makeText(this@ManagerStudentActivity,"Loi",Toast.LENGTH_SHORT).show()
            }

        })
        return liveStudent
    }

    private fun fetchData(): MutableLiveData<MutableList<Classroom>> {
        val client = AppConfig.builder.create(GetClassroom::class.java)
        var service: Call<List<Classroom>> = client.getAllClass()
        var list: MutableLiveData<MutableList<Classroom>> = MutableLiveData()
        var tempList: MutableList<Classroom> = mutableListOf()
        service.enqueue(object : Callback<List<Classroom>> {
            override fun onResponse(
                call: Call<List<Classroom>>,
                response: Response<List<Classroom>>,
            ) {
                val listCalss: List<Classroom>? = response.body()
                for (clasRoom in listCalss!!) {
                    tempList.add(clasRoom)
                }
                list.postValue(tempList)
            }

            override fun onFailure(call: Call<List<Classroom>>, t: Throwable) {

            }

        })
        return list

    }

}