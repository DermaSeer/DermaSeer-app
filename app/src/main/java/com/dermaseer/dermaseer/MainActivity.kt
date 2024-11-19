package com.dermaseer.dermaseer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dermaseer.dermaseer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding

   private val requestPermissionLauncher =
      registerForActivityResult(
         ActivityResultContracts.RequestPermission()
      ) { isGranted: Boolean ->
         if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
         } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
         }
      }

   private fun allPermissionsGranted() =
      ContextCompat.checkSelfPermission(
         this,
         Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED

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

      binding.fabScan.setOnClickListener {
         if (allPermissionsGranted()) {
            navController.navigate(R.id.cameraFragment)
         } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
         }
      }
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