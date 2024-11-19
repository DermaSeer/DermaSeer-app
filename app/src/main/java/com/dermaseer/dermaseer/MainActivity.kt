package com.dermaseer.dermaseer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dermaseer.dermaseer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)
      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
         val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
         v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
         insets
      }
      binding.navView.menu.getItem(2).isEnabled = false
      val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
      val navController = navHostFragment.navController
      navController.addOnDestinationChangedListener { _, destination, _ ->
         when (destination.id) {
            R.id.homeFragment, R.id.articleFragment, R.id.historyFragment, R.id.profileFragment, -> {
               binding.bottomAppBar.visibility = View.VISIBLE
               binding.fabScan.visibility = View.VISIBLE
            }
            else -> {
               binding.bottomAppBar.visibility = View.GONE
               binding.fabScan.visibility = View.GONE
            }
         }
      }
      binding.navView.setupWithNavController(navController)
   }

   @Deprecated("Deprecated in Java")
   override fun onBackPressed() {
      val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
      val currentFragmentId = navHostFragment.navController.currentDestination?.id

      if (currentFragmentId == R.id.homeFragment ||
         currentFragmentId == R.id.articleFragment ||
         currentFragmentId == R.id.historyFragment ||
         currentFragmentId == R.id.profileFragment) {
         finish()
      } else {
         super.onBackPressed()
      }
   }
}