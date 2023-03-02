package com.example.fund.RecycleView

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
import com.example.fund.UI.AuditActivity

class OperateAdapter(
    val projectList: List<Project>,
    val activity: AppCompatActivity,
    val token: String
) :
    RecyclerView.Adapter<OperateAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val projectTitle: TextView = view.findViewById(R.id.title)
        val projectImage: ImageView = view.findViewById(R.id.projectImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val project = projectList[position]
            val intent = Intent(parent.context, AuditActivity::class.java).apply {
                putExtra("title", project.title)
                putExtra("content", project.content)
                putExtra("imagePath", project.imagePath)
                putExtra("status", project.status)
                putExtra("telephone", project.telephone)
                putExtra("fund", project.fund)
                putExtra("pid", project.pid)
                putExtra("token", token)
            }
            parent.context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projectList[position]
        holder.projectTitle.text = project.title
        Glide.with(activity).load(project.imagePath).into(holder.projectImage)
    }

    override fun getItemCount() = projectList.size
}