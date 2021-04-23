package com.tarmeez.game.games

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tarmeez.game.R
import com.tarmeez.game.databinding.FragmentMatchingGameBinding
import org.jetbrains.anko.childrenRecursiveSequence
import java.util.Collections.shuffle
import kotlin.collections.ArrayList

class MatchingGameFragment: Fragment() {
    private var _binding: FragmentMatchingGameBinding? = null
    private val binding  get() = _binding!!
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val gamesViewModel:GamesViewModel by viewModels()

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchingGameBinding.inflate(inflater, container, false)

        initViews()
        return binding.root
    }

    private fun initViews() {
        setRandomTagsToTheButtons()
        setImageButtonsOnClickListener()
        binding.retry.setOnClickListener {
            retryGame()
        }
        binding.moveToFinalGame.setOnClickListener {
            //add navigation to Final game fragment
        }
    }

    private fun setImageButtonsOnClickListener() {
        val tableRows = binding.tableView.children.toList()
        tableRows.forEach { row ->
            val cards = row.childrenRecursiveSequence()
            for (card in cards) {
                if (card is ImageView) {
                    card.setOnClickListener {
                        if(gamesViewModel.numOfButtons < 2) {
                            card.apply {
                                setImageButtonDrawable(tag.toString(), id)
                            }
                            gamesViewModel.results.add(card.id)
                            gamesViewModel.numOfButtons ++
                            if(gamesViewModel.numOfButtons == 2){
                                checkIfButtonsMatch()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkIfButtonsMatch() {
        val firstCard = binding.root.findViewById<ImageView>(gamesViewModel.results[0])
        val secondCard = binding.root.findViewById<ImageView>(gamesViewModel.results[1])
        if ( firstCard.tag.toString()  == secondCard.tag.toString()) {
            gamesViewModel.results = ArrayList()
            gamesViewModel.numOfButtons = 0
            gamesViewModel.numOfMatching ++
            if (gamesViewModel.numOfMatching == 3) {
                gamesViewModel.controlSound(requireContext(), R.raw.correct_sound_effect)
                gamesViewModel.numOfMatching = 0
                //update Scores here in data store and in FB
            }
        } else {
            handler.postDelayed ( {
                firstCard.apply {
                        Glide.with(binding.root)
                            .load(R.drawable.matching_image_question)
                            .into(this)
                        isEnabled = true
                    }
                secondCard.apply {
                        Glide.with(binding.root).load(R.drawable.matching_image_question).into(this)
                        isEnabled = true
                }
                gamesViewModel.results = ArrayList()
                gamesViewModel.numOfButtons = 0
            }, 1500)
        }
    }

    private fun setImageButtonDrawable(tag:String, id:Int) {
        when (tag.trim()) {
            "heart" -> {
                binding.root.findViewById<ImageView>(id).apply {
                    Glide.with(binding.root).load(R.drawable.heart).into(this)
                    isEnabled = false
                }
            }

            "rocket"-> {
                binding.root.findViewById<ImageView>(id).apply {
                    Glide.with(binding.root).load(R.drawable.rocket).into(this)
                    isEnabled = false
                }
            }

            "astronaut"-> {
                binding.root.findViewById<ImageView>(id).apply {
                    Glide.with(binding.root).load(R.drawable.astronaut).into(this)
                    isEnabled = false
                }
            }
        }
    }

    private fun setRandomTagsToTheButtons() {
        shuffle(gamesViewModel.tags)
        val tempTags = gamesViewModel.tags.toMutableList()
        val tableRows = binding.tableView.children.toList()
        tableRows.forEach { row ->
            val cards = row.childrenRecursiveSequence()
            for (card in cards ) {
                if (card is ImageView) {
                    card.tag = tempTags.first()
                    tempTags.remove(tempTags.first())
                }
            }
        }
    }

    private fun retryGame() {
        val tableRows = binding.tableView.children.toList()
        tableRows.forEach { row ->
            val cards = row.childrenRecursiveSequence()
            for (card in cards) {
                if ( card is ImageView) {
                    card.apply {
                        Glide.with(binding.root).load(R.drawable.matching_image_question).into(this)
                        isEnabled = true
                    }
                }
            }
        }
        setRandomTagsToTheButtons()
        setImageButtonsOnClickListener()
    }
}