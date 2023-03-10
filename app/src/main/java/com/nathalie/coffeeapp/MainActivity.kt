package com.nathalie.coffeeapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.nathalie.coffeeapp.data.StartingBeans
import com.nathalie.coffeeapp.data.service.AuthService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
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

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            authRepo.deAuthenticate()
            findNavController(R.id.navHostFragment).navigate(R.id.to_login_fragment)

            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (!authRepo.isAuthenticate()) {
            findNavController(R.id.navHostFragment).navigate(R.id.to_login_fragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }
}