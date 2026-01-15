package com.example.podgotovka.views.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.podgotovka.R
import com.example.podgotovka.databinding.ActivityMainBinding
import com.example.podgotovka.views.fragments.CatalogFragment
import com.example.podgotovka.views.fragments.HomeFragment
import com.example.podgotovka.views.fragments.ProfileFragment
import com.example.podgotovka.views.fragments.ProjectsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = HomeFragment()
    private val catalogFragment = CatalogFragment()
    private val projectsFragment = ProjectsFragment()
    private val profileFragment = ProfileFragment()

    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFragments()
        setupBottomNavigation()
        setupBackPress()
    }

    private fun setupFragments() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, profileFragment, "profile")
            .hide(profileFragment)
            .add(R.id.frameLayout, projectsFragment, "projects")
            .hide(projectsFragment)
            .add(R.id.frameLayout, catalogFragment, "catalog")
            .hide(catalogFragment)
            .add(R.id.frameLayout, homeFragment, "home")
            .commit()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHome -> switchFragment(homeFragment)
                R.id.menuCatalog -> switchFragment(catalogFragment)
                R.id.menuProjects -> switchFragment(projectsFragment)
                R.id.menuProfile -> switchFragment(profileFragment)
            }
            true
        }
    }

    private fun switchFragment(target: Fragment) {
        if (activeFragment == target) return

        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(target)
            .commit()

        activeFragment = target
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (activeFragment != homeFragment) {
                    binding.bottomNavigationView.selectedItemId = R.id.menuHome
                    switchFragment(homeFragment)
                } else {
                    finish()
                }
            }
        })
    }
}