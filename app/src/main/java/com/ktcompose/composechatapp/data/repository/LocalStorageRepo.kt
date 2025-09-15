package com.ktcompose.composechatapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ktcompose.composechatapp.constants.K
import com.ktcompose.composechatapp.data.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class LocalStorageRepo @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveMyUser(user: UserModel) {
        val json = gson.toJson(user)
        prefs.edit().putString(K.USERS, json).apply()
    }

    fun getMyUser(): UserModel? {
        val json = prefs.getString(K.USERS, null) ?: return null
        return gson.fromJson(json, UserModel::class.java)
    }

    fun clearMyUser() {
        prefs.edit().remove(K.USERS).apply()
    }
}

