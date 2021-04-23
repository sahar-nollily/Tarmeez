package com.tarmeez.game.games.astronaut

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.navigation.Navigation
import com.tarmeez.game.R
import java.util.*

@SuppressLint("ViewConstructor")
class GameView(
    private val fragment: AstronautGameFragment,
    private var screenX: Int,
    private var screenY: Int
) : SurfaceView(fragment.context),
    Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private var score = 0
    private val background1: Background
    private val background2: Background
    private val flight: Flight
    private val random: Random
    private val bullets: MutableList<Bullet>
    private val birds: Array<Bird>
    private var soundPool: SoundPool? = null
    private val sound: Int
    private val paint: Paint
    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        background1.x -= 10 * screenRatioX.toInt()
        background2.x -= 10 * screenRatioX.toInt()
        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }
        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }
        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY.toInt()
        else flight.y += 30 * screenRatioY.toInt()

        if (flight.y < 0)
            flight.y = 0
        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height.toInt()
        val trash: MutableList<Bullet> = ArrayList()
        for (bullet in bullets) {
            if (bullet.x > screenX) trash.add(bullet)
            bullet.x += 50 * screenRatioX.toInt()
            for (bird in birds) {
                if (Rect.intersects(
                        bird.getCollisionShape(),
                        bullet.getCollisionShape()
                    )
                ) {
                    score++
                    bird.x = -500
                    bullet.x = screenX + 500
                    bird.wasShot = true
                }
            }
        }
        for (bullet in trash) bullets.remove(bullet)
        for (bird in birds) {

            bird.x -= bird.speed
            if (bird.x + bird.width < 0) {
                if (!bird.wasShot) {
                    isGameOver = true
                    return
                }
                val bound = (15 * screenRatioX).toInt()
                bird.speed = random.nextInt(bound)
                if (bird.speed < 10 * screenRatioX) bird.speed =
                    (10 * screenRatioX).toInt()
                bird.x = screenX
                bird.y = random.nextInt(1000)
            }
            if (Rect.intersects(
                    bird.getCollisionShape(),
                    flight.getCollisionShape()
                )
            ) {
                isGameOver = true
                return
            }
        }

    }

    private fun sleep() {}
    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(
                background1.background,
                background1.x.toFloat(),
                background1.y.toFloat(),
                paint
            )
            canvas.drawBitmap(
                background2.background,
                background2.x.toFloat(),
                background2.y.toFloat(),
                paint
            )
            canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)
            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(flight.dead, flight.x.toFloat(), flight.y.toFloat(), paint)
                canvas.drawBitmap(flight.dead, flight.x.toFloat(), flight.y.toFloat(), paint)
                canvas.drawText(
                    context.getString(R.string.game_over),
                    canvas.width / 2 - 200.toFloat(),
                    canvas.height - 200.toFloat(),
                    paint
                )
                holder.unlockCanvasAndPost(canvas)
                waitBeforeExiting()
                return
            }
            for (bird in birds) canvas.drawBitmap(
                bird.bird,
                bird.x.toFloat(),
                bird.y.toFloat(),
                paint
            )

            canvas.drawBitmap(flight.flight, flight.x.toFloat(), flight.y.toFloat()- screenX / 4, paint)
            for (bullet in bullets) canvas.drawBitmap(
                bullet.bullet,
                bullet.x.toFloat(),
                bullet.y.toFloat()-300,
                paint
            )
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                flight.isGoingUp = true
            }
            MotionEvent.ACTION_UP -> {
                flight.isGoingUp = false
                if (event.x > screenX / 2) flight.toShoot++
            }
        }
        return true
    }

    fun pause() {
        try {
            isPlaying = false
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun newBullet() {
        soundPool!!.play(sound, 1f, 1f, 0, 0, 1f)
        val bullet = Bullet(resources)
        bullet.x = (flight.x + flight.width).toInt()
        bullet.y = (flight.y + flight.height / 2).toInt()
        bullets.add(bullet)
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(2500)
            val navController =
                Navigation.findNavController(fragment.requireActivity(), R.id.fragment_container)
            navController.navigate(R.id.endGameFragment)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        var screenRatioX: Float = 0.0f
        var screenRatioY: Float = 0.0f
    }

    init {
        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenRatioX = size.x / screenX.toFloat()
        screenRatioY = size.y / screenY.toFloat()
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        sound = soundPool!!.load(fragment.context, R.raw.shoot, 1)
        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)
        flight = Flight(this, screenY, resources)
        bullets = ArrayList<Bullet>()
        background2.x = screenX
        paint = Paint()
        paint.textSize = 100f
        paint.color = Color.WHITE
        birds = arrayOf(Bird(resources), Bird(resources))
        for (i in 0..1) {
            val bird = Bird(resources)
            birds[i] = bird
        }
        random = Random()
    }
}