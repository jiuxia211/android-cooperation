package com.example.fund.Service

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}