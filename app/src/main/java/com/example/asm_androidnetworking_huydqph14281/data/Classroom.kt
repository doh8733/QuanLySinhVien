package com.example.asm_androidnetworking_huydqph14281.data

import com.google.gson.annotations.SerializedName

data class Classroom(
    @SerializedName("_id")
    val id: String,
    @SerializedName("tenLop")
    val tenLop: String,
    @SerializedName("maLop")
    val maLop: String
    )

