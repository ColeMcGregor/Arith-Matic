package com.wiseravenstudios.arithmatic.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/*
 * One application-wide DataStore holds Arith-Matic's small persisted
 * preferences.
 *
 * Future accessibility, appearance, and export preferences can add new keys
 * without changing this DataStore declaration.
 */
private val Context.arithMaticPreferencesDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "arithmatic_preferences"
)

/**
 * Returns the application-wide preferences DataStore.
 *
 * Callers should use an application Context when retaining the returned
 * instance in long-lived dependencies.
 */
fun Context.getArithMaticDataStore():
        DataStore<Preferences> {
    return applicationContext
        .arithMaticPreferencesDataStore
}