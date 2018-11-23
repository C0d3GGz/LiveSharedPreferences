package com.example.crackx.playgrounds

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LiveSharedPreferencesInstrumentationTest{

    @get: Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var testContext: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sut: LiveSharedPreferences

    @Before
    fun setup(){
        testContext = InstrumentationRegistry.getInstrumentation().context
        sharedPreferences = testContext.getSharedPreferences("test", Context.MODE_PRIVATE)
        sut = LiveSharedPreferences(sharedPreferences)
    }

    @Test
    fun shouldRespectDefaultValue(){
        val result = sut.getValue("nonExistingKey", 122).value
        assertThat(result, `is`(122))
    }

    @Test
    fun shouldAcceptNullString(){
        val KEY = "stringKey"
        sut.setValue<String?>(KEY, null)

        val result = sut.getValue<String?>(KEY).value
        assertNull(result)
    }
}