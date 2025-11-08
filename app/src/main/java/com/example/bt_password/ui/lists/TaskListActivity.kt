package com.example.bt_password.ui.lists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.api.RetrofitClient
import com.example.bt_password.data.model.ApiResponse
import com.example.bt_password.data.model.Task
import com.example.bt_password.ui.detail.TaskDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListActivity : AppCompatActivity() {

    private lateinit var rvTasks: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Ánh xạ view
        rvTasks = findViewById(R.id.rvTasks)
        emptyView = findViewById(R.id.emptyView)

        // Cấu hình RecyclerView
        rvTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(emptyList()) { task ->
            // Khi click vào 1 Task → mở chi tiết
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("TASK_ID", task.id)
            startActivity(intent)
        }
        rvTasks.adapter = adapter

        // Gọi API
        loadTasks()
    }

    private fun loadTasks() {
        Log.d("TaskListActivity", "🔄 Bắt đầu tải danh sách task...")

        emptyView.visibility = View.GONE
        rvTasks.visibility = View.GONE

        RetrofitClient.instance.getTasks().enqueue(object : Callback<ApiResponse<List<Task>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Task>>>,
                response: Response<ApiResponse<List<Task>>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.isSuccess) {
                        val taskList = apiResponse.data
                        if (taskList.isNotEmpty()) {
                            adapter.updateTasks(taskList)
                            rvTasks.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                            Log.d("TaskListActivity", "✅ Lấy được ${taskList.size} task từ API")
                        } else {
                            emptyView.visibility = View.VISIBLE
                            Log.w("TaskListActivity", "⚠️ Không có task nào trong danh sách")
                        }
                    } else {
                        emptyView.visibility = View.VISIBLE
                        Log.e("TaskListActivity", "❌ API trả lỗi: ${apiResponse?.message}")
                        Toast.makeText(
                            this@TaskListActivity,
                            "API lỗi: ${apiResponse?.message ?: "Không có dữ liệu"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    emptyView.visibility = View.VISIBLE
                    Log.e("TaskListActivity", "❌ Lỗi response: ${response.code()}")
                    Toast.makeText(
                        this@TaskListActivity,
                        "Lỗi: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Task>>>, t: Throwable) {
                emptyView.visibility = View.VISIBLE
                Log.e("TaskListActivity", "❌ Lỗi kết nối API: ${t.message}")
                Toast.makeText(
                    this@TaskListActivity,
                    "Không thể tải dữ liệu: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
