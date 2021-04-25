package com.tarmeez.game.games.astronaut

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tarmeez.game.R

class Background internal constructor(
    screenX: Int,
    screenY: Int,
    res: Resources?
) {
    var x = 0
    var y = 0
    var background: Bitmap = BitmapFactory.decodeResource(res, R.drawable.backgroungame)

    init {
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}
