package com.example.bt_password.ui.lists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.api.RetrofitClient
import com.example.bt_password.data.model.ApiResponse
import com.example.bt_password.data.model.Task
import com.example.bt_password.ui.detail.TaskDetailActivity
import com.example.bt_password.ui.settings.SettingActivity
import com.example.bt_password.ProfileActivity
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListActivity : AppCompatActivity() {

    private lateinit var rvTasks: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: TaskAdapter

    private lateinit var logoImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Ánh xạ view
        rvTasks = findViewById(R.id.rvTasks)
        emptyView = findViewById(R.id.emptyView)

        // Cấu hình RecyclerView
        rvTasks.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(emptyList()) { task ->
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra("TASK_ID", task.id)
            startActivity(intent)
        }
        rvTasks.adapter = adapter

        // Gọi API
        loadTasks()

        // Cấu hình bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_tasks -> {
                    Toast.makeText(this, "Danh sách công việc", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
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
