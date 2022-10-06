package com.example.asm_androidnetworking_huydqph14281

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asm_androidnetworking_huydqph14281.Adapter.ClassroomsAdapter
import com.example.asm_androidnetworking_huydqph14281.AppConfig.AppConfig
import com.example.asm_androidnetworking_huydqph14281.Interface.GetClassroom
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import com.example.asm_androidnetworking_huydqph14281.ViewModel.ClassViewModel
//import com.example.asm_androidnetworking_huydqph14281.ViewModel.ClassViewModelFactory

class ViewClassActivity : AppCompatActivity() {
    private val rcvClass: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcvClass) }
    //private lateinit var viewModel :ClassViewModel
    private val client = AppConfig.builder.create(GetClassroom::class.java)
    private var service: Call<List<Classroom>> = client.getAllClass()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_class)
        fetchData()

    }
    private fun fetchData(){
        service.enqueue(object : Callback<List<Classroom>>{
            override fun onResponse(
                call: Call<List<Classroom>>,
                response: Response<List<Classroom>>,
            ) {

                if (response.isSuccessful){
                    val list = response.body()
                    rcvClass.layoutManager = LinearLayoutManager(this@ViewClassActivity)
                    rcvClass.adapter = list?.let { ClassroomsAdapter(this@ViewClassActivity,
                        it as MutableList<Classroom>) }
                    rcvClass.adapter!!.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<List<Classroom>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}