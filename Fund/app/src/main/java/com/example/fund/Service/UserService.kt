package com.example.fund.service

import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("api/v1/user/login")
    fun login(
        @Field("user_name") username: String, @Field("password") password: String
    ): Call<UserJson>

    @FormUrlEncoded
    @POST("api/v1/user/register")
    fun register(
        @Field("user_name") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("class") type: String
    ): Call<UserJson>

    @POST("api/v1/user/send-email")
    fun send(@Header("Authorization") token: String): Call<UserJson>

    @FormUrlEncoded
    @PUT("api/v1/user/update/password")
    fun update(
        @Header("Authorization") token: String,
        @Field("original_password") original_password: String,
        @Field("new_password") new_password: String,
        @Field("token") code: String
    ): Call<UserJson>
}