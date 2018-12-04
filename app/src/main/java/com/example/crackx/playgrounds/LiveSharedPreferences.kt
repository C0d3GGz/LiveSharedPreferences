package com.example.crackx.playgrounds

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.apply
import kotlin.reflect.KClass

class LiveSharedPreferences(val sharedPreferences: SharedPreferences){

    private val cachedData = mutableMapOf<OuterKey, MutableLiveData<Any>>()

    inline fun <reified T: Any>getValue(key: String, defaultValue: T? = null): LiveData<T> { // TODO result type T is incorrect if you use null strings...

        val outerKey = OuterKey(key, T::class)
        val value: Any? = when(T::class){
            Int::class -> sharedPreferences.getInt(key, defaultValue as Int? ?: -1)
            Long::class -> sharedPreferences.getLong(key, defaultValue as Long? ?: -1L)
            Float::class -> sharedPreferences.getFloat(key, defaultValue as Float? ?: -1f)
            Boolean::class -> sharedPreferences.getBoolean(key, defaultValue as Boolean? ?: false)
            else -> sharedPreferences.getString(key, defaultValue as String?)
        }

        if(`access$cachedData`[outerKey] == null){
            `access$cachedData`[outerKey] = MutableLiveData()
            `access$cachedData`[outerKey]!!.postValue(value)
        }

        return `access$cachedData`[outerKey] as LiveData<T>
    }

    inline fun <reified T : Any>setValue(key: String, value: T?){

        when(T::class){
            Int::class -> sharedPreferences.apply { putInt(key, value as Int) }
            Long::class -> sharedPreferences.apply { putLong(key, value as Long) }
            Float::class -> sharedPreferences.apply { putFloat(key, value as Float) }
            Boolean::class -> sharedPreferences.apply { putBoolean(key, value as Boolean) }
            else -> sharedPreferences.apply { putString(key, value as String?) }
        }

        val outerKey = OuterKey(key, T::class)
        `access$cachedData`[outerKey]?.postValue(value)
    }

    @PublishedApi
    internal val `access$cachedData`: MutableMap<OuterKey, MutableLiveData<Any>>
        get() = cachedData
}


data class OuterKey(val innerKey: String, val clazz: KClass<out Any>)
