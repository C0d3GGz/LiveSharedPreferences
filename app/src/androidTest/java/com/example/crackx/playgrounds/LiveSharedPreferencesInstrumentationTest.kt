package com.example.crackx.playgrounds

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LiveSharedPreferencesInstrumentationTest{

    @get: Rule
    var rule = InstantTaskExecutorRule()

    @Test
    fun shouldRespectDefaultValue(){
        val textContext = InstrumentationRegistry.getInstrumentation().context
        val sharedPreferences = textContext.getSharedPreferences("test", Context.MODE_PRIVATE)

        val sut = LiveSharedPreferences(sharedPreferences)

        val result = sut.getValue("nonExistingKey", 122).value
        assertThat(result, `is`(122))
    }
}