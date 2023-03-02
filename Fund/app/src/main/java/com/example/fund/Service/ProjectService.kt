package com.example.fund.Service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ProjectService {
    @POST("api/v1/project/show/pass")
    fun showPass(@Header("Authorization") token: String): Call<ProjectJson>

    @POST("api/v1/project/create")
    fun createProject(@Header("Authorization") token: String): Call<ProjectJson>

    @Multipart
    @PUT("api/v1/project/upload/file/{pid}")
    fun uploadFile(
        @Header("Authorization") token: String,
        @Path("pid") pid: Int,
        @Part file: MultipartBody.Part

    ): Call<ProjectJson>

    @FormUrlEncoded
    @PUT("api/v1/project/upload/info/{pid}")
    fun uploadInfo(
        @Header("Authorization") token: String,
        @Path("pid") pid: Int,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("telephone") telephone: String
    ): Call<ProjectJson>

    @FormUrlEncoded
    @POST("api/v1/project/search")
    fun search(
        @Header("Authorization") token: String,
        @Field("info") info: String
    ): Call<ProjectJson>

    @POST("api/v1/project/show/unknown")
    fun showUnknown(@Header("Authorization") token: String): Call<ProjectJson>

    @FormUrlEncoded
    @POST("api/v1/project/audit/{pid}")
    fun audit(
        @Header("Authorization") token: String,
        @Path("pid") pid: Int,
        @Field("ispass") ispass: String
    ): Call<ProjectJson>

    @DELETE("api/v1/project/delete/{pid}")
    fun delete(
        @Header("Authorization") token: String,
        @Path("pid") pid: Int,
    ): Call<ProjectJson>
}