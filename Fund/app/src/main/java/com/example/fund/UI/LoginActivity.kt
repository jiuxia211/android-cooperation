package com.example.fund.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fund.R
import com.example.fund.Service.UserJson
import com.example.fund.Service.UserService
import com.example.fund.databinding.LoginBinding
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        //使用viewBinding
        val binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //登录
        binding.login.setOnClickListener {
            //获取输入的账号密码
            val account = binding.EditAccount.text.toString()
            val password = binding.EditPassword.text.toString()
            //发送请求,获取返回的数据
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:1234/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val userService = retrofit.create(UserService::class.java)
            userService.login(account, password).enqueue(object : Callback<UserJson> {
                override fun onResponse(call: Call<UserJson>, response: Response<UserJson>) {
                    val responseData = response.body()
                    if (responseData != null) {
                        if (responseData.status == 200) {
                            responseData.msg.showToast(this@LoginActivity)
                            login(
                                this@LoginActivity,
                                responseData.data.token,
                                responseData.data.user.username,
                                responseData.data.user.email,
                                responseData.data.user.money.toString(),
                                responseData.data.user.`class`.toString()
                            )
                        } else {
                            responseData.msg.showToast(this@LoginActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<UserJson>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        binding.register.setOnClickListener {
            register(this)
        }
    }

    companion object {
        fun login(
            context: Context,
            token: String,
            username: String,
            email: String,
            money: String,
            type: String
        ) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("money", money)
            intent.putExtra("class", type)
            context.startActivity(intent)
        }

        fun register(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
