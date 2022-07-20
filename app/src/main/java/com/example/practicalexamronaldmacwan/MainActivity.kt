package com.example.practicalexamronaldmacwan

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practicalexamronaldmacwan.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_add_category, R.id.nav_add_contact, R.id.nav_contact_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        drawerLayout.setScrimColor(ContextCompat.getColor(this, android.R.color.transparent))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)

        drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
            }
            override fun onDrawerOpened(drawerView: View) {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
            }
            override fun onDrawerClosed(drawerView: View) {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
            }
            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_host_fragment_content_main) {
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.menu)
            }
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}