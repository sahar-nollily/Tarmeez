package com.tarmeez.game.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentRegisterBinding

class RegisterFragment: Fragment() {
    private var _binding:FragmentRegisterBinding? = null
    val binding:FragmentRegisterBinding?
    get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container, false)
        initViews()
        return binding?.root
    }

    private fun initViews() {
        binding?.userEmail?.hint = getString(R.string.enter_email, " ")
        binding?.userPassword?.hint = getString(R.string.enter_password, " ")
        binding?.passwordConfirmation?.hint = getString(R.string.password_confirmation, " ")
    }
}