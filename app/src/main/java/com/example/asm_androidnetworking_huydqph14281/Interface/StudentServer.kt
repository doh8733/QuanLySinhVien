package com.example.asm_androidnetworking_huydqph14281.Interface

import com.example.asm_androidnetworking_huydqph14281.data.Student
import retrofit2.Call
import retrofit2.http.*

interface StudentServer {
    @GET("student")
    fun getAllStudent(): Call<List<Student>>

    @FormUrlEncoded
    @POST("add-newStudent")
    fun postDataStudent(
        @Field("maSinhVien") maSinhVien : String,
        @Field("maLop") maLop : String,
        @Field("hoTen") hoTen : String,
        @Field("ngaySinh") ngaySinh : String,
        @Field("queQuan") queQuan : String
    ): Call<Student>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("delete-student/{id}")
    fun postDeleteStudent(
        @Path(value = "id") _id: String,
        @Field("maSinhVien") maSinhVien : String,
        @Field("maLop") maLop : String,
        @Field("hoTen") hoTen : String,
        @Field("ngaySinh") ngaySinh : String,
        @Field("queQuan") queQuan : String
    ):Call<Student>

    @FormUrlEncoded
    @POST("edit-student/{id}")
    fun postEditStudent(
        @Path(value = "id") _id: String,
        @Field("maSinhVien") maSinhVien : String,
        @Field("maLop") maLop : String,
        @Field("hoTen") hoTen : String,
        @Field("ngaySinh") ngaySinh : String,
        @Field("queQuan") queQuan : String
    ):Call<Student>
}