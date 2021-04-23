package com.tarmeez.game.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel @ViewModelInject constructor(private val firebaseAuth:FirebaseAuth):ViewModel() {
    private val _state = MutableLiveData<State>()
    val state:LiveData<State>
    get() = _state
    fun Login (email:String, password:String) {
        _state.postValue(State.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.postValue(State.Authenticated)
            } else {
                _state.postValue(State.ErrorLogin(task.exception?.message.toString()))
            }
        }
    }

    fun register (email: String, password: String) {
        _state.postValue(State.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.postValue(State.Authenticated)
            } else {
                _state.postValue(State.ErrorLogin(task.exception?.message.toString()))
            }
        }
    }

    sealed class State {
        class ErrorLogin(val message:String):State()
        object Loading: State()
        object Authenticated: State()
    }
}