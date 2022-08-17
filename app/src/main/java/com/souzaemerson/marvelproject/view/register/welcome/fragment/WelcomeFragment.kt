package com.souzaemerson.marvelproject.view.register.welcome.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.souzaemerson.marvelproject.data.model.User
import com.souzaemerson.marvelproject.databinding.FragmentWelcomeBinding
import com.souzaemerson.marvelproject.view.login.activity.LoginActivity
import com.souzaemerson.marvelproject.view.register.activity.RegisterActivity

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>("REGISTER_USER") as User

        welcomeWith(user.name)
        goToLoginPage()
    }

    private fun welcomeWith(username: String){
        val welcomeName = "SEJA BEM VINDO(A), $username!"
        binding.welcome.text = welcomeName
    }

    private fun goToLoginPage(){
        (activity as RegisterActivity).backToLoginStep()
    }
}