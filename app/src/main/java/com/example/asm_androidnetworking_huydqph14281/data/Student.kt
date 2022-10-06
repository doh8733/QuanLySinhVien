package com.example.asm_androidnetworking_huydqph14281.data

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("_id")
     var id: String,
    @SerializedName("maSinhVien")
     var maSinhVien: String,
    @SerializedName("maLop")
     var maLop: String,
    @SerializedName("hoTen")
     var hoTen: String,
    @SerializedName("ngaySinh")
     var ngaySinh: String,
    @SerializedName("queQuan")
     var queQuan: String,
)
