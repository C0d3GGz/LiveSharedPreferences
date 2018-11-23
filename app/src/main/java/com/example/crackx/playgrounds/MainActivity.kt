package com.example.crackx.playgrounds

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs = getSharedPreferences("test", Context.MODE_PRIVATE)
        val livePrefs = LiveSharedPreferences(sharedPrefs)
        var lastKnownVal = -1

        livePrefs.getValue("level", 1).observe(this, Observer {
            text.text = it.toString()
            lastKnownVal = it
        })

        livePrefs.getValue("randomUUID", "none").observe(this, Observer {
            anotherText.text = it
        })

        button.setOnClickListener {
            livePrefs.setValue("level", lastKnownVal+1)
            livePrefs.setValue("randomUUID", UUID.randomUUID().toString())
        }
    }
}
