package com.tarmeez.game.data

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    fun saveUserInfo (
        name:String? = null,
        email:String? = null, age:Int = 0,
        scores:Int = 0, gender:String? = null) {
        with (sharedPreferences.edit()) {
            putString(Keys.NAME, name)
            putString(Keys.EMAIL, email)
            putInt(Keys.AGE, age)
            putInt(Keys.SCORES, scores)
            putString(Keys.GENDER, gender)
            commit()
        }
    }

    fun getUser() = User (
        name = sharedPreferences.getString(Keys.NAME, null),
        email = sharedPreferences.getString(Keys.EMAIL, null),
        age = sharedPreferences.getInt(Keys.AGE, 0),
        scores = sharedPreferences.getInt(Keys.SCORES, 0),
        gender = sharedPreferences.getString(Keys.GENDER, null)
    )

    fun isLoggedOut() = sharedPreferences.getString(Keys.EMAIL, null) == null

    fun logout() = with(sharedPreferences.edit()) {
        putString(Keys.EMAIL, null)
        commit()
    }
}