package com.example.podgotovka.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.podgotovka.R
import com.example.podgotovka.databinding.ActivityLoginBinding
import com.example.podgotovka.utils.SharedPreferencesUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        checkAutoLogin()
        // Принудительная первая проверка
        checkAndUpdateButton()
    }

    private fun setupViews() {
        setupTextWatchers()
        setupButtonClickListener()

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkAndUpdateButton()
            }
        }

        binding.emailET.addTextChangedListener(textWatcher)
        binding.passwordET.addTextChangedListener(textWatcher)
    }

    private fun setupButtonClickListener() {
        binding.btnLogin.setOnClickListener {
            if (areAllFieldsValid()) {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.emailET.text.toString().trim()
        val password = binding.passwordET.text.toString().trim()

        if (SharedPreferencesUtils.login(this, email, password)) {
            // Успешный вход
            SharedPreferencesUtils.saveLoginState(this, true)
            Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndUpdateButton() {
        val allFieldsValid = areAllFieldsValid()
        updateButtonState(allFieldsValid)
    }

    private fun areAllFieldsValid(): Boolean {
        val emailValid = isValidEmail(binding.emailET.text.toString().trim())
        val passwordValid = binding.passwordET.text.toString().trim().isNotEmpty()

        return emailValid && passwordValid
    }

    private fun isValidEmail(email: String): Boolean {
        val isValid = if (email.isEmpty()) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        return isValid
    }

    private fun updateButtonState(isEnabled: Boolean) {
        binding.btnLogin.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_login)
            binding.btnLogin.alpha = 1f
        } else {
            binding.btnLogin.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_disable_login)
            binding.btnLogin.alpha = 0.5f
        }

        // Принудительно обновляем отображение
        binding.btnLogin.invalidate()
    }

    private fun checkAutoLogin() {
        if (SharedPreferencesUtils.isLoggedIn(this)) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}