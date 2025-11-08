package com.example.bt_password.ui.lists

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.model.Task
import kotlin.random.Random

class TaskAdapter(
    private var tasks: List<Task>,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // 🎨 Danh sách màu nền nhẹ nhàng, ngẫu nhiên
    private val colors = listOf(
        "#FFEBEE", "#F3E5F5", "#E8EAF6", "#E3F2FD",
        "#E0F7FA", "#E0F2F1", "#F1F8E9", "#FFFDE7",
        "#FFF3E0", "#FBE9E7"
    )

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chkDone: CheckBox = view.findViewById(R.id.chkDone)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val root: View = view.findViewById(R.id.rootView)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(tasks[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // Gán dữ liệu
        holder.tvTitle.text = task.title
        holder.tvContent.text = if (task.description.isNotEmpty()) task.description else "No description"
        holder.tvStatus.text = task.status.replaceFirstChar { it.uppercase() }

        // Hiển thị dueDate từ API
        val dueDate = if (!task.dueDate.isNullOrEmpty()) task.dueDate else "No due date"
        holder.tvTime.text = dueDate

        // Đánh dấu checkbox nếu đã hoàn thành
        holder.chkDone.isChecked = task.status.equals("completed", true)

        // Bo góc mềm cho mỗi task item
        val bg = GradientDrawable()
        bg.cornerRadius = 36f // 👈 Bo góc nhẹ
        val color = Color.parseColor(colors[Random.nextInt(colors.size)])
        bg.setColor(color)
        holder.root.background = bg

        // Màu chữ cho trạng thái
        val statusColor = when (task.status.lowercase()) {
            "completed" -> ContextCompat.getColor(holder.itemView.context, R.color.teal_700)
            "pending" -> ContextCompat.getColor(holder.itemView.context, R.color.orange)
            else -> ContextCompat.getColor(holder.itemView.context, R.color.gray)
        }
        holder.tvStatus.setTextColor(statusColor)
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
