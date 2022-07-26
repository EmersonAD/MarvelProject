package com.souzaemerson.marvelproject.view.login.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.souzaemerson.marvelproject.core.Status
import com.souzaemerson.marvelproject.data.db.AppDatabase
import com.souzaemerson.marvelproject.data.repository.login.LoginRepository
import com.souzaemerson.marvelproject.data.repository.login.LoginRepositoryImpl
import com.souzaemerson.marvelproject.data.repository.login.LoginRepositoryMock
import com.souzaemerson.marvelproject.databinding.ActivityLoginBinding
import com.souzaemerson.marvelproject.util.Watcher
import com.souzaemerson.marvelproject.util.setError
import com.souzaemerson.marvelproject.util.toast
import com.souzaemerson.marvelproject.view.login.viewmodel.LoginViewModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: LoginRepository
    private val dao by lazy {
        AppDatabase.getDb(applicationContext).characterDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        repository = LoginRepositoryImpl(dao)
        viewModel = LoginViewModel.LoginViewModelProvideFactory(repository)
            .create(LoginViewModel::class.java)

        observeVMEvents()

        binding.run {
            loginButton.setOnClickListener {
                val email = binding.loginUsernameEdit.text.toString()
                val password = binding.loginPasswordEdit.text.toString()

                viewModel.login(email, password)
            }
            loginUsernameEdit.addTextChangedListener(watcher)
            loginPasswordEdit.addTextChangedListener(watcher)
        }
    }

    private val watcher = Watcher {
        binding.loginButton.isEnabled = binding.loginUsernameEdit.text.toString().isNotEmpty() &&
                binding.loginPasswordEdit.text.toString().isNotEmpty()
    }

    private fun observeVMEvents() {
        viewModel.loginFieldErrorResId.observe(this) {
            binding.loginUsernameLayout.setError(this@LoginActivity, it)
        }
        viewModel.passwordFieldErrorResId.observe(this) {
            binding.loginPasswordLayout.setError(this@LoginActivity, it)
        }
        viewModel.loading.observe(this) {
            binding.loginButton.progress(it)
        }
        viewModel.user.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { user ->
                        toast("Sucesso ao logar ${user.name}")
                    }
                }
                Status.ERROR -> {
                    toast("Erro tentar logar-se")
                }
                Status.LOADING -> {}
            }
        }
    }
}