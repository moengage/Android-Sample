package com.moengage.sample.kotlin.inbox.custom_adapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.moengage.core.internal.logger.Logger
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.adapter.ViewHolder
import com.moengage.inbox.ui.adapter.InboxListAdapter
import com.moengage.sample.kotlin.R
import java.lang.Exception

/**
 * Custom [ViewHolder] class to display [InboxMessage] item.
 */
class CustomInboxViewHolder(private val view: View) : ViewHolder(view) {

    private val tag = "CustomInboxViewHolder"

    private val messageView: TextView = view.findViewById(R.id.inboxMessage)
    private val titleView: TextView = view.findViewById(R.id.inboxTitle)
    private val couponCode: Button = view.findViewById(R.id.couponCode)
    private val deleteButton: ImageButton = view.findViewById(R.id.inboxDelete)

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
                couponCode.text = MoEInboxHelper.getInstance().getCouponCode(inboxMessage)
                couponCode.visibility = View.VISIBLE
            }
            titleView.text = inboxMessage.textContent.title
            messageView.text = inboxMessage.textContent.message

            deleteButton.setOnClickListener{
                // Deletes the InboxMessage item from Notification Center and removes the item
                // from the list
                inboxListAdapter.deleteItem(position, inboxMessage)
            }
            view.setOnClickListener {
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