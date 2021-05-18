package com.moengage.sample.kotlin.inbox.custominbox

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.listener.OnMessagesAvailableListener
import com.moengage.inbox.core.listener.UnClickedCountListener
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.sample.kotlin.R
import com.moengage.sample.kotlin.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity(), OnMessagesAvailableListener,
    UnClickedCountListener {

    private lateinit var adapter: NotificationAdapter
    private lateinit var binding: ActivityNotificationsBinding
    private var unreadCount: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        //Fetch the unclicked message count
        MoEInboxHelper.getInstance().getUnClickedMessagesCountAsync(this, this)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val badgeView = menu.findItem(R.id.badge).actionView
        badgeView.findViewById<TextView>(R.id.tvUnClickedCount).text = unreadCount.toString()

        return super.onCreateOptionsMenu(menu)
    }

    private fun loadData() {
        //Fetch all inbox messages asynchronously
        MoEInboxHelper.getInstance().fetchAllMessagesAsync(this, this)
    }

    private fun initView() {
        adapter = NotificationAdapter()
        binding.notificationList.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.notificationList.context,
            DividerItemDecoration.VERTICAL
        )
        binding.notificationList.addItemDecoration(dividerItemDecoration)
        binding.notificationList.adapter = adapter
    }

    /**
     * callback for receiving the inbox messages list
     * @param list - List of {@link InboxMessage}
     */
    override fun onMessagesAvailable(messageList: List<InboxMessage>) {
        if (messageList.isEmpty()) {
            binding.emptyMessage.visibility = View.VISIBLE
            binding.notificationList.visibility = View.GONE
        } else {
            binding.emptyMessage.visibility = View.GONE
            binding.notificationList.visibility = View.VISIBLE
            adapter.setData(messageList)
        }
    }

    /**
     * callback for receiving unclicked count
     * @param count - unclicked message count
     */
    override fun onCountAvailable(count: Long) {
        unreadCount = count
        invalidateOptionsMenu()
    }
}