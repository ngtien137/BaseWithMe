package com.base.baselibrary.utils.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class BaseDataStore constructor(var context: Context, private val storeName: String) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = storeName)

    inline fun <reified T> getPrefValue(key: String, defaultValue: T): Flow<T> =
        context.dataStore.data.map { preferences ->
            val keyPref = when (T::class.java) {
                String::class.javaObjectType -> {
                    stringPreferencesKey(key)
                }
                Int::class.javaObjectType -> {
                    intPreferencesKey(key)
                }
                Float::class.javaObjectType -> {
                    floatPreferencesKey(key)
                }
                Long::class.javaObjectType -> {
                    longPreferencesKey(key)
                }
                Boolean::class.javaObjectType -> {
                    booleanPreferencesKey(key)
                }
                else -> stringPreferencesKey(key)
            }
            (preferences[keyPref] ?: defaultValue) as T
        }

    suspend inline fun <reified T> putPrefValue(key: String, value: T) {
        context.dataStore.edit { edit ->
            when (T::class.java) {
                String::class.javaObjectType -> {
                    edit[stringPreferencesKey(key)] = value as String
                }
                Int::class.javaObjectType -> {
                    edit[intPreferencesKey(key)] = value as Int
                }
                Float::class.javaObjectType -> {
                    edit[floatPreferencesKey(key)] = value as Float
                }
                Long::class.javaObjectType -> {
                    edit[longPreferencesKey(key)] = value as Long
                }
                Boolean::class.javaObjectType -> {
                    edit[booleanPreferencesKey(key)] = value as Boolean
                }
            }
        }
    }
}