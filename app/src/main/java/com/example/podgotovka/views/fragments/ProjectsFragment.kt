package com.example.podgotovka.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.podgotovka.databinding.FragmentProjectsBinding

class ProjectsFragment : Fragment() {

    private lateinit var binding: FragmentProjectsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProjectsBinding.inflate(inflater, container, false)
        return binding.root
    }
}