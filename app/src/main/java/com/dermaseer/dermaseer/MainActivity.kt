package com.dermaseer.dermaseer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dermaseer.dermaseer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding

   private val requestPermissionLauncher =
      registerForActivityResult(
         ActivityResultContracts.RequestPermission()
      ) { isGranted: Boolean ->
         if (isGranted) {

         } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
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
      binding.navView.menu.getItem(2).title = "Scan"
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
      requestPermissionLauncher.launch(Manifest.permission.CAMERA)
      binding.fabScan.setOnClickListener {
         if (allPermissionsGranted()) {
            navController.navigate(R.id.cameraFragment)
         } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
         }
      }
   }

   @Deprecated("Deprecated in Java")
   override fun onBackPressed() {
      val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
      val currentFragmentId = navHostFragment.navController.currentDestination?.id

      when (currentFragmentId) {
         R.id.homeFragment,
         R.id.articleFragment,
         R.id.historyFragment,
         R.id.profileFragment,
         R.id.onBoardingFragment,
         R.id.scanResultRecomendationFragment2,
         R.id.completeProfileFragment -> { showAlertDialog() }
         else -> super.onBackPressed()
      }
   }

   @SuppressLint("SetTextI18n")
   private fun showAlertDialog() {
      val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
      val inflater = LayoutInflater.from(this)
      val customView = inflater.inflate(R.layout.custom_alert_dialog, null)

      val titleView = customView.findViewById<TextView>(R.id.dialog_title)
      val messageView = customView.findViewById<TextView>(R.id.dialog_message)
      val positiveBtn = customView.findViewById<Button>(R.id.btn_positive)
      val negativeBtn = customView.findViewById<Button>(R.id.btn_negative)

      titleView.text = this.getString(R.string.confirm_exit)
      messageView.text = this.getString(R.string.are_you_sure_exit)
      positiveBtn.text = this.getString(R.string.yes)
      negativeBtn.text = this.getString(R.string.no)

      val dialog = builder.setView(customView).create()
      positiveBtn.setOnClickListener {
         finish()
         dialog.dismiss()
      }
      negativeBtn.setOnClickListener { dialog.dismiss() }
      dialog.window?.setBackgroundDrawable(ColorDrawable(0))
      dialog.show()
   }
}