package com.example.podgotovka.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.podgotovka.databinding.FragmentProfileBinding
import com.example.podgotovka.utils.SharedPreferencesUtils

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загружаем данные пользователя из SharedPreferences
        loadUserData()
    }

    private fun loadUserData() {
        // Получаем контекст (используем requireContext() для безопасности)
        val context = requireContext()

        // Получаем данные пользователя из SharedPreferences
        val userData = SharedPreferencesUtils.getUserData(context)

        // Формируем полное имя пользователя
        // Можно объединить фамилию, имя и отчество в нужном порядке

        val displayName = userData.firstName.ifBlank { "Пользователь" }

        // Устанавливаем данные в TextView
        binding.titleTextViewProfile.text = displayName
        binding.subTitleTextViewProfile.text = userData.email

        // Логирование для отладки
        println("Загружены данные пользователя: Имя=$displayName, Email=${userData.email}")
    }
}