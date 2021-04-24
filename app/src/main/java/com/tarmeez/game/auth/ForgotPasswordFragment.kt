package com.tarmeez.game.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "ForgotPasswordFragment"
@AndroidEntryPoint
class ForgotPasswordFragment: Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val authViewModel:AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() {
        binding.userEmail.hint = getString(R.string.enter_email, " ")
        binding.forgotPassword.setOnClickListener {
            hideKeyBoard()
            if (validation()) {
                authViewModel.resetPassword(binding.userEmail.text.toString())
            }
        }
    }

    private fun initObservers() {
        authViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is AuthViewModel.State.PasswordReseated -> {
                    Log.d(TAG, "Email has been sent")
                    // add snackbar here to notify user that the email has been sent
                }

                is AuthViewModel.State.ErrorPasswordResetting -> {
                    Log.d(TAG, state.message)
                    // add snackbar here to notify user that something went wrong
                }

                else -> {}
            }
        })
    }

    private fun validation():Boolean {
        var result = true
        if (binding.userEmail.text.toString().isBlank()) {
            binding.userEmail.error = getString(R.string.enter_email)
            result = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.userEmail.text.toString()).matches()) {
            binding.userEmail.error = getString(R.string.invalid_email)
            result = false
        }
        return result
    }

    private fun hideKeyBoard() {
        activity?.let {
            val inputManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus
            if (view != null) {
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}