package com.example.fund.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fund.R
import com.example.fund.databinding.DetailBinding
import com.example.fund.service.ProjectJson
import com.example.fund.service.ProjectService
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)
        supportActionBar?.hide()
        //使用viewBinding
        val binding = DetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //获取传来的项目数据 并传给控件
        val token = intent.getStringExtra("token").toString()
        binding.title.text = intent.getStringExtra("title")
        binding.content.text = intent.getStringExtra("content")
        binding.fund.text = "已筹集" + intent.getStringExtra("fund") + "元"
        Glide.with(this).load(intent.getStringExtra("imagePath")).into(binding.image)
        binding.contribute.setOnClickListener {
            contribute(token, intent.getIntExtra("pid", 0), binding.editFund.text.toString())
        }
    }

    private fun contribute(token: String, pid: Int, fund: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val projectService = retrofit.create(ProjectService::class.java)
        projectService.contribute(token, pid, fund).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
                val responseData = response.body()
                if (responseData != null) {
                    if (responseData.status == 200) {
                        responseData.msg.showToast(this@DetailActivity)
                    } else {
                        responseData.msg.showToast(this@DetailActivity)
                    }
                }
            }

            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}