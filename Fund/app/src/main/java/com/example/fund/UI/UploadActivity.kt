package com.example.fund.UI

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fund.R
import com.example.fund.Service.ProjectJson
import com.example.fund.Service.ProjectService
import com.example.fund.databinding.UploadBinding
import com.example.fund.showToast
import com.example.fund.utils.URIPathHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class UploadActivity : AppCompatActivity() {
    lateinit var part: MultipartBody.Part
    var pid = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)
        val binding = UploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //获取token
        val token = intent.getStringExtra("token")
        binding.ChoosePicture.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示图片
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
        binding.CreateProject.setOnClickListener {

            //发送请求,获取返回的数据
            if (pid == 0) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:1234/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val projectService = retrofit.create(ProjectService::class.java)
                projectService.createProject(token.toString())
                    .enqueue(object : Callback<ProjectJson> {
                        override fun onResponse(
                            call: Call<ProjectJson>,
                            response: Response<ProjectJson>
                        ) {
                            val responseData = response.body()
                            if (responseData != null) {
                                Log.d("zz", responseData.msg)
                                pid = responseData.data.pid
                                responseData.msg.showToast(this@UploadActivity)
                            }
                        }

                        override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            } else {
                "您已创建项目，请先上传图片和项目信息".showToast(this@UploadActivity)
            }

        }
        binding.UploadFile.setOnClickListener {
            if (pid != 0) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:1234/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val projectService = retrofit.create(ProjectService::class.java)
                projectService.uploadFile(token.toString(), pid, part)
                    .enqueue(object : Callback<ProjectJson> {
                        override fun onResponse(
                            call: Call<ProjectJson>,
                            response: Response<ProjectJson>
                        ) {
                            val responseData = response.body()
                            if (responseData != null) {
                                Log.d("zz", responseData.msg)
                                responseData.msg.showToast(this@UploadActivity)
                            }
                        }

                        override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            } else {
                "请先创建项目".showToast(this@UploadActivity)
            }
        }
        binding.UploadInfo.setOnClickListener {
            //获取输入的标题内容电话
            val title = binding.EditTitle.text.toString()
            val content = binding.EditContent.text.toString()
            val telephone = binding.EditTelephone.text.toString()
            if (pid != 0) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:1234/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val projectService = retrofit.create(ProjectService::class.java)
                projectService.uploadInfo(token.toString(), pid, title, content, telephone)
                    .enqueue(object : Callback<ProjectJson> {
                        override fun onResponse(
                            call: Call<ProjectJson>,
                            response: Response<ProjectJson>
                        ) {
                            val responseData = response.body()
                            if (responseData != null) {
                                Log.d("zz", responseData.msg)
                                responseData.msg.showToast(this@UploadActivity)
                            }
                        }

                        override fun onFailure(call: Call<ProjectJson>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
            } else {
                "请先创建项目".showToast(this@UploadActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        //将选择的图片显示
                        val bitmap = getBitmapFromUri(uri)
                        val picture: ImageView = findViewById(R.id.file)
                        picture.setImageBitmap(bitmap)
                        //获取真实路径
                        val helper = URIPathHelper()
                        val path = helper.getPath(this, uri)
                        //转成MultipartBody.Part
                        val file = File(path.toString())
                        val requestBody = file.asRequestBody("image/jp".toMediaTypeOrNull())
                        part = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    }
                }
            }
        }
    }


    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
}

