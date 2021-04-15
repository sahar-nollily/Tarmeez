package com.tarmeez.game.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    private var _binding:FragmentLoginBinding? = null
    val binding:FragmentLoginBinding?
    get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initViews()
        return binding?.root
    }

    private fun initViews() {
        binding?.userEmail?.hint = getString(R.string.enter_email, " ")
        binding?.userPassword?.hint = getString(R.string.enter_password, " ")
    }
}