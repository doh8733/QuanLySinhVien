package com.example.asm_androidnetworking_huydqph14281

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import com.example.asm_androidnetworking_huydqph14281.AppConfig.AppConfig
import com.example.asm_androidnetworking_huydqph14281.Interface.GetClassroom
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.DataClassmateResponse
import com.example.asm_androidnetworking_huydqph14281.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import com.google.android.material.textfield.TextInputLayout as TextInputLayout1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddClass.setOnClickListener {
           val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_add_class)
             val view: View by lazy { dialog.findViewById<View>(R.id.view) }
             val btnAdd: MaterialButton by lazy { dialog.findViewById<MaterialButton>(R.id.btnAdd) }
             val tilIdClass: TextInputLayout1 by lazy { dialog.findViewById<TextInputLayout1>(R.id.tilIdClass) }
             val tilNameClass: TextInputLayout1 by lazy { dialog.findViewById<TextInputLayout1>(R.id.tilNameClass) }
            btnAdd.setOnClickListener{
                var tilTenLop = tilNameClass.editText?.text.toString()
                var tilMalop = tilIdClass.editText?.text.toString()

                if (tilTenLop.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong ten lop",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else if (tilMalop.isEmpty()){
                    Toast.makeText(this,"Khong duoc de trong ten lop",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val clientInterface = AppConfig.builder.create(GetClassroom::class.java)
                var service: Call<DataClassmateResponse> = clientInterface.postClass(tilTenLop,tilMalop)
                service.enqueue(object : Callback<DataClassmateResponse>{
                    override fun onResponse(
                        call: Call<DataClassmateResponse>,
                        response: Response<DataClassmateResponse>,
                    ) {
                        if (response.isSuccessful){
                            val result = response.body().toString()
                            Toast.makeText(this@MainActivity,result,Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<DataClassmateResponse>, t: Throwable) {
                    }

                })
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.btnAddStudent.setOnClickListener {
            intent = Intent(this,ManagerStudentActivity::class.java)
            startActivity(intent)
        }
        binding.btnViewClass.setOnClickListener {
            intent = Intent(this, ViewClassActivity::class.java)
            startActivity(intent)
        }

    }
}