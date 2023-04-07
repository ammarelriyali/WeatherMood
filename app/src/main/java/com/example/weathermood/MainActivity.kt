package com.example.weathermood

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.example.weathermood.shareperf.MySharedPreference
import com.google.android.material.navigation.NavigationView
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    var layoutDirection:Int?= null
    companion object{
        var isNotOpen=true;
    }
    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null && isNotOpen) {
            MySharedPreference.getInstance(newBase)
            setLocal(MySharedPreference.getLanguage(), newBase)
            isNotOpen=false
        }
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutDirection!=null)
            window.decorView.layoutDirection = layoutDirection!!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content)




        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_alert, R.id.nav_setting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { navController: NavController?, navDestination: NavDestination, bundle: Bundle? ->
            val isMatch: Boolean = navDestination.label == "fragment_maps"
            setVisibilityToolbar(isMatch)
        }
    }

    private fun setVisibilityToolbar(show: Boolean) {
        Log.i("TAGG", "setVisibilityToolbar: $show")
        binding.appBar.toolbar.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setLocal(lang: String, newBase: Context) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        newBase.resources.updateConfiguration(
            config,
            newBase.resources.displayMetrics
        )

        // Determine layout direction based on selected language
        layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

    }

}