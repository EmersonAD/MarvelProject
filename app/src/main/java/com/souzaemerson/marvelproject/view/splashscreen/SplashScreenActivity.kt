package com.souzaemerson.marvelproject.view.splashscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.souzaemerson.marvelproject.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}