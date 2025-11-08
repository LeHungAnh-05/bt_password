package com.example.bt_password.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.model.Subtask

class SubtaskAdapter(private var subtasks: List<Subtask>) :
    RecyclerView.Adapter<SubtaskAdapter.SubtaskViewHolder>() {

    class SubtaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cbSubtask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subtask, parent, false)
        return SubtaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubtaskViewHolder, position: Int) {
        val subtask = subtasks[position]
        holder.checkBox.text = subtask.title
        holder.checkBox.isChecked = subtask.isCompleted
    }

    override fun getItemCount() = subtasks.size

    fun updateData(newList: List<Subtask>) {
        subtasks = newList
        notifyDataSetChanged()
    }
}
