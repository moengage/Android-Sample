package com.moengage.mutliple_instances

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.moengage.inapp.MoEInAppHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(applicationContext)
    }
}