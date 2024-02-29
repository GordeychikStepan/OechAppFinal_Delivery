package com.example.oechAppFinal.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.oechAppFinal.R
import com.example.oechAppFinal.databinding.ActivityMainBinding
import com.example.oechAppFinal.model.Constances
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(getSavedThemeState())
        {
            setTheme(R.style.Theme_OechAppFinal_Night)
            binding.navView.setBackgroundColor(resources.getColor(R.color.black, theme))
        }
        else
        {
            setTheme(R.style.Theme_OechAppFinal)
        }

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_wallet,
                R.id.navigation_track,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(Constances.NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(Constances.KEY, false)
    }

    override fun onResume() {
        super.onResume()
        val pref = applicationContext.getSharedPreferences("RedirectData", 0)
        when (pref.getInt("nav_id", 0)){
            2131362211 -> {
                binding.navView.selectedItemId = R.id.navigation_home
                pref.edit().clear().apply()
            }
            2131362214 -> {
                binding.navView.selectedItemId = R.id.navigation_wallet
                pref.edit().clear().apply()
            }
            2131362213 -> {
                binding.navView.selectedItemId = R.id.navigation_track
                pref.edit().clear().apply()
            }
            2131362212 -> {
                binding.navView.selectedItemId = R.id.navigation_profile
                pref.edit().clear().apply()
            }
        }
    }
}