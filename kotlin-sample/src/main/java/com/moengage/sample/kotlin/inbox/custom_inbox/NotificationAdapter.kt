package com.moengage.sample.kotlin.inbox.custom_inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.sample.kotlin.databinding.NotificationItemBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Adapter for binding custom inbox item view
 *
 */
class NotificationAdapter :
    RecyclerView.Adapter<NotificationAdapter.NotificationItem>() {
    private var messages: ArrayList<InboxMessage> =
        ArrayList()

    fun setData(messages: List<InboxMessage>) {
        this.messages = messages as ArrayList<InboxMessage>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationItem {
        return NotificationItem(
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: NotificationItem,
        position: Int
    ) {
        val inboxMessage = messages[position]
        holder.itemViewBinding.notificationTitle.text = inboxMessage.textContent.title
        holder.itemViewBinding.notificationMessage.text = inboxMessage.textContent.message

        if (MoEInboxHelper.getInstance().hasCouponCode(inboxMessage)) {
            holder.itemViewBinding.notifCouponCode.text =
                MoEInboxHelper.getInstance().getCouponCode(inboxMessage)
            holder.itemViewBinding.notifCouponCode.visibility = View.VISIBLE
        }

        holder.itemViewBinding.unClickedIndicator.visibility =
            if (inboxMessage.isClicked) View.INVISIBLE else View.VISIBLE

        holder.itemViewBinding.root.setOnClickListener {
            // mark the inbox message clicked and track the click event
            MoEInboxHelper.getInstance()
                .trackMessageClicked(holder.itemViewBinding.root.context, inboxMessage)
            notifyDataSetChanged()
        }

        holder.itemViewBinding.notifDelete.setOnClickListener {
            //Delete the InboxMessage from inbox
            MoEInboxHelper.getInstance().deleteMessage(holder.itemViewBinding.root.context, inboxMessage)
            messages.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class NotificationItem(binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        public val itemViewBinding = binding
    }
}