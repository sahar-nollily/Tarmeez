package com.tarmeez.game.auth

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentRegisterBinding

class RegisterFragment: Fragment() {
    private var _binding:FragmentRegisterBinding? = null
    val binding:FragmentRegisterBinding?
    get() = _binding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container, false)
        initViews()
        initObserver()
        return binding?.root
    }

    private fun initViews() {
        binding?.userEmail?.hint = getString(R.string.enter_email, " ")
        binding?.userPassword?.hint = getString(R.string.enter_password, " ")
        binding?.passwordConfirmation?.hint = getString(R.string.password_confirmation, " ")
        binding?.hasAccount?.setOnClickListener {
            binding?.root?.let { view ->
                   Navigation.findNavController(view)
                       .navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
        binding?.register?.setOnClickListener {
            if (validation()) {
                authViewModel.register(
                    binding?.userEmail?.text.toString(),
                    binding?.userEmail?.text.toString()
                )
            }
        }

        binding?.passwordConfirmation?.nextFocusDownId = R.id.register
        progressDialog = object : AlertDialog(requireContext()){
            override fun show() {
                super.show()
                setContentView(R.layout.loading)
            }
        }

    }

    private fun initObserver() {
        authViewModel.state.observe(viewLifecycleOwner ,{ state ->
            when (state) {
                is AuthViewModel.State.Authenticated -> {
                    Toast.makeText(requireContext(), "true", Toast.LENGTH_LONG).show()
                }

                is AuthViewModel.State.ErrorLogin -> {
                    Toast.makeText(requireContext(), "error: ${state.message}", Toast.LENGTH_LONG).show()
                }

                is AuthViewModel.State.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun validation():Boolean {
        var result = true
        if(binding?.userEmail?.text?.isBlank() == true) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            result = false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding?.userEmail?.text.toString()).matches()) {
            binding?.userEmail?.error = getString(R.string.invalid_email)
        }
        if(binding?.passwordConfirmation?.text?.isBlank() == true) {
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        if(binding?.userEmail?.text?.isBlank() == true && binding?.userPassword?.text?.isBlank() == true) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        if(binding?.userPassword?.text.toString() != binding?.passwordConfirmation?.text.toString()) {
            binding?.passwordConfirmation?.error = getString(R.string.un_matched_password)
            result = false
        } else if( binding?.userPassword?.text.toString().length < 6 ) {
            binding?.userPassword?.error = getString(R.string.password_more_than_six)
            result = false
        }
        return result
    }
}