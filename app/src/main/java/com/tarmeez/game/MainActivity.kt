package com.tarmeez.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tarmeez.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    lateinit var fab: FloatingActionButton
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var bottomAppBar: BottomAppBar
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

        supportActionBar?.hide()
        fab = findViewById(R.id.fab)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomAppBar = findViewById(R.id.bottomAppBar)

        setUpNavigationController()

        setupUserInformation()
    }


    private fun setUpNavigationController(){
        navController =
            Navigation.findNavController(this, R.id.fragment_container)
        bottomNavigationView.background = null

        fab.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
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

            if(destination.id in fragmentsWithHeader){
                binding.headerLayout.rootHeaderLayout.visibility = View.VISIBLE
            }else{
                binding.headerLayout.rootHeaderLayout.visibility = View.GONE
            }
        }
    }


    private fun setupUserInformation(){
        val gender = "أنثى"
        if(gender == "ذكر"){
            binding.headerLayout.profileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.boy_avatar))
        }else if(gender == "أنثى"){
            binding.headerLayout.profileImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.girl_avatar))
        }
        binding.headerLayout.score.text = "100"
        binding.headerLayout.username.text = "Sahar"
    }
}