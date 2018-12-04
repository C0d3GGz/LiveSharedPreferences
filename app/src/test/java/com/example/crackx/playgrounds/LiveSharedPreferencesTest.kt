package com.example.crackx.playgrounds

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.core.Is.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class LiveSharedPreferencesTest{

    @get: Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var sharedPrefs: SharedPreferences

    @Before
    fun setup(){
        sharedPrefs = mock(SharedPreferences::class.java)
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPrefs.edit()).thenReturn(editor)
    }

    @Test
    fun `should wrap SharedPrefs result into LiveData`(){

        val KEY = "hello"
        val VALUE = "greetings"

        val sut = LiveSharedPreferences(sharedPrefs)
        sut.setValue(KEY, VALUE)

        `when`(sharedPrefs.getString(eq(KEY), eq(null))).thenReturn(VALUE)
        val result = sut.getValue<String>(KEY).value

        assertThat(result, `is`(VALUE))
    }

    @Test
    fun `should return different result with same key - based on type`(){

        val KEY = "level"
        val INT_VALUE = 1
        val STRING_VALUE = "One"

        `when`(sharedPrefs.getString(eq(KEY), anyString())).thenReturn(STRING_VALUE)
        `when`(sharedPrefs.getInt(eq(KEY), anyInt())).thenReturn(INT_VALUE)

        val sut = LiveSharedPreferences(sharedPrefs)
        val stringResult = sut.getValue(KEY, STRING_VALUE).value
        val intResult = sut.getValue(KEY, INT_VALUE).value

        assertThat(stringResult, `is`(STRING_VALUE))
        assertThat(intResult, `is`(INT_VALUE))
    }

}