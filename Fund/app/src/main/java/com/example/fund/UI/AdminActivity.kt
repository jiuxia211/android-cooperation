package com.example.fund.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fund.R
import com.example.fund.RecycleView.OperateAdapter
import com.example.fund.RecycleView.Project
import com.example.fund.Service.ProjectJson
import com.example.fund.Service.ProjectService
import com.example.fund.databinding.AdminBinding
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminActivity : AppCompatActivity() {
    var projectList = ArrayList<Project>()
    var status = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin)
        supportActionBar?.hide()
        //使用viewBinding
        val bingding = AdminBinding.inflate(layoutInflater)
        setContentView(bingding.root)
        //获取token
        val token = intent.getStringExtra("token")
        //加载RecycleView适配器
        val layoutManager = LinearLayoutManager(this)
        bingding.projectre.layoutManager = layoutManager
        val adapter = OperateAdapter(projectList, this, token.toString())
        bingding.projectre.adapter = adapter
        bingding.audit.setOnClickListener {

            initProjectAudit(intent.getStringExtra("token"))
            bingding.projectre.adapter?.notifyDataSetChanged()
        }
        bingding.delete.setOnClickListener {
            initProjectDelete(intent.getStringExtra("token"))
            bingding.projectre.adapter?.notifyDataSetChanged()
        }

    }

    //发送请求返回项目信息
    private fun initProjectAudit(token: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.showUnknown(token.toString()).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
                val responseData = response.body()
                println(response.body().toString())
                if (responseData != null) {
                    projectList.clear()
                    if (responseData.status == 200) {
                        if (responseData.data.item != null) {
                            for (i in 0 until responseData.data.item.size) {
                                projectList.add(
                                    Project(
                                        responseData.data.item[i].title,
                                        "http://" + responseData.data.item[i].pic_path,
                                        responseData.data.item[i].content,
                                        responseData.data.item[i].telephone,
                                        responseData.data.item[i].is_pass,
                                        responseData.data.item[i].fund.toString(),
                                        responseData.data.item[i].pid
                                    )
                                )
                                responseData.msg.showToast(this@AdminActivity)
//                                println(projectList[i].imagePath)
                            }
                        } else {
                            "暂时没有项目哦".showToast(this@AdminActivity)
                        }
                    } else {
                        responseData.msg.showToast(this@AdminActivity)
                    }

                }
            }

            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    private fun initProjectDelete(token: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.showPass(token.toString()).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
                val responseData = response.body()
                println(response.body().toString())
                if (responseData != null) {
                    projectList.clear()
                    if (responseData.status == 200) {
                        if (responseData.data.item != null) {
                            for (i in 0 until responseData.data.item.size) {
                                projectList.add(
                                    Project(
                                        responseData.data.item[i].title,
                                        "http://" + responseData.data.item[i].pic_path,
                                        responseData.data.item[i].content,
                                        responseData.data.item[i].telephone,
                                        responseData.data.item[i].is_pass,
                                        responseData.data.item[i].fund.toString(),
                                        responseData.data.item[i].pid
                                    )
                                )
                                responseData.msg.showToast(this@AdminActivity)
//                                println(projectList[i].imagePath)
                            }
                        } else {
                            "暂时没有项目哦".showToast(this@AdminActivity)
                        }
                    } else {
                        responseData.msg.showToast(this@AdminActivity)
                    }

                }
            }

            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}