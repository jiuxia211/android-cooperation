package com.example.fund.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fund.R
import com.example.fund.databinding.MyProjectBinding
import com.example.fund.recycleView.MyProjectAdapter
import com.example.fund.recycleView.Project
import com.example.fund.service.ProjectJson
import com.example.fund.service.ProjectService
import com.example.fund.showToast
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyProjectActivity : AppCompatActivity() {
    val projectList = ArrayList<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_project)
        supportActionBar?.hide()
        //使用viewBinding
        val binding = MyProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = intent.getStringExtra("token")
        //加载RecycleView适配器
        val layoutManager = LinearLayoutManager(this)
        binding.projectRecycle.layoutManager = layoutManager
        val adapter = MyProjectAdapter(projectList, this)
        binding.projectRecycle.adapter = adapter
        //通过接口获得项目数据
        runBlocking {
            showMy(token.toString())

        }
        Log.d("zz", "我正准备刷新数据")
        binding.projectRecycle.adapter?.notifyDataSetChanged()
        //下拉刷新
        binding.swipeRefresh.setColorSchemeResources(R.color.purple_500)
        binding.swipeRefresh.setOnRefreshListener {
            showMy(token.toString())
            Log.d("zz", "我正准备刷新数据")
            binding.projectRecycle.adapter?.notifyDataSetChanged()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showMy(token: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:1234/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val projectService = retrofit.create(ProjectService::class.java)
        projectService.showMy(token).enqueue(object : Callback<ProjectJson> {
            override fun onResponse(call: Call<ProjectJson>, response: Response<ProjectJson>) {
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
                            "暂时没有项目哦".showToast(this@MyProjectActivity)
                        }
                    } else {
                        responseData.msg.showToast(this@MyProjectActivity)
                    }

                }

            }

            override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                t.printStackTrace()
            }
        })
        Log.d("zz", "我获取完数据了")
    }
}