package com.neohamzah.tomkitsapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.databinding.ActivityLoginBinding
import com.neohamzah.tomkitsapp.ui.main.MainActivity
import com.neohamzah.tomkitsapp.utils.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setupAction()
        observeLoginResult()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etLogemail.text.toString().trim()
            val password = binding.etLogpassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                showToast("Email and Password cannot be empty!")
            }
        }
            binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    showToast("Login success!")
                    navigateToMain()
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast("Invalid email or Password")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
