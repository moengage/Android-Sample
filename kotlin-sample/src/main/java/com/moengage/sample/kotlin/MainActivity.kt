package com.moengage.sample.kotlin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


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
        MoEHelper.getInstance(applicationContext).setUniqueId(1)

        //tracking user attributes samples
        MoEHelper.getInstance(applicationContext).setFullName("Michael Jordan")
        MoEHelper.getInstance(applicationContext).setEmail("a@b.com")
        MoEHelper.getInstance(applicationContext).setUserAttribute("last ordered", Date())
    }

}
