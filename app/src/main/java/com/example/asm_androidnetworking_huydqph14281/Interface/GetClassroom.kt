package com.example.asm_androidnetworking_huydqph14281.Interface

import android.telecom.Call
import com.example.asm_androidnetworking_huydqph14281.data.Classroom
import com.example.asm_androidnetworking_huydqph14281.data.DataClassmateResponse
import retrofit2.http.*

interface GetClassroom {
    @GET("classroom")
    fun getAllClass() : retrofit2.Call<List<Classroom>>

    @FormUrlEncoded
    @POST("add-newClass")
    fun postClass(
        @Field("tenLop") tenLop : String,
        @Field("maLop")  maLop : String
    ) : retrofit2.Call<DataClassmateResponse>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("delete-Class/{id}")
    fun postDelete(
        @Path(value = "id") _id : String,
        @Field("maLop") maLop: String,
        @Field("tenLop") tenLop: String
    ): retrofit2.Call<DataClassmateResponse>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("edit-Class/{id}")
    fun postEdit(
        @Path(value = "id")_id: String,
        @Field("maLop") maLop: String,
        @Field("tenLop") tenLop: String,
    ): retrofit2.Call<DataClassmateResponse>
}