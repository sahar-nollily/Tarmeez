package com.tarmeez.game.auth

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tarmeez.game.MainActivity
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    private var _binding:FragmentLoginBinding? = null
    val binding:FragmentLoginBinding?
    get() = _binding
    private val authViewModel: AuthViewModel by lazy { ViewModelProviders.of(this)
        .get(AuthViewModel::class.java) }
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
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        binding?.login?.apply {
            setOnClickListener {
                if (validation()) {
                    authViewModel.Login(binding?.userEmail?.text.toString(), binding?.userPassword.toString())
                }
            }

            setOnFocusChangeListener { view, b ->
                hideKeyBoard()
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
        authViewModel.state.observe(viewLifecycleOwner ,{ state ->
            when (state) {
                is AuthViewModel.State.Authenticated -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "true", Toast.LENGTH_LONG).show()
                    //navigate to home page
                }

                is AuthViewModel.State.ErrorLogin -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "error: ${state.message}", Toast.LENGTH_LONG).show()
                    Log.d("LamiaTest", state.message)
                }

                is AuthViewModel.State.Loading -> {
                    progressDialog.show()
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
        if(binding?.userEmail?.text?.isBlank() == true && binding?.userPassword?.text?.isBlank() == true) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        if( binding?.userPassword?.text.toString().length < 6 ) {
            binding?.userPassword?.error = getString(R.string.password_more_than_six)
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