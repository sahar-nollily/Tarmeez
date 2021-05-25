package com.tarmeez.game.auth

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.tarmeez.game.data.PreferenceManager
import com.tarmeez.game.data.User

private const val TAG = "AuthViewModel"
class AuthViewModel @ViewModelInject constructor (
    private val firebaseAuth:FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val preferenceManager: PreferenceManager):ViewModel() {
    private val _state = MutableLiveData<State>()
    val state:LiveData<State>
    get() = _state

    fun login (email:String, password:String) {
        _state.postValue(State.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                preferenceManager.saveUserInfo(email = email, isLogin = true)
                checkIfUserInfoExist(email)
                _state.postValue(State.Authenticated)
            } else {
                _state.postValue(State.AuthError(task.exception?.message.toString()))
            }
        }
    }

    fun register (email: String, password: String) {
        _state.postValue(State.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val ref =  firebaseDatabase.reference
                val users = ref.child("Users")
                val currantUser = users.push()
                preferenceManager.saveUserInfo(id = currantUser.key, email = email)
                users.child(currantUser.key.toString())
                    .setValue(preferenceManager.getUser()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _state.postValue(State.Authenticated)
                        } else {
                            _state.postValue(State.DatabaseException(task.exception?.message.toString()))
                        }
                    }
            } else {
                _state.postValue(State.AuthError(task.exception?.message.toString()))
            }
        }
    }

    fun resetPassword (email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _state.postValue(State.PasswordReseated)
            } else {
                _state.postValue(State.ErrorPasswordResetting (task.exception?.message.toString()))
            }
        }
    }

    private fun checkIfUserInfoExist (email:String) {
        val currantUser = preferenceManager.getUser()
        //this will be implemented only if the user deleted the app
        if (currantUser.id == null)
            firebaseDatabase.reference.child("Users")
                .orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object :ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (user in snapshot.children) {
                            val jsonUser = Gson().fromJson(user.value.toString(), User::class.java)
                            preferenceManager.saveUserInfo (
                                id = jsonUser.id,
                                name = jsonUser.name,
                                age = jsonUser.age,
                                gender = jsonUser.gender,
                                email = jsonUser.email,
                                scores = jsonUser.scores
                            )
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, error.message)
                    }
                })
        else
            Log.d(TAG, "User id is not null")
        }

    sealed class State {
        class  AuthError(val message:String):State()
        object Loading: State()
        object Authenticated: State()
        object PasswordReseated:State()
        class  ErrorPasswordResetting(val message: String):State()
        class  DatabaseException(val message:String):State()
    }
}