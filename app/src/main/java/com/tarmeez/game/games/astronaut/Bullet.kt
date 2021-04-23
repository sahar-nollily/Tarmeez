package com.tarmeez.game.games.astronaut

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.tarmeez.game.R
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioX
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioY

class Bullet internal constructor(res: Resources?) {
    var x = 0
    var y = 0
    var width: Int
    var height: Int
    var bullet: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bullet)
    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

    init {
        width = bullet.width
        height = bullet.height
        width /= 3
        height /= 8
        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()
        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)
    }
}