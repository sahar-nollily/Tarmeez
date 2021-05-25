package com.tarmeez.game.content

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.tarmeez.game.data.PreferenceManager

class ContentViewModel @ViewModelInject constructor (
    private val preferenceManager: PreferenceManager,
    private val firebaseDatabase: FirebaseDatabase,
    ):ViewModel() {
    private val _state:MutableLiveData<State> = MutableLiveData()
    val state:LiveData<State>
    get() = _state
    fun isLoggedOut() = preferenceManager.isLoggedOut()

    fun logout() = preferenceManager.logout()

    fun saveProfileInfo (name:String, age:Int, gender:String) {
        val ref =  firebaseDatabase.reference
        val users = ref.child("Users")
        val currantUser = preferenceManager.getUser()
        preferenceManager.saveUserInfo( id = currantUser.id, email = currantUser.email,
            name = name, age = age, gender = gender )
        val userRef =  users.child(currantUser.id.toString())
        userRef.setValue(preferenceManager.getUser()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.postValue(State.DataSaved)
            } else {
                _state.postValue(State.DataSavingException(task.exception.toString()))
            }
        }
    }

    sealed class State {
        object DataSaved:State()
        class  DataSavingException(val message:String):State()
    }
}