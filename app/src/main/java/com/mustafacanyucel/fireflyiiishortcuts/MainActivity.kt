package com.mustafacanyucel.fireflyiiishortcuts

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mustafacanyucel.fireflyiiishortcuts.databinding.ActivityMainBinding
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import com.mustafacanyucel.fireflyiiishortcuts.ui.management.ShortcutModelDetailHostActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.Manifest
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authManager: Oauth2Manager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.manageShortcutsFab.setOnClickListener {
            startActivity(Intent(this, ShortcutModelDetailHostActivity::class.java))
        }

        findNavController(R.id.nav_host_fragment_activity_main)
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.navigation_home -> binding.manageShortcutsFab.show()
                    else -> binding.manageShortcutsFab.hide()
                }
            }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_settings,
                R.id.navigation_sync
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleAuthIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted
                    Log.d(TAG, "Notification permission already granted")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // User previously declined, but didn't check "Don't ask again"
                    // Show a dialog explaining why you need this permission
                    showNotificationPermissionRationale()
                }
                else -> {
                    // First time asking or user checked "Don't ask again" and denied
                    // Request permission directly
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        REQUEST_NOTIFICATION_PERMISSION
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotificationPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission")
            .setMessage("This app needs notification permission to alert you about shortcut executions and their status.")
            .setPositiveButton("Grant") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                // Explain the consequences of not having notifications
                Toast.makeText(this, "You won't receive shortcut execution updates", Toast.LENGTH_LONG).show()
            }
            .show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, notifications will work
                Log.d(TAG, "Notification permission granted")
            } else {
                // Permission denied, notifications won't work
                Log.d(TAG, "Notification permission denied")
                // You might want to show a dialog explaining why notifications are important
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        Log.d(TAG, "Intent received: action=${intent.action}, data=${intent.data}")

        when {
            // handle custom schemes
            intent.data?.scheme == "fireflyshortcuts" && intent.data?.host == "oauth2callback" -> {
                val code = intent.data?.getQueryParameter("code")
                val state = intent.data?.getQueryParameter("state")

                Log.d(TAG, "Custom scheme detected: code=$code, state=$state")

                if (code != null) {
                    handleAuthCode(code, state)
                }
            }
            intent.action == Intent.ACTION_VIEW -> {
                Log.d(TAG, "HTTPS scheme detected")
                handleAuthIntent(intent)
            }
        }
    }

    private fun handleAuthCode(code: String, state: String?) {
        lifecycleScope.launch {
            try {
                val success = authManager.handleManualAuthCode(code, state)
                Toast.makeText(this@MainActivity,
                    if (success) getString(R.string.auth_success) else getString(R.string.auth_fail),
                    Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Auth code processing error", e)
                Toast.makeText(
                    this@MainActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleAuthIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            // Detailed logging
            Log.d("MainActivity", "Intent data: ${intent.data}")
            Log.d(
                "MainActivity",
                "oauth_canceled: ${intent.getBooleanExtra("oauth_canceled", false)}"
            )
            Log.d("MainActivity", "All extras: ${intent.extras?.keySet()?.joinToString()}")

            // Check if there's an error parameter in the URI
            val errorParam = intent.data?.getQueryParameter("error")
            if (errorParam != null) {
                Log.d("MainActivity", "Error parameter in URI: $errorParam")
            }

            lifecycleScope.launch {
                val success = authManager.handleAuthorizationResponse(intent)

                val message =
                    if (success) getString(R.string.auth_success) else getString(R.string.auth_fail)
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()

                if (success) {
                    binding.navView.selectedItemId = R.id.navigation_settings
                }
            }
        }
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 123
        private const val TAG = "MainActivity"
    }
}