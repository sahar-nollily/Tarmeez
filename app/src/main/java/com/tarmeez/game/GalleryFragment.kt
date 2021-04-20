package com.tarmeez.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tarmeez.game.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        binding.fightingGameCardView.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.menuGameFragment)
        }

        binding.matchingGameCardView.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.matchingGameFragment)
        }

        return binding.root
    }

}