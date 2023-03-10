package com.example.fund.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fund.R
import com.example.fund.databinding.OperateBinding
import com.example.fund.recycleView.Project
import com.example.fund.service.ProjectJson
import com.example.fund.service.ProjectService
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OperateActivity : AppCompatActivity() {
    val projectList = ArrayList<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.operate)
        supportActionBar?.hide()
        val binding = OperateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //获取传来的项目数据 并传给控件
        val title = intent.getStringExtra("title")
        val imagePath = intent.getStringExtra("imagePath")
        val content = intent.getStringExtra("content")
        val pid = intent.getIntExtra("pid", 0)
        val token = intent.getStringExtra("token")
        val ispass = intent.getStringExtra("status")
        binding.title.text = title
        binding.content.text = content
        Glide.with(this).load(imagePath).into(binding.image)
        if (ispass == "pass") {
            binding.pass.visibility = View.GONE
            binding.fail.visibility = View.GONE
            binding.delete.visibility = View.VISIBLE
        } else {
            binding.pass.visibility = View.VISIBLE
            binding.fail.visibility = View.VISIBLE
            binding.delete.visibility = View.GONE
        }
        binding.pass.setOnClickListener {
            auditProject(token, "pass", pid)
            finish()
        }
        binding.fail.setOnClickListener {
            auditProject(token, "fail", pid)
            finish()
        }
        binding.delete.setOnClickListener {
            deleteProject(token, pid)
            finish()
        }
    }

    //项目审核
    private fun auditProject(token: String?, ispass: String, pid: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.audit(token.toString(), pid, ispass).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
                val responseData = response.body()
                if (responseData != null) {
                    if (responseData.status == 200) {
                        responseData.msg.showToast(this@OperateActivity)
                    } else {
                        "操作失败".showToast(this@OperateActivity)
                        responseData.msg.showToast(this@OperateActivity)
                    }
                }
            }


            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    private fun deleteProject(token: String?, pid: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.delete(token.toString(), pid).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
                val responseData = response.body()
                if (responseData != null) {
                    if (responseData.status == 200) {
                        responseData.msg.showToast(this@OperateActivity)
                    } else {
                        "操作失败".showToast(this@OperateActivity)
                        responseData.msg.showToast(this@OperateActivity)
                    }
                }
            }


            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

}
