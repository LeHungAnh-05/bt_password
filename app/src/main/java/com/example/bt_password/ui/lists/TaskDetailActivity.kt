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

    private var taskId: Int = -1 // 👈 giữ kiểu Int nhất quán

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

        // ✅ Lấy Task ID từ Intent (Int)
        taskId = intent.getIntExtra("TASK_ID", -1)
        Log.d("TaskDetailActivity", "📦 Nhận TASK_ID từ intent: $taskId")

        if (taskId == -1) {
            Toast.makeText(this, "Không tìm thấy Task ID!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cấu hình RecyclerView phụ
        rvSubtasks.layoutManager = LinearLayoutManager(this)
        rvAttachments.layoutManager = LinearLayoutManager(this)
        subtaskAdapter = SubtaskAdapter(emptyList())
        attachmentAdapter = AttachmentAdapter(emptyList())
        rvSubtasks.adapter = subtaskAdapter
        rvAttachments.adapter = attachmentAdapter

        // Sự kiện
        btnBack.setOnClickListener { finish() }
        btnDelete.setOnClickListener { deleteTask(taskId) }

        // Tải chi tiết
        loadTaskDetail(taskId)
    }

    // ✅ Hiển thị dữ liệu chi tiết task
    private fun updateUI(task: Task) {
        tvDetailTitle.text = task.title ?: "No title"
        tvDetailDesc.text = if (task.description.isNullOrEmpty()) "No description" else task.description
        tvDetailCategory.text = task.category ?: "N/A"
        tvDetailStatus.text = task.status ?: "N/A"
        tvDetailPriority.text = task.priority ?: "N/A"
        tvDetailDue.text = "Due: ${task.dueDate ?: "Không rõ"}"

        Log.d("TaskDetailActivity", """
            ✅ Hiển thị Task:
            - ID: ${task.id}
            - Title: ${task.title}
            - Description: ${task.description}
            - Status: ${task.status}
            - Category: ${task.category}
            - Priority: ${task.priority}
            - Due: ${task.dueDate}
        """.trimIndent())
    }

    // ✅ Lấy chi tiết task từ danh sách chung
    private fun loadTaskDetail(id: Int) {
        Log.d("TaskDetailActivity", "🔄 Bắt đầu tải chi tiết task ID=$id ...")
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getTasks().enqueue(object : Callback<ApiResponse<List<Task>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Task>>>,
                response: Response<ApiResponse<List<Task>>>
            ) {
                progressBar.visibility = View.GONE
                Log.d("TaskDetailActivity", "📥 API trả về mã ${response.code()}")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("TaskDetailActivity", "🌐 Dữ liệu trả về: $apiResponse")

                    val tasks = apiResponse?.data ?: emptyList()
                    Log.d("TaskDetailActivity", "📋 Tổng số task: ${tasks.size}")

                    val found = tasks.find { it.id == id }

                    if (found != null) {
                        updateUI(found)
                    } else {
                        Log.w("TaskDetailActivity", "⚠️ Không tìm thấy task ID=$id trong danh sách")
                        Toast.makeText(this@TaskDetailActivity, "Không tìm thấy task!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TaskDetailActivity", "❌ Lỗi phản hồi API: ${response.errorBody()?.string()}")
                    Toast.makeText(this@TaskDetailActivity, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Task>>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("TaskDetailActivity", "❌ Lỗi kết nối: ${t.message}")
                Toast.makeText(this@TaskDetailActivity, "Không thể tải dữ liệu: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ✅ Xóa task (giả lập vì amock không thật sự xóa)
    private fun deleteTask(id: Int) {
        Log.d("TaskDetailActivity", "🗑️ Yêu cầu xóa Task ID=$id ...")
        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.deleteTask(id)
            .enqueue(object : Callback<ApiResponse<Void>> {
                override fun onResponse(
                    call: Call<ApiResponse<Void>>,
                    response: Response<ApiResponse<Void>>
                ) {
                    progressBar.visibility = View.GONE
                    Log.d("TaskDetailActivity", "📥 Kết quả xóa: ${response.code()}")

                    if (response.isSuccessful) {
                        Toast.makeText(this@TaskDetailActivity, "Xóa thành công (mock)!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@TaskDetailActivity, "Không thể xóa (mock API)!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Log.e("TaskDetailActivity", "❌ Lỗi xóa: ${t.message}")
                    Toast.makeText(this@TaskDetailActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
