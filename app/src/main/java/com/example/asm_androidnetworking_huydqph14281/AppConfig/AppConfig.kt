package com.example.asm_androidnetworking_huydqph14281.AppConfig

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppConfig {
    const val BASEURL ="https://android-netwking.herokuapp.com/users/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())

    }
    val builder = retrofit.build()
}