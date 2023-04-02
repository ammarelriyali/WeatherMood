package com.example.weathermood

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weathermood.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.util.*
import java.util.function.IntPredicate

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content)

        val dialogsIds = intArrayOf(
            R.id.action_nav_home_to_maps_fragment,
            R.id.action_nav_favourite_to_maps_fragment
            )

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_alert,R.id.nav_setting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { navController: NavController?, navDestination: NavDestination, bundle: Bundle? ->
            val isMatch: Boolean = Arrays.stream(dialogsIds).anyMatch(
                 { id: Int -> navDestination.id == id })
                setVisibilityToolbar(isMatch)
        }
    }

    private fun setVisibilityToolbar(show:Boolean) {
       binding.appBar.toolbar.visibility= View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}