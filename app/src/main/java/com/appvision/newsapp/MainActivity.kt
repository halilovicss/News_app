package com.appvision.newsapp

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.appvision.newsapp.databinding.ActivityMainBinding
import com.appvision.newsapp.extensions.listOfHidden


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.home, R.navigation.bookmarks)
        val controller = binding.bottomNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
        )

        controller.observe(this) {
            binding.toolbarMenu.title = ""
            binding.toolbarMenu.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
            setSupportActionBar(binding.toolbarMenu)
            it.addOnDestinationChangedListener { _, destination, _ ->
                hideToolbar(destination.id)
            }
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun hideToolbar(id: Int) {
        binding.toolbarMenu.visibility = if (id in listOfHidden) {
            GONE
        } else {
            VISIBLE
        }
    }
}



