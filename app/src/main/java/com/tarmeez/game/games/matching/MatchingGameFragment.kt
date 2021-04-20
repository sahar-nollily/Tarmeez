package com.tarmeez.game.games.matching

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarmeez.game.databinding.FragmentMatchingGameBinding


class MatchingGameFragment : Fragment() {

    private var _binding: FragmentMatchingGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchingGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}