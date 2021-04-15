package com.tarmeez.game.games.astronaut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tarmeez.game.databinding.FragmentMenuGameBinding


class MenuGameFragment : Fragment() {
    private var _binding:FragmentMenuGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentMenuGameBinding.inflate(inflater, container, false)

        binding.play.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            val action = MenuGameFragmentDirections.actionMenuGameFragmentToAstronautGameFragment2()
            findNavController().navigate(action)
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}