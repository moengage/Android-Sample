package com.moengage.sample.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.MoEInboxUiHelper
import com.moengage.inbox.ui.listener.OnMessageClickListener
import com.moengage.inbox.ui.view.InboxActivity
import com.moengage.sample.kotlin.inbox.custom_adapter.CustomInboxAdapter
import com.moengage.sample.kotlin.inbox.custom_inbox.NotificationsActivity
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnMessageClickListener {

    private val tag = "MainActivity"
    private lateinit var adapter: FeatureListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        initUI()

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

    private fun initUI() {
        val expandableFeaturesList = findViewById<ExpandableListView>(R.id.expandableListView)
        adapter = FeatureListAdapter(this, getFeaturesList(), getData())
        expandableFeaturesList.setAdapter(adapter)
        expandableFeaturesList.setOnChildClickListener { parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long ->
            handleOnChildClickEvent(groupPosition, childPosition)
            false
        }
    }

    override fun onMessageClick(inboxMessage: InboxMessage): Boolean {
        Timber.v("$tag onMessageClick() : $inboxMessage")
        return false
    }

    private fun getData(): HashMap<String, List<String>> {
        val features = getFeaturesList()
        val expandableFeatureList = HashMap<String, List<String>>()

        val inbox: ArrayList<String> = arrayListOf("Custom Inbox", "Custom InboxAdapter")

        //Add Inbox features
        expandableFeatureList[features[0]] = inbox

        return expandableFeatureList
    }

    private fun getFeaturesList(): List<String> {
        val features: MutableList<String> =
            ArrayList()
        features.add("Inbox")
        return features
    }

    private fun handleOnChildClickEvent(groupPosition: Int, childPosition: Int) {
        when (adapter.getChild(groupPosition, childPosition)) {
            "Custom Inbox" -> startActivity(Intent(this, NotificationsActivity::class.java))
            "Custom InboxAdapter" -> {
                // set the customised InboxAdapter and launch InboxActivity
                MoEInboxUiHelper.getInstance().setInboxAdapter(CustomInboxAdapter())
                startActivity(Intent(this, InboxActivity::class.java))
            }
        }
    }
}
