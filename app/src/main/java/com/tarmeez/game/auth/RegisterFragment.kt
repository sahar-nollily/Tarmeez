package com.tarmeez.game.auth

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateMarginsRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private var _binding:FragmentRegisterBinding? = null
    private val binding:FragmentRegisterBinding?
    get() = _binding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var progressDialog: AlertDialog

    override fun onCreateView (
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
        binding?.userEmail?.isFocusable = true
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
                    binding?.userEmail?.text.toString().trim(),
                    binding?.userPassword?.text.toString().trim()
                )
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
                    //Navigate to HomeFragment and create session
                }

                is AuthViewModel.State.ErrorLogin -> {
                    progressDialog.dismiss()
                    Log.d(TAG,  state.message)
                    //This code causes exception due to Theme.AppCompat

                    /*
                    val snackBar = binding?.root?.let {
                        Snackbar.make(
                            it,
                            getString(R.string.error_message, " "), 3000)
                    }
                    if (snackBar != null) {
                        val snackBarText: TextView = snackBar.view.
                        findViewById(com.google.android.material.R.id.snackbar_text)
                        snackBarText.textSize = 16f
                        val layoutParams = FrameLayout.LayoutParams(
                            LinearLayout
                                .LayoutParams.WRAP_CONTENT, LinearLayout.
                            LayoutParams.WRAP_CONTENT, Gravity.END )
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