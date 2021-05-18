package com.moengage.sample.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.MoEInboxUiHelper
import com.moengage.inbox.ui.listener.OnMessageClickListener
import com.moengage.inbox.ui.view.InboxActivity
import com.moengage.sample.kotlin.databinding.ActivityMainBinding
import com.moengage.sample.kotlin.inbox.customadapter.CustomInboxAdapter
import com.moengage.sample.kotlin.inbox.custominbox.NotificationsActivity
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity(), OnMessageClickListener, OnHomeCategorySelected {

    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        trackSampleData()
    }

    private fun initView() {
        val categoryList = arrayListOf<HomeCategory>()
        categoryList.apply {
            add(HomeCategory(FeaturesCategory.INBOX_DEFAULT_UI, "Default Notification Center"))
            add(HomeCategory(FeaturesCategory.INBOX_CUSTOM_INBOX_ADAPTER, "Custom InboxAdapter"))
            add(HomeCategory(FeaturesCategory.INBOX_CUSTOM_INBOX, "Custom Inbox - Self handled"))
        }
        binding.categoryList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter =
                HomeListAdapter(categoryList, this@MainActivity)
        }
    }

    override fun onMessageClick(inboxMessage: InboxMessage): Boolean {
        Timber.v("$tag onMessageClick() : $inboxMessage")
        return false
    }

    private fun trackSampleData() {
        //building event attributes
        val properties = Properties()
            .addAttribute("sign-up-date", Date())
            .addAttribute("type", "user-pass")
        //sample track event
        MoEHelper.getInstance(applicationContext).trackEvent("Sign Up", properties)

        //identify user uniquely across installs and devices
        //this id should be the one the app uses to identify user in your system
        //should be set only after log-in
        // The value used here is only for illustration purposes.
        MoEHelper.getInstance(applicationContext).setUniqueId("androidsample")

        //tracking user attributes samples
        MoEHelper.getInstance(applicationContext).setFullName("Michael Jordan")
        MoEHelper.getInstance(applicationContext).setEmail("a@b.com")
        MoEHelper.getInstance(applicationContext).setUserAttribute("last ordered", Date())
    }

    override fun onHomeCategorySelected(category: FeaturesCategory) {
        when (category) {
            FeaturesCategory.INBOX_DEFAULT_UI -> {
                startActivity(Intent(this, InboxActivity::class.java))
            }
            FeaturesCategory.INBOX_CUSTOM_INBOX_ADAPTER -> {
                // set the customised InboxAdapter and launch InboxActivity
                MoEInboxUiHelper.getInstance().setInboxAdapter(CustomInboxAdapter())
                startActivity(Intent(this, InboxActivity::class.java))
            }
            FeaturesCategory.INBOX_CUSTOM_INBOX -> {
                startActivity(Intent(this, NotificationsActivity::class.java))
            }
        }
    }
}
