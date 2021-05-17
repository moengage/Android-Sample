package com.moengage.sample.kotlin.inbox.custom_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.adapter.InboxAdapter
import com.moengage.inbox.ui.adapter.ViewHolder
import com.moengage.inbox.ui.adapter.InboxListAdapter
import com.moengage.sample.kotlin.R

/**
 * To use your own custom InboxAdapter, extend [InboxAdapter]and bind
 * your customised [ViewHolder] class.
 *
 */
class CustomInboxAdapter : InboxAdapter() {

    /**
     * @return custom [ViewHolder] object - [CustomInboxViewHolder]
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return CustomInboxViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.custom_inbox_item_view, viewGroup, false
            )
        )
    }

    override fun getItemViewType(position: Int, inboxMessage: InboxMessage): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int,
        inboxMessage: InboxMessage,
        inboxListAdapter: InboxListAdapter
    ) {
        (viewHolder as CustomInboxViewHolder).onBind(position, inboxMessage, inboxListAdapter)
    }
}