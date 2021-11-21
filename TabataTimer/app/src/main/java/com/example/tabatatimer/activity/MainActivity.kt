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
import java.util.*
import android.R
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val language = prefs.getString(getString(com.example.tabatatimer.R.string.language), "en")
        val resources: Resources = (this as Context).resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration

        config.setLocale(Locale(language!!.lowercase(Locale.ROOT)))

        val fontScale = prefs.getString(getString(com.example.tabatatimer.R.string.font_scale), "1")!!.toFloat()
        config.fontScale = fontScale

        resources.updateConfiguration(config, dm)

        super.onCreate(savedInstanceState)
        setContentView(com.example.tabatatimer.R.layout.activity_main)
        val navController = this.findNavController(com.example.tabatatimer.R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


        val darkTheme = prefs.getBoolean(getString(com.example.tabatatimer.R.string.dark_theme), false)
        if (darkTheme)
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        //configureToolbar()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(com.example.tabatatimer.R.id.navHostFragment)
        return navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.example.tabatatimer.R.id.action_settings -> {
                val action = NavigationDirections.actionToPreferenceFragment()
                findNavController(com.example.tabatatimer.R.id.navHostFragment).navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(com.example.tabatatimer.R.menu.menu, menu)
        return true
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById<View>(com.example.tabatatimer.R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
    }
}