package com.tarmeez.game.games.astronaut

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.tarmeez.game.R
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioX
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioY

class Bird internal constructor(res: Resources?) {
    var speed = 20
    var wasShot = true
    var x = 0
    var y: Int
    var width: Int
    var height: Int
    var birdCounter = 1
    var bird1: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bird1)
    var bird2: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bird2)
    var bird3: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bird3)
    var bird4: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bird4)
    val bird: Bitmap
        get() {
            if (birdCounter == 1) {
                birdCounter++
                return bird1
            }
            if (birdCounter == 2) {
                birdCounter++
                return bird2
            }
            if (birdCounter == 3) {
                birdCounter++
                return bird3
            }
            birdCounter = 1
            return bird4
        }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }
    init {
        width = bird1.width
        height = bird1.height
        width /= 15
        height /= 15
        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()
        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false)
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false)
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false)
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false)
        y = -height
    }
}
