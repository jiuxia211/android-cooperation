package com.example.fund.recycleView

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fund.R
import com.example.fund.ui.DetailActivity

class MyProjectAdapter(var projectList: List<Project>, val activity: AppCompatActivity) :
    RecyclerView.Adapter<MyProjectAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val projectTitle: TextView = view.findViewById(R.id.title)
        val projectImage: ImageView = view.findViewById(R.id.projectImage)
        val projectStatus: TextView = view.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_project_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val project = projectList[position]
            val intent = Intent(parent.context, DetailActivity::class.java).apply {
                putExtra("title", project.title)
                putExtra("content", project.content)
                putExtra("imagePath", project.imagePath)
                putExtra("status", project.status)
                putExtra("telephone", project.telephone)
                putExtra("fund", project.fund)
                putExtra("pid", project.pid)
            }
            parent.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projectList[position]
        holder.projectTitle.text = project.title
        Glide.with(activity).load(project.imagePath).into(holder.projectImage)
        if (project.status == "pass") {
            holder.projectStatus.text = "项目状态:通过"
        } else if (project.status == "fail") {
            holder.projectStatus.text = "项目状态:已被驳回"
        } else {
            holder.projectStatus.text = "项目状态:未审核"
        }

    }

    override fun getItemCount() = projectList.size
    
}