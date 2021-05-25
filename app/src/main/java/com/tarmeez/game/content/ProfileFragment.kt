package com.tarmeez.game.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val contentViewModel:ContentViewModel by viewModels()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initViews()
        initObserver()
        return binding.root
    }

    private fun initViews() {
        binding.logoutIcon.setOnClickListener {
            val logout = contentViewModel.logout()
            if (logout) {
                findNavController().navigate(R.id.ProfileFragmentToLoginFragment)
            }
        }

        binding.saveButton.setOnClickListener {
            //use contentViewModel.saveProfileInfo() to save user info
        }
    }

    private fun initObserver() {
        contentViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is ContentViewModel.State.DataSaved -> {

                }

                is ContentViewModel.State.DataSavingException -> {

                }
            }
        })
    }
}