package com.example.podgotovka.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.podgotovka.R
import com.example.podgotovka.databinding.ActivityRegisterBinding
import com.example.podgotovka.utils.SharedPreferencesUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTextWatchers()
        setupSpinner()
        setupButtonClickListener()
        // Принудительная первая проверка
        checkAndUpdateButton()
    }

    private fun setupTextWatchers() {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                checkAndUpdateButton()
            }
        }

        binding.firstNameET.addTextChangedListener(textWatcher)
        binding.middleNameET.addTextChangedListener(textWatcher)
        binding.lastNameET.addTextChangedListener(textWatcher)
        binding.dateBirthET.addTextChangedListener(textWatcher)
        binding.emailET.addTextChangedListener(textWatcher)
    }

    private fun setupSpinner() {

        // Проверяем адаптер
        val adapter = binding.spinnerGender.adapter
        if (adapter == null) {
            Log.e("RegisterActivity", "Адаптер Spinner равен NULL!")
        } else {
            Log.d("RegisterActivity", "Адаптер Spinner: count=${adapter.count}")
            for (i in 0 until adapter.count) {
                Log.d("RegisterActivity", "  Item $i: '${adapter.getItem(i)}'")
            }
        }

        // Проверяем текущую позицию
        val currentPosition = binding.spinnerGender.selectedItemPosition

        // Устанавливаем слушатель
        binding.spinnerGender.onItemSelectedListener = object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = binding.spinnerGender.selectedItem?.toString() ?: ""
                Log.d("RegisterActivity", "Выбранный элемент: '$selectedItem'")
                checkAndUpdateButton()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                Log.d("RegisterActivity", "Spinner onNothingSelected")
                checkAndUpdateButton()
            }
        }

        // Принудительно вызываем проверку после настройки
        checkAndUpdateButton()
    }

    private fun setupButtonClickListener() {
        binding.btnRegister.setOnClickListener {
            if (areAllFieldsValid()) {
                saveUserData()
                navigateToCreatePassword()
            }
        }
    }

    private fun saveUserData() {
        val firstName = binding.firstNameET.text.toString().trim()
        val middleName = binding.middleNameET.text.toString().trim()
        val lastName = binding.lastNameET.text.toString().trim()
        val dateBirth = binding.dateBirthET.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem?.toString() ?: ""
        val email = binding.emailET.text.toString().trim()

        SharedPreferencesUtils.saveUserData(
            context = this,
            firstName = firstName,
            middleName = middleName,
            lastName = lastName,
            dateBirth = dateBirth,
            gender = gender,
            email = email
        )
    }

    private fun navigateToCreatePassword() {
        val intent = Intent(this, CreatePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun checkAndUpdateButton() {
        val allFieldsValid = areAllFieldsValid()

        updateButtonState(allFieldsValid)
    }

    private fun areAllFieldsValid(): Boolean {
        // Проверяем каждое поле отдельно с логированием
        val firstNameValid = binding.firstNameET.text.toString().trim().isNotEmpty()
        val middleNameValid = binding.middleNameET.text.toString().trim().isNotEmpty()
        val lastNameValid = binding.lastNameET.text.toString().trim().isNotEmpty()
        val dateBirthValid = binding.dateBirthET.text.toString().trim().isNotEmpty()
        val emailValid = isValidEmail(binding.emailET.text.toString().trim())

        // Проверяем Spinner - выбран ли реальный пол (не заглушка)
        val genderValid = isGenderValid()

        return firstNameValid &&
                middleNameValid &&
                lastNameValid &&
                dateBirthValid &&
                emailValid &&
                genderValid
    }

    private fun isGenderValid(): Boolean {
        val position = binding.spinnerGender.selectedItemPosition
        val adapter = binding.spinnerGender.adapter

        if (adapter == null || adapter.count == 0) {
            return false
        }

        // Проверяем, что выбрана не первая позиция (предполагая, что первая - заглушка)
        // ИЛИ проверяем по тексту
        if (position <= 0) {
            return false
        }

        // Дополнительная проверка по тексту
        val selectedText = binding.spinnerGender.selectedItem?.toString() ?: ""
        val invalidValues = listOf("", "Выберите пол", "Select gender", "Пол", "Gender")

        return !invalidValues.contains(selectedText.trim())
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

        binding.btnRegister.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnRegister.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_login)
            binding.btnRegister.alpha = 1f
        } else {
            binding.btnRegister.background = ContextCompat.getDrawable(this, R.drawable.shape_btn_disable_login)
            binding.btnRegister.alpha = 0.5f
        }

        // Принудительно обновляем отображение
        binding.btnRegister.invalidate()
    }
}