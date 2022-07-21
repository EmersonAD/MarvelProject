package com.souzaemerson.marvelproject.view.login.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souzaemerson.marvelproject.R
import com.souzaemerson.marvelproject.databinding.ActivityLoginBinding
import com.souzaemerson.marvelproject.databinding.ActivityRegisterBinding
import com.souzaemerson.marvelproject.view.login.register.activity.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}