package com.tarmeez.game.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val contentViewModel: ContentViewModel by viewModels()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        checkIfUserLoggedOut()
        return binding.root
    }

    private fun checkIfUserLoggedOut() {
        val isLoggedOut = contentViewModel.isLoggedOut()
        if (isLoggedOut) {
            findNavController().navigate(R.id.HomeFragmentToLoginFragment)
        }
    }
}