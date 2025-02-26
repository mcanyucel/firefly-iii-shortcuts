package com.mustafacanyucel.fireflyiiishortcuts

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mustafacanyucel.fireflyiiishortcuts.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
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

        handleIntent(intent)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData = intent.data

        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            if (appLinkData.host == "fireflyiiishortcuts.mustafacanyucel.com"
                && appLinkData.path == "/oauth2redirect") {
                Toast.makeText(this, "Processing authentication...", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch {
                    val success = authManager.handleAuthorizationResponse(appLinkData)

                    val message = if (success) "Authentication successful!" else "Authentication failed!"
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()

                    // navigate to settings for an optional sync
                    if (success) {
                        binding.navView.selectedItemId = R.id.navigation_settings
                    }
                }
            }
        }
    }
}