package com.example.fund.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fund.R
import com.example.fund.databinding.MyAccountBinding

class MyAccountAcitivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_account)
        val binding = MyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val token = intent.getStringExtra("token").toString()
        val username = intent.getStringExtra("username").toString()
        val email = intent.getStringExtra("email").toString()
        val money = intent.getStringExtra("money").toString()
        val type = intent.getStringExtra("class").toString()
        //将数据传给界面
        binding.username.setText(username)
        binding.email.setText("邮箱:" + email)
        binding.money.setText("余额:" + money)
        if (type == "1") {
            binding.type.setText("一般用户")
        } else if (type == "2") {
            binding.type.setText("提出众筹者")
        } else if (type == "0") {
            binding.type.setText("管理员")
        }
        binding.updataPassword.setOnClickListener {
            updataPassword(this, token)
        }
    }

    companion object {
        fun updataPassword(
            context: Context,
            token: String,
        ) {
            val intent = Intent(context, UpdatePasswordActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }
    }
}