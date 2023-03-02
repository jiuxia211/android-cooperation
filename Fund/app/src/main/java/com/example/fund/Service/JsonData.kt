package com.example.fund.Service

data class UserJson(
    val `data`: Data,
    val error: String,
    val msg: String,
    val status: Int
)


data class ProjectJson(
    val `data`: Data,
    val error: String,
    val msg: String,
    val status: Int
)

data class Data(
    //登录时传回
    val token: String,
    val user: User,
    //展示项目时传回
    val item: List<Item>,
    //创建时传回
    val total: Int,
    val content: String,
    val fund: Int,
    val is_pass: String,
    val pic_path: String,
    val pid: Int,
    val telephone: String,
    val title: String,
    val uid: Int
)

data class User(
    val `class`: Int,
    val email: String,
    val id: String,
    val money: Int,
    val username: String
)

data class Item(
    val content: String,
    val fund: Int,
    val is_pass: String,
    val pic_path: String,
    val pid: Int,
    val title: String,
    val uid: Int,
    val telephone: String
)