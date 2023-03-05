package com.example.fund.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fund.R
import com.example.fund.databinding.UpdataPasswordBinding
import com.example.fund.service.UserJson
import com.example.fund.service.UserService
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdatePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updata_password)
        val binding = UpdataPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val token = intent.getStringExtra("token").toString()
        binding.sendEmail.setOnClickListener {
            send(token)
        }
        binding.updataPassword.setOnClickListener {
            update(
                token,
                binding.editOriginalPassword.text.toString(),
                binding.editNewPassword.text.toString(),
                binding.editToken.text.toString()
            )
        }
    }

    private fun send(token: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)
        userService.send(token)
            .enqueue(object : Callback<UserJson> {
                override fun onResponse(
                    call: Call<UserJson>,
                    response: Response<UserJson>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        if (responseData.status == 200) {
                            responseData.msg.showToast(this@UpdatePasswordActivity)
                        } else {
                            responseData.msg.showToast(this@UpdatePasswordActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun update(
        token: String,
        original_password: String,
        new_password: String,
        code: String
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val userService = retrofit.create(UserService::class.java)
        userService.update(token, original_password, new_password, code)
            .enqueue(object : Callback<UserJson> {
                override fun onResponse(
                    call: Call<UserJson>,
                    response: Response<UserJson>
                ) {
                    val responseData = response.body()
                    if (responseData != null) {
                        if (responseData.status == 200) {
                            responseData.msg.showToast(this@UpdatePasswordActivity)
                            finish()
                        } else {
                            responseData.msg.showToast(this@UpdatePasswordActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}