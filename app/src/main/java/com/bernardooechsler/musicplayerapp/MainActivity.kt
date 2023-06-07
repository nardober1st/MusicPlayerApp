package com.bernardooechsler.musicplayerapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    private lateinit var time_txt: TextView
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val play_btn: Button = findViewById(R.id.play_button)
        val stop_btn: Button = findViewById(R.id.pause_button)
        val forward_btn: Button = findViewById(R.id.forward_button)
        val back_btn: Button = findViewById(R.id.back_button)

        val title_txt: TextView = findViewById(R.id.song_title)
        time_txt = findViewById(R.id.time_left_text)

        seekBar = findViewById(R.id.seek_bar)

        // Media Player

        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.astronaut
        )

        seekBar.isClickable = false

        // Adding Functionalities to the buttons
        play_btn.setOnClickListener {
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            time_txt.text = startTime.toString()
            seekBar.progress = startTime.toInt()

            handler.postDelayed(UpdateSongTime, 100)
        }

        // Setting the music title
        title_txt.text = resources.getResourceEntryName(
            R.raw.astronaut
        )

        // Stop Button
        stop_btn.setOnClickListener {
            mediaPlayer.pause()
        }

        // Forward Button
        forward_btn.setOnClickListener {
            var temp = startTime
            if ((temp + forwardTime) <= finalTime) {
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Can't jump forward", Toast.LENGTH_LONG).show()
            }
        }

        // Back Button
        back_btn.setOnClickListener {
            var temp = startTime.toInt()

            if ((temp - backwardTime) > 0) {
                startTime -= backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "We can't jump backward", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Creating the Runnable
    val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            time_txt.text = " " +
                    String.format(
                        "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            )
                        )
                    )

            seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }
    }
}