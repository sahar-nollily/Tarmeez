package com.tarmeez.game

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.AndroidEntryPoint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.tarmeez.game.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    private val fragmentsWithBottomNavigationView = listOf(
        R.id.homeFragment,
        R.id.profileFragment,
        R.id.galleryFragment
    )

    private val fragmentsWithHeader = listOf(
        R.id.homeFragment,
        R.id.galleryFragment,
        R.id.matchingGameFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigationController()
        setupUserInformation()
    }

    private fun setUpNavigationController() {
        navController =
            Navigation.findNavController(this, R.id.fragment_container)
        binding.bottomNavigationView.background = null

        binding.fab.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
        findNavController(R.id.fragment_container)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id in fragmentsWithBottomNavigationView) {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.fab.visibility = View.VISIBLE
                binding.bottomAppBar.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationView.visibility = View.GONE
                binding.fab.visibility = View.GONE
                binding.bottomAppBar.visibility = View.GONE

            }

            if (destination.id in fragmentsWithHeader) {
                binding.headerLayout.rootHeaderLayout.visibility = View.VISIBLE
            } else {
                binding.headerLayout.rootHeaderLayout.visibility = View.GONE
            }
        }
    }

    private fun setupUserInformation() {
        val gender = "ss"
        if (gender == "ذكر") {
            binding.headerLayout.profileImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.boy_avatar
                )
            )
        } else if (gender == "أنثى") {
            binding.headerLayout.profileImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.girl_avatar
                )
            )
        } else
            binding.headerLayout.profileImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.profile_picture
                )
            )
        binding.headerLayout.score.text = "100"
        binding.headerLayout.username.text = "Sahar"
    }

    fun hideKeyBoard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}