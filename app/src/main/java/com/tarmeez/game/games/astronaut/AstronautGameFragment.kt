package com.tarmeez.game.games.astronaut

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarmeez.game.R

class AstronautGameFragment : Fragment() {

    private lateinit var gameView: GameView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val point = Point()
        (context as Activity?)!!.windowManager
            .defaultDisplay.getSize(point)
        gameView = GameView(this,point.x,point.y)



        return gameView
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }


}