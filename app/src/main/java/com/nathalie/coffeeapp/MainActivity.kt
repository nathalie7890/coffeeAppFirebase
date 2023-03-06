package com.nathalie.coffeeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.nathalie.coffeeapp.data.service.AuthService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var authRepo: AuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

//        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
//        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
//
//        val navigationView = findViewById<NavigationView>(R.id.navigationView)
//        navigationView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        if (!authRepo.isAuthenticate()) {
            findNavController(R.id.navHostFragment).navigate(R.id.loginFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }
}