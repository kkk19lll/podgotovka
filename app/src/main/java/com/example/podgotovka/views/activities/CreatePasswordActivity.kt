package com.example.podgotovka.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.podgotovka.R
import com.example.podgotovka.databinding.ActivityCreatePasswordBinding
import com.example.podgotovka.utils.SharedPreferencesUtils

class CreatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        validateAll() // Первоначальная проверка
        updateButtonState(false) // Инициализация состояния кнопки
    }

    private fun setupListeners() {
        binding.passwordET.addTextChangedListener {
            showPasswordHint()
            validateAll()
        }

        binding.confirmPasswordET.addTextChangedListener { validateAll() }

        binding.btnCreatePassword.setOnClickListener {
            if (validateAll()) completeRegistration()
        }
    }

    private fun validateAll(): Boolean {
        val password = binding.passwordET.text.toString()
        val confirm = binding.confirmPasswordET.text.toString()
        val isValid = isValidPassword(password) && password == confirm

        binding.btnCreatePassword.isEnabled = isValid
        updateButtonState(isValid) // Обновляем внешний вид кнопки
        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() }
    }

    private fun showPasswordHint() {
        val password = binding.passwordET.text.toString()

        if (password.isNotEmpty() && !isValidPassword(password)) {
            binding.passwordET.error = "Нужны: A-Z, a-z, 0-9, спецсимволы"
        } else {
            binding.passwordET.error = null
        }

        // Показ ошибки подтверждения пароля
        val confirm = binding.confirmPasswordET.text.toString()
        if (confirm.isNotEmpty() && password != confirm) {
            binding.confirmPasswordET.error = "Пароли не совпадают"
        } else {
            binding.confirmPasswordET.error = null
        }
    }

    private fun updateButtonState(isEnabled: Boolean) {
        if (isEnabled) {
            binding.btnCreatePassword.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_login)
            binding.btnCreatePassword.alpha = 1f
        } else {
            binding.btnCreatePassword.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_disable_login)
            binding.btnCreatePassword.alpha = 0.5f
        }

        binding.btnCreatePassword.invalidate()
    }

    private fun completeRegistration() {
        val password = binding.passwordET.text.toString()
        SharedPreferencesUtils.savePassword(this, password)

        Toast.makeText(this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}