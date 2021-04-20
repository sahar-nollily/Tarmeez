package com.tarmeez.game.games.astronaut

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tarmeez.game.databinding.FragmentEndGameBinding

class EndGameFragment : Fragment() {
    private var _binding: FragmentEndGameBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEndGameBinding.inflate(inflater, container, false)

        binding.restrt.setOnClickListener {
            val action = EndGameFragmentDirections.actionEndGameFragmentToMenuGameFragment()
            findNavController().navigate(action)
        }
        binding.next.setOnClickListener {
//            val action = EndGameFragmentDirections.actionEndGameFraagmentToFinalGameFragment("-MQcsbQzoRZAJLGQuq0J")
//            findNavController().navigate(action)
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}