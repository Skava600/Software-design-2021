package com.example.tabatatimer.activity

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.example.tabatatimer.NavigationDirections
import com.example.tabatatimer.R
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val language = prefs.getString(getString(R.string.language), "en")
        val resources: Resources = (this as Context).resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration

        config.setLocale(Locale(language!!.lowercase(Locale.ROOT)))

        val fontScale = prefs.getString(getString(R.string.font_scale), "1")!!.toFloat()
        config.fontScale = fontScale

        resources.updateConfiguration(config, dm)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val darkTheme = prefs.getBoolean(getString(R.string.dark_theme), false)
        if (darkTheme)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val action = NavigationDirections.actionToPreferenceFragment()
                findNavController(R.id.navHostFragment).navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}