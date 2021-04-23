package com.tarmeez.game.games

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel

class GamesViewModel: ViewModel() {
    private lateinit var mediaPlayer: MediaPlayer
    var numOfButtons = 0
    var results:ArrayList<Int> = ArrayList()
    var numOfMatching = 0
    val tags = listOf ("heart", "astronaut", "rocket", "heart", "astronaut", "rocket")

    fun controlSound(context: Context, soundId:Int) {
        mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer.start()
    }
}