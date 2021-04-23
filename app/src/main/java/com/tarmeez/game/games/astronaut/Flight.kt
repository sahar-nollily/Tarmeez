package com.tarmeez.game.games.astronaut

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.tarmeez.game.R
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioX
import com.tarmeez.game.games.astronaut.GameView.Companion.screenRatioY

class Flight internal constructor(
    private val gameView: GameView,
    screenY: Int,
    res: Resources?
) {
    var toShoot = 0
    var isGoingUp = false
    var x: Int
    var y: Int
    var width: Float
    var height: Float
    var wingCounter = 0
    var shootCounter = 1
    var flight1: Bitmap = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
    var flight2: Bitmap = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
    var shoot1: Bitmap
    var shoot2: Bitmap
    var shoot3: Bitmap
    var shoot4: Bitmap
    var shoot5: Bitmap
    var dead: Bitmap
    var restart: Bitmap
    var next: Bitmap
    val flight: Bitmap
        get() {
            if (toShoot != 0) {
                if (shootCounter == 1) {
                    shootCounter++
                    return shoot1
                }
                if (shootCounter == 2) {
                    shootCounter++
                    return shoot2
                }
                if (shootCounter == 3) {
                    shootCounter++
                    return shoot3
                }
                if (shootCounter == 4) {
                    shootCounter++
                    return shoot4
                }
                shootCounter = 1
                toShoot--
                gameView.newBullet()
                return shoot5
            }
            if (wingCounter == 0) {
                wingCounter++
                return flight1
            }
            wingCounter--
            return flight2
        }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width.toInt(), y + height.toInt())
    }

    init {
        width = flight1.width.toFloat()
        height = flight1.height.toFloat()
        width /= 8
        height /= 8
        width = (width * screenRatioX)
        height = (height * screenRatioY)
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.astrountcat)
        shoot1 = Bitmap.createScaledBitmap(shoot1, width.toInt(), height.toInt(), false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width.toInt(), height.toInt(), false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width.toInt(), height.toInt(), false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width.toInt(), height.toInt(), false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width.toInt(), height.toInt(), false)
        flight1 = Bitmap.createScaledBitmap(flight1, width.toInt(), height.toInt(), false)
        flight2 = Bitmap.createScaledBitmap(flight2, width.toInt(), height.toInt(), false)
        dead = BitmapFactory.decodeResource(res, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(dead, width.toInt(), height.toInt(), false)
        restart = BitmapFactory.decodeResource(res, R.drawable.restart)
        restart = Bitmap.createScaledBitmap(restart, width.toInt(), height.toInt(), false)
        next = BitmapFactory.decodeResource(res, R.drawable.next)
        next = Bitmap.createScaledBitmap(next, width.toInt(), height.toInt(), false)
        y = screenY / 2
        x = (64 * screenRatioX).toInt()
    }

}
