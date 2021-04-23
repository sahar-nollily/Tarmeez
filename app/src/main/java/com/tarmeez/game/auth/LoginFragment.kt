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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment: Fragment() {
    private var _binding:FragmentLoginBinding? = null
    private val binding:FragmentLoginBinding?
    get() = _binding
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
           //Navigate to forgot password fragments
        }
        binding?.login?.apply {
            setOnClickListener {
                hideKeyBoard()
                if (validation()) {
                    authViewModel.Login (
                        binding?.userEmail?.text.toString().trim(),
                        binding?.userPassword?.text.toString().trim()
                    )
                }
            }

            setOnFocusChangeListener { _, _ ->
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
        authViewModel.state.observe(viewLifecycleOwner , { state ->
            when (state) {
                is AuthViewModel.State.Authenticated -> {
                    progressDialog.dismiss()
                    //Navigate to HomeFragment and create session
                }

                is AuthViewModel.State.ErrorLogin -> {
                    progressDialog.dismiss()
                    Log.d(TAG,  state.message)

                    //This code causes exception due to Themt.AppCompat
                    /*
                    val snackBar = binding?.root?.let {
                        Snackbar.make(
                            it,
                            getString(R.string.error_message, " "), 3000)
                    }
                    if (snackBar != null) {
                        val snackBarText: TextView = snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
                        snackBarText.textSize = 16f
                        val layoutParams = FrameLayout.LayoutParams(
                            LinearLayout
                                .LayoutParams.WRAP_CONTENT, LinearLayout.
                            LayoutParams.WRAP_CONTENT, Gravity.RIGHT )
                        layoutParams.updateMarginsRelative(0, 1800,
                            0, 0)
                        snackBarText.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,
                            R.drawable.ic_baseline_error_outline_24, 0)
                        snackBar.view.setPadding(400, 0, 0, 0)
                        layoutParams.gravity = Gravity.CENTER
                        snackBar.view.layoutParams = layoutParams
                        snackBar.setBackgroundTint(resources.getColor(R.color.dark_gray))
                        snackBar.setActionTextColor(resources.getColor(R.color.white))
                        snackBar.show()
                    }

                     */
                }

                is AuthViewModel.State.Loading -> {
                    progressDialog.show()
                }
            }
        })
    }

    private fun validation():Boolean {
        var result = true
        if(binding?.userEmail?.text.toString().isBlank()) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            result = false
        }
        if(binding?.userPassword?.text.toString().isBlank()) {
            binding?.userPassword?.error = getString(R.string.enter_your_password)
            result = false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding?.userEmail?.text.toString()).matches()) {
            binding?.userEmail?.error = getString(R.string.invalid_email)
        }
        if(binding?.userEmail?.text.toString().isBlank() &&
            binding?.userPassword?.text.toString().isBlank() ) {
            binding?.userEmail?.error = getString(R.string.enter_your_email)
            binding?.userPassword?.error = getString(R.string.enter_your_password)
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