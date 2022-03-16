package com.moengage.example.inbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moengage.example.databinding.InboxMessageItemBinding
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxMessage

/**
 * @author Umang Chamaria
 * Date: 2022/02/17
 */
class InboxAdapter(private val context: Context, private val messages: List<InboxMessage>) :
    RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

    class ViewHolder(val binding: InboxMessageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            InboxMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.binding.title.text = message.textContent.title
        holder.binding.message.text = message.textContent.message
        if (message.textContent.summary.isNotBlank()) {
            holder.binding.summary.text = message.textContent.summary
            holder.binding.summary.visibility = View.VISIBLE
        } else {
            holder.binding.summary.visibility = View.GONE
        }
        holder.binding.delete.setOnClickListener {
            MoEInboxHelper.getInstance().deleteMessage(context, message)
        }
        holder.binding.card.setOnClickListener {
//            MoEInboxHelper.getInstance()
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}