package com.neohamzah.tomkitsapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.neohamzah.tomkitsapp.ViewModelFactory
import com.neohamzah.tomkitsapp.databinding.ActivityRegisterBinding
import com.neohamzah.tomkitsapp.utils.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.register(username, email, password)
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.registerResult.observe(this) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage(result.data.message)
                                setPositiveButton("Next") { _, _ ->
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
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
}
