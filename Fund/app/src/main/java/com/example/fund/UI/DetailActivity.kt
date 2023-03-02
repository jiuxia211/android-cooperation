package com.example.fund.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fund.R
import com.example.fund.databinding.DetailBinding

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)
        supportActionBar?.hide()
        //使用viewBinding
        val binding = DetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //获取传来的项目数据 并传给控件
        val title = intent.getStringExtra("title")
        val imagePath = intent.getStringExtra("imagePath")
        val content = intent.getStringExtra("content")
        val fund = intent.getStringExtra("fund")
        binding.title.text = title
        binding.content.text = content
        binding.fund.text = fund
        Glide.with(this).load(imagePath).into(binding.image)
    }
}