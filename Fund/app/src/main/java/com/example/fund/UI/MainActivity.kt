package com.example.fund.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fund.R
import com.example.fund.RecycleView.Project
import com.example.fund.RecycleView.ProjectAdapter
import com.example.fund.Service.ProjectJson
import com.example.fund.Service.ProjectService
import com.example.fund.databinding.ActivityMainzBinding
import com.example.fund.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val projectList = ArrayList<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainz)
        supportActionBar?.hide()
        //使用viewBinding
        val binding = ActivityMainzBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //获取用户数据
        val token = intent.getStringExtra("token")
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val money = intent.getStringExtra("money")
        val type = intent.getStringExtra("class")

        //将数据传给界面
        binding.username.setText(username)
        binding.email.setText("邮箱" + email)
        binding.money.setText("余额" + money)
        if (type == "1") {
            binding.type.setText("一般用户")
        } else if (type == "2") {
            binding.type.setText("提出众筹者")
        } else if (type == "0") {
            binding.type.setText("管理员")
        }
        //通过接口获得项目数据
        initProject(token)
        //加载RecycleView适配器
        val layoutManager = LinearLayoutManager(this)
        binding.projectre.layoutManager = layoutManager
        val adapter = ProjectAdapter(projectList, this)
        binding.projectre.adapter = adapter
        //点击 “项目” 调整为项目布局
        binding.project.setOnClickListener {
            binding.userInterface.visibility = View.GONE
            binding.projectInterface.visibility = View.VISIBLE
            binding.projectre.visibility = View.VISIBLE
        }
        //点击 “用户” 调整为用户布局
        binding.user.setOnClickListener {
            binding.projectInterface.visibility = View.GONE
            binding.projectre.visibility = View.GONE
            binding.userInterface.visibility = View.VISIBLE
        }
        //进入上传项目界面
        binding.upload.setOnClickListener {
            upload(this, token.toString())
        }
        //进入管理员界面
        binding.auditInterface.setOnClickListener {
            if (type != "0") {
                "你不是管理员".showToast(this)
            } else {
                audit(this, token.toString())
            }

        }
        //搜索按钮
        binding.Search.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:1234/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val projectService = retrofit.create(ProjectService::class.java)
            projectService.search(token.toString(), binding.EditSearch.text.toString())
                .enqueue(object : Callback<ProjectJson> {
                    override fun onResponse(
                        call: Call<ProjectJson>,
                        response: Response<ProjectJson>
                    ) {
                        val responseData = response.body()
                        if (responseData != null) {
                            if (responseData.status == 200) {
                                if (responseData.data.item != null) {
                                    projectList.clear()
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
                                    }
                                } else {
                                    "搜索不到项目呢".showToast(this@MainActivity)
                                }
                            } else {
                                responseData.msg.showToast(this@MainActivity)
                            }

                        }

                    }

                    override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            binding.projectre.adapter?.notifyDataSetChanged()
        }

    }

    //发送请求返回项目信息
    private fun initProject(token: String?) {
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
//                                println(projectList[i].imagePath)
                            }
                        } else {
                            "暂时没有项目哦".showToast(this@MainActivity)
                        }
                    } else {
                        responseData.msg.showToast(this@MainActivity)
                    }

                }
            }

            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    companion object {
        //进入上传项目界面
        fun upload(context: Context, token: String) {
            val intent = Intent(context, UploadActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }

        //进入管理员界面
        fun audit(context: Context, token: String) {
            val intent = Intent(context, AdminActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }

    }

}

