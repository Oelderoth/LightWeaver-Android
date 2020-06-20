package com.example.lightweaver.moblie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        findViewById<FloatingActionButton>(R.id.fab_add)?.setOnClickListener { view ->
            Snackbar.make(view, "Added a thing", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val topLevelDestinations = setOf(R.id.nav_devices, R.id.nav_device_groups, R.id.nav_scenes, R.id.nav_palette, R.id.nav_settings)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            showActiveFloatingActionButton(destination.id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showActiveFloatingActionButton(destinationId: Int) {
        // Since the FABs must exist at a higher level than the Fragment to behave properly
        // determine the appropriate FAB for the current destination and hide all others
        var visibleFab: FloatingActionButton? = when (destinationId) {
            R.id.nav_devices -> findViewById(R.id.fab_add)
            else -> null
        }

        if (visibleFab?.id != R.id.fab_add) findViewById<FloatingActionButton>(R.id.fab_add).hide()

        visibleFab?.show()

        // TODO: Once we switch between FABs use this to get the pop-in and pop-out transition
        //findViewById<FloatingActionButton>(R.id.fab_add).hide(object : FloatingActionButton.OnVisibilityChangedListener() {
        //    override fun onHidden(fab: FloatingActionButton?) {
        //        fab?.show()
        //    }
        //})
    }
}