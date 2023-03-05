package com.example.fund.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fund.R
import com.example.fund.databinding.ActivityMainzBinding
import com.example.fund.recycleView.Project
import com.example.fund.recycleView.ProjectAdapter
import com.example.fund.service.ProjectJson
import com.example.fund.service.ProjectService
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
        val token = intent.getStringExtra("token").toString()
        val username = intent.getStringExtra("username").toString()
        val email = intent.getStringExtra("email").toString()
        val money = intent.getStringExtra("money").toString()
        val type = intent.getStringExtra("class").toString()

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
        binding.projectRecycle.adapter?.notifyDataSetChanged()
        //加载RecycleView适配器
        val layoutManager = LinearLayoutManager(this)
        binding.projectRecycle.layoutManager = layoutManager
        val adapter = ProjectAdapter(projectList, this, token)
        binding.projectRecycle.adapter = adapter
        //点击 “项目” 调整为项目布局
        binding.project.setOnClickListener {
            binding.userInterface.visibility = View.GONE
            binding.projectInterface.visibility = View.VISIBLE
            binding.swipeRefresh.visibility = View.VISIBLE
        }
        //点击 “用户” 调整为用户布局
        binding.user.setOnClickListener {
            binding.projectInterface.visibility = View.GONE
            binding.swipeRefresh.visibility = View.GONE
            binding.userInterface.visibility = View.VISIBLE
        }
        //进入上传项目界面
        binding.upload.setOnClickListener {
            upload(this, token)
        }
        //查看我的项目
        binding.myProject.setOnClickListener {
            myProject(this, token)
        }
        //进入个人中心
        binding.myAccount.setOnClickListener {
            myAccount(this, token, username, email, money, type)
        }
        //进入管理员界面
        binding.admin.setOnClickListener {
            if (type != "0") {
                "你不是管理员".showToast(this)
            } else {
                admin(this, token.toString())
            }

        }
        //下拉刷新
        binding.swipeRefresh.setColorSchemeResources(R.color.purple_500)
        binding.swipeRefresh.setOnRefreshListener {
            initProject(token)
            binding.projectRecycle.adapter?.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }
        //搜索按钮
        binding.search.setOnClickListener {
            search(token.toString(), binding.editSearch.text.toString())
            binding.projectRecycle.adapter?.notifyDataSetChanged()
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

    private fun search(token: String, info: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.search(token, info)
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
    }

    companion object {
        //进入上传项目界面
        fun upload(context: Context, token: String) {
            val intent = Intent(context, UploadActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }

        //进入管理员界面
        fun admin(context: Context, token: String) {
            val intent = Intent(context, AdminActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }

        //进入我的项目界面
        fun myProject(context: Context, token: String) {
            val intent = Intent(context, MyProjectActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }

        fun myAccount(
            context: Context,
            token: String,
            username: String,
            email: String,
            money: String,
            type: String
        ) {
            val intent = Intent(context, MyAccountAcitivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("username", username)
            intent.putExtra("email", email)
            intent.putExtra("money", money)
            intent.putExtra("class", type)
            context.startActivity(intent)
        }

    }

}

