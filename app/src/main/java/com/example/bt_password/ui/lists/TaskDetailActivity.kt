package com.example.bt_password.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.api.RetrofitClient
import com.example.bt_password.data.model.ApiResponse
import com.example.bt_password.data.model.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailDesc: TextView
    private lateinit var tvDetailCategory: TextView
    private lateinit var tvDetailStatus: TextView
    private lateinit var tvDetailPriority: TextView
    private lateinit var tvDetailDue: TextView
    private lateinit var rvSubtasks: RecyclerView
    private lateinit var rvAttachments: RecyclerView
    private lateinit var btnBack: ImageView
    private lateinit var btnDelete: ImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var subtaskAdapter: SubtaskAdapter
    private lateinit var attachmentAdapter: AttachmentAdapter

    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // Ánh xạ View
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailDesc = findViewById(R.id.tvDetailDesc)
        tvDetailCategory = findViewById(R.id.tvDetailCategory)
        tvDetailStatus = findViewById(R.id.tvDetailStatus)
        tvDetailPriority = findViewById(R.id.tvDetailPriority)
        tvDetailDue = findViewById(R.id.tvDetailDue)
        rvSubtasks = findViewById(R.id.rvSubtasks)
        rvAttachments = findViewById(R.id.rvAttachments)
        btnBack = findViewById(R.id.btnBack)
        btnDelete = findViewById(R.id.btnDelete)
        progressBar = findViewById(R.id.progressBar)

        // Lấy Task ID từ Intent
        taskId = intent.getIntExtra("TASK_ID", -1)
        Log.d("TaskDetail", "Task ID nhận được: $taskId")
        if (taskId == -1) {
            Toast.makeText(this, "Không tìm thấy Task ID!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cấu hình RecyclerView
        rvSubtasks.layoutManager = LinearLayoutManager(this)
        rvAttachments.layoutManager = LinearLayoutManager(this)
        subtaskAdapter = SubtaskAdapter(emptyList())
        attachmentAdapter = AttachmentAdapter(emptyList())
        rvSubtasks.adapter = subtaskAdapter
        rvAttachments.adapter = attachmentAdapter

        // Sự kiện
        btnBack.setOnClickListener { finish() }
        btnDelete.setOnClickListener { deleteTask() }

        // Tải dữ liệu chi tiết
        loadTaskDetail()
    }

    private fun loadTaskDetail() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getTaskById(taskId)
            .enqueue(object : Callback<ApiResponse<Task>> {
                override fun onResponse(
                    call: Call<ApiResponse<Task>>,
                    response: Response<ApiResponse<Task>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val body = response.body()
                        val task = body?.data
                        if (task != null) {
                            updateUI(task)
                        } else {
                            Toast.makeText(
                                this@TaskDetailActivity,
                                "Không có dữ liệu task!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@TaskDetailActivity,
                            "Lỗi tải dữ liệu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Task>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("TaskDetail", "❌ Lỗi API: ${t.message}")
                    Toast.makeText(this@TaskDetailActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun updateUI(task: Task) {
        tvDetailTitle.text = task.title
        tvDetailDesc.text = task.description
        tvDetailCategory.text = task.category ?: "N/A"
        tvDetailStatus.text = task.status
        tvDetailPriority.text = task.priority ?: "N/A"
        tvDetailDue.text = "Due: ${task.dueDate ?: "Không rõ"}"

        // Cập nhật subtasks & attachments
        subtaskAdapter.updateData(task.subtasks ?: emptyList())
        attachmentAdapter.updateData(task.attachments ?: emptyList())
    }

    private fun deleteTask() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.deleteTask(taskId)
            .enqueue(object : Callback<ApiResponse<Void>> {
                override fun onResponse(
                    call: Call<ApiResponse<Void>>,
                    response: Response<ApiResponse<Void>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@TaskDetailActivity,
                            "Xóa thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@TaskDetailActivity,
                            "Không thể xóa!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@TaskDetailActivity,
                        "Lỗi: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
