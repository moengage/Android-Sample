package com.moengage.example

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.moengage.cards.ui.CardActivity
import com.moengage.core.MOENGAGE_ACCOUNT_IDENTIFIER
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.inapp.MoEInAppHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // track event
        val properties = Properties()
        properties.addAttribute("attributeString", "string").addAttribute("attributeInteger", 123)
            .addAttribute("attributeDate", Date())
            .addDateIso("attributeDateIso", "2022-02-10T21:12:00Z")
        MoEAnalyticsHelper.trackEvent(applicationContext, "EVENT_SAMPLE", properties)

        // user attribute tracking
        MoEAnalyticsHelper.setFirstName(applicationContext, "First Name")
        MoEAnalyticsHelper.setLastName(applicationContext, "Last Name")
        MoEAnalyticsHelper.setBirthDate(applicationContext, Date())
    }

    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notifications -> {

                /**
                 * Here we are navigating to default cards UI implementation
                 * */
                val intent = Intent(this, CardActivity::class.java)
                intent.putExtra(MOENGAGE_ACCOUNT_IDENTIFIER, "<MOE_APP_ID>")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}