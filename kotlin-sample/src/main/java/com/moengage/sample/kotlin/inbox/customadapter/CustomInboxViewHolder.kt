package com.moengage.sample.kotlin.inbox.customadapter

import android.view.View
import com.moengage.core.internal.logger.Logger
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.adapter.ViewHolder
import com.moengage.inbox.ui.adapter.InboxListAdapter
import com.moengage.sample.kotlin.databinding.CustomInboxItemViewBinding
import java.lang.Exception

/**
 * Custom [ViewHolder] class to display [InboxMessage] item.
 */
class CustomInboxViewHolder(private val binding: CustomInboxItemViewBinding) : ViewHolder(binding.root) {

    private val tag = "CustomInboxViewHolder"

    public fun onBind(
        position: Int,
        inboxMessage: InboxMessage,
        inboxListAdapter: InboxListAdapter
    ) {
        try {
            Logger.v("$tag onBind() : ")

            /**
             * checks if the [InboxMessage] has coupon code
             */
            if (MoEInboxHelper.getInstance().hasCouponCode(inboxMessage)) {
                // fetches the coupon code
                binding.couponCode.text = MoEInboxHelper.getInstance().getCouponCode(inboxMessage)
                binding.couponCode.visibility = View.VISIBLE
            }
            binding.inboxTitle.text = inboxMessage.textContent.title
            binding.inboxMessage.text = inboxMessage.textContent.message

            binding.inboxDelete.setOnClickListener{
                // Deletes the InboxMessage item from Notification Center and removes the item
                // from the list
                inboxListAdapter.deleteItem(position, inboxMessage)
            }
            binding.root.setOnClickListener {
                //Notifies the InboxMessage clicked event to MoEngage SDK
                //NOTE: When you are using custom InboxAdapter and want the SDK to handle the
                // item click action, `InboxListAdapter#onItemClicked` must be invoked.
                inboxListAdapter.onItemClicked(position, inboxMessage)
            }
        } catch (e: Exception) {
            Logger.e("$tag Bind(): ", e)
        }
    }
}