package com.example.bt_password.ui.detail

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bt_password.R
import com.example.bt_password.data.model.Attachment

class AttachmentAdapter(private var attachments: List<Attachment>) :
    RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>() {

    inner class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAttachment: ImageView = itemView.findViewById(R.id.ivAttachment)
        val tvAttachmentName: TextView = itemView.findViewById(R.id.tvAttachmentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val attachment = attachments[position]
        holder.tvAttachmentName.text = attachment.fileName
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(attachment.fileUrl))
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = attachments.size

    // 👉 Cũng có hàm updateData()
    fun updateData(newAttachments: List<Attachment>) {
        attachments = newAttachments
        notifyDataSetChanged()
    }
}
