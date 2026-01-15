package com.example.podgotovka.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesUtils {
    private const val PREFS_NAME = "user_preferences"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_MIDDLE_NAME = "middle_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_DATE_BIRTH = "date_birth"
    private const val KEY_GENDER = "gender"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_IS_USER_CREATED = "is_user_created"
    private const val KEY_IS_LOGGED_IN = "is_logged_in" // Добавляем ключ для состояния входа

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Сохранение данных из RegisterActivity
    fun saveUserData(
        context: Context,
        firstName: String,
        middleName: String,
        lastName: String,
        dateBirth: String,
        gender: String,
        email: String
    ) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.putString(KEY_MIDDLE_NAME, middleName)
        editor.putString(KEY_LAST_NAME, lastName)
        editor.putString(KEY_DATE_BIRTH, dateBirth)
        editor.putString(KEY_GENDER, gender)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
        Log.d("SharedPreferencesUtils", "Данные пользователя сохранены")
    }

    // Сохранение пароля из CreatePasswordActivity
    fun savePassword(context: Context, password: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_IS_USER_CREATED, true)
        editor.apply()
        Log.d("SharedPreferencesUtils", "Пароль сохранен, пользователь создан")
    }

    // Получение данных пользователя
    fun getUserData(context: Context): UserData {
        val prefs = getSharedPreferences(context)
        return UserData(
            firstName = prefs.getString(KEY_FIRST_NAME, "") ?: "",
            middleName = prefs.getString(KEY_MIDDLE_NAME, "") ?: "",
            lastName = prefs.getString(KEY_LAST_NAME, "") ?: "",
            dateBirth = prefs.getString(KEY_DATE_BIRTH, "") ?: "",
            gender = prefs.getString(KEY_GENDER, "") ?: "",
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            password = prefs.getString(KEY_PASSWORD, "") ?: "" // Добавляем пароль в модель
        )
    }

    // Получение только пароля
    fun getPassword(context: Context): String {
        return getSharedPreferences(context).getString(KEY_PASSWORD, "") ?: ""
    }

    // Проверка, создан ли пользователь
    fun isUserCreated(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_USER_CREATED, false)
    }

    // АВТОРИЗАЦИЯ - новые методы
    fun login(context: Context, email: String, password: String): Boolean {
        val userData = getUserData(context)
        val storedPassword = getPassword(context)

        return userData.email == email && storedPassword == password
    }

    fun saveLoginState(context: Context, isLoggedIn: Boolean) {
        getSharedPreferences(context).edit()
            .putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout(context: Context) {
        saveLoginState(context, false)
    }

    // Очистка всех данных (для выхода/удаления аккаунта)
    fun clearUserData(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
        Log.d("SharedPreferencesUtils", "Данные пользователя очищены")
    }

    data class UserData(
        val firstName: String,
        val middleName: String,
        val lastName: String,
        val dateBirth: String,
        val gender: String,
        val email: String,
        val password: String = ""
    )
}