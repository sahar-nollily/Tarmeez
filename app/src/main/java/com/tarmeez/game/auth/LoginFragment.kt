package com.tarmeez.game.auth

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.tarmeez.game.MainActivity
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment: Fragment() {
    private var _binding:FragmentLoginBinding? = null
    private val binding:FragmentLoginBinding?
    get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var progressDialog:AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initViews()
        initObserver()
        return binding?.root
    }

    private fun initViews() {
        binding?.userEmail?.hint = getString(R.string.enter_email, " ")
        binding?.userPassword?.hint = getString(R.string.enter_password, " ")
        binding?.newAccount?.setOnClickListener {
            binding?.root?.let { view ->
                Navigation.findNavController(view)
                    .navigate(R.id.LoginFragmentToRegisterFragment)
            }
        }
        binding?.forgotPassword?.setOnClickListener {
            binding?.root?.let { view ->
                Navigation.findNavController(view)
                    .navigate(R.id.LoginFragmentToForgotPasswordFragment) }
        }
        binding?.login?.apply {
            setOnClickListener {
                (activity as MainActivity).hideKeyBoard()
                if (validation()) {
                    authViewModel.login (
                        binding?.userEmail?.text.toString().trim(),
                        binding?.userPassword?.text.toString().trim()
                    )
                }
            }
        }
        progressDialog = object : AlertDialog(requireContext()){
            override fun show() {
                super.show()
                setContentView(R.layout.loading)
            }
        }
    }

    private fun initObserver() {
        authViewModel.state.observe(viewLifecycleOwner , { state ->
            when (state) {
                is AuthViewModel.State.Authenticated -> {
                    Log.d(TAG, "User Authenticated")
                    progressDialog.dismiss()
                    //Navigate to HomeFragment and create session

                }
                is AuthViewModel.State.ErrorLogin -> {
                    progressDialog.dismiss()
                    Log.d(TAG,  state.message)
                    //Add snackbasr here to notify the user that something went wrong
                }
                is AuthViewModel.State.Loading -> {
                    progressDialog.show()
                }
                else -> {}
            }
        })
    }

    private fun validation():Boolean {
        var result = true
        if (binding?.userEmail?.text.toString().isBlank()) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            result = false
        }
        if (binding?.userPassword?.text.toString().isBlank()) {
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding?.userEmail?.text.toString()).matches()) {
            binding?.userEmail?.error = getString(R.string.invalid_email)
        }
        if (binding?.userEmail?.text.toString().isBlank() &&
            binding?.userPassword?.text.toString().isBlank() ) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        return result
    }
}