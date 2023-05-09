package com.appvision.newsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.appvision.newsapp.R.id
import com.appvision.newsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarMenu.setupWithNavController(navController, appBarConfiguration)



        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                id.menu_home -> {
                    navController.navigate(id.homepageFragment)
                    return@setOnItemSelectedListener true
                }

                id.menu_bookmarks -> {
                    navController.navigate(id.bookmarksFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == id.articleFragment) {
                binding.bottomNavigation.visibility = View.GONE
                binding.toolbarMenu.visibility = View.VISIBLE
                //    binding.toolbarMenu.inflateMenu(menu.article)

            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.toolbarMenu.visibility = View.GONE
                binding.toolbarMenu.menu.clear()
            }
        }
    }

}




