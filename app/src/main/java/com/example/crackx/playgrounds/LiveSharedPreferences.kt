package com.example.crackx.playgrounds

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jetbrains.anko.apply

class LiveSharedPreferences(val sharedPreferences: SharedPreferences){

    private val cachedData = mutableMapOf<OuterKey, MutableLiveData<Any>>()

    inline fun <reified T>getValue(key: String, defaultValue: T? = null): LiveData<T> { // TODO result type T is incorrect if you use null strings...

        val value: Any?
        val outerKey: OuterKey
        
        when(T::class.java.simpleName){
            Integer::class.java.simpleName -> {
                value = sharedPreferences.getInt(key, defaultValue as Int? ?: -1)
                outerKey = OuterKey(key, Int.javaClass)
            }
            Long::class.java.simpleName -> {
                value = sharedPreferences.getLong(key, defaultValue as Long? ?: -1L)
                outerKey = OuterKey(key, Long.javaClass)
            }
            Float::class.java.simpleName -> {
                value = sharedPreferences.getFloat(key, defaultValue as Float? ?: -1f)
                outerKey = OuterKey(key, Float.javaClass)
            }
            Boolean::class.java.simpleName -> {
                value = sharedPreferences.getBoolean(key, defaultValue as Boolean? ?: false)
                outerKey = OuterKey(key, Float.javaClass)
            }
            else -> {
                value = sharedPreferences.getString(key, defaultValue as String?)
                outerKey = OuterKey(key, String.javaClass)
            }
        }

        if(`access$cachedData`[outerKey] == null){
            `access$cachedData`[outerKey] = MutableLiveData()
            `access$cachedData`[outerKey]!!.postValue(value)
        }

        return `access$cachedData`[outerKey] as LiveData<T>
    }

    inline fun <reified T>setValue(key: String, value: T?){

        val outerKey: OuterKey

        when(T::class.java.simpleName){
            Integer::class.java.simpleName -> {
                sharedPreferences.apply { putInt(key, value as Int) }
                outerKey = OuterKey(key, Int.javaClass)
            }
            Long::class.java.simpleName -> {
                sharedPreferences.apply { putLong(key, value as Long) }
                outerKey = OuterKey(key, Long.javaClass)
            }
            Float::class.java.simpleName -> {
                sharedPreferences.apply { putFloat(key, value as Float) }
                outerKey = OuterKey(key, Float.javaClass)
            }
            Boolean::class.java.simpleName -> {
                sharedPreferences.apply { putBoolean(key, value as Boolean) }
                outerKey = OuterKey(key, Boolean.javaClass)
            }
            else ->{
                sharedPreferences.apply { putString(key, value as String?) }
                outerKey = OuterKey(key, String.javaClass)
            }
        }

        //TODO: creation not necessary
        if(`access$cachedData`[outerKey] == null){
            `access$cachedData`[outerKey] = MutableLiveData()
        }
        `access$cachedData`[outerKey]!!.postValue(value)
    }

    @PublishedApi
    internal val `access$cachedData`: MutableMap<OuterKey, MutableLiveData<Any>>
        get() = cachedData
}


data class OuterKey(val innerKey: String, val clazz: Class<Any>)
