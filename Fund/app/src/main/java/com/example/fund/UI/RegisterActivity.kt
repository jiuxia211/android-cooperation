package com.example.fund.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fund.R
import com.example.fund.databinding.RegisterBinding
import com.example.fund.service.UserJson
import com.example.fund.service.UserService
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val binding = RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.register.setOnClickListener {
            //获取输入的账号密码邮箱以及用户类型
            val account = binding.EditAccount.text.toString()
            val password = binding.EditPassword.text.toString()
            val email = binding.EditEmail.text.toString()
            var type = 0
            if (binding.rb1.isChecked) {
                type = 1
            } else if (binding.rb2.isChecked) {
                type = 2
            }
            var msg = "获取失败x"
            var status = "获取失败x"
            if (type != 0) {
                //发送请求,获取返回的数据
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:1234/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val userService = retrofit.create(UserService::class.java)
                userService.register(account, password, email, type.toString())
                    .enqueue(object : Callback<UserJson> {
                        override fun onResponse(
                            call: Call<UserJson>,
                            response: Response<UserJson>
                        ) {
                            val responseData = response.body()
                            if (responseData != null) {
                                if (responseData.status == 200) {
                                    responseData.msg.showToast(this@RegisterActivity)
                                    finish()
                                } else {
                                    responseData.msg.showToast(this@RegisterActivity)
                                }
                            }
                        }

                        override fun onFailure(call: Call<UserJson>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            }

        }
    }
}