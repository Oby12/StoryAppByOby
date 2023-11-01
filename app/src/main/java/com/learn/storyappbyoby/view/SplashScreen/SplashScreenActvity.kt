package com.learn.storyappbyoby.view.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.view.main.MainActivity


class SplashScreenActvity : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_actvity)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },SPLASH_DELAY)
    }
}