package com.mustafacanyucel.fireflyiiishortcuts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mustafacanyucel.fireflyiiishortcuts.databinding.ActivityMainBinding
import com.mustafacanyucel.fireflyiiishortcuts.services.auth.Oauth2Manager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authManager: Oauth2Manager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleAuthIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthIntent(intent)
    }


    private fun handleAuthIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            // Detailed logging
            Log.d("MainActivity", "Intent data: ${intent.data}")
            Log.d("MainActivity", "oauth_canceled: ${intent.getBooleanExtra("oauth_canceled", false)}")
            Log.d("MainActivity", "All extras: ${intent.extras?.keySet()?.joinToString()}")

            // Check if there's an error parameter in the URI
            val errorParam = intent.data?.getQueryParameter("error")
            if (errorParam != null) {
                Log.d("MainActivity", "Error parameter in URI: $errorParam")
            }

            lifecycleScope.launch {
                val success = authManager.handleAuthorizationResponse(intent)

                val message =
                    if (success) "Authentication successful!" else "Authentication failed!"
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()

                if (success) {
                    binding.navView.selectedItemId = R.id.navigation_settings
                }
            }
        }
    }
}