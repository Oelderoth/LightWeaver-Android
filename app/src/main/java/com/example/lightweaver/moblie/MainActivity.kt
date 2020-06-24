package com.example.lightweaver.moblie

import android.animation.Animator
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val topLevelDestinations = setOf(R.id.nav_devices, R.id.nav_device_groups, R.id.nav_scenes, R.id.nav_palette, R.id.nav_settings)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setupFabWithNavigationController(navController, fab, listOf(R.id.nav_devices)) { _, destinationId ->
            when (destinationId) {
                R.id.nav_devices -> navController.navigate(R.id.action_start_create_device)
            }
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

        if (visibleFab?.id != R.id.fab_add) {
            findViewById<FloatingActionButton>(R.id.fab_add).hide()
        }

        visibleFab?.show()

        // TODO: Once we switch between FABs use this to get the pop-in and pop-out transition
        //findViewById<FloatingActionButton>(R.id.fab_add).hide(object : FloatingActionButton.OnVisibilityChangedListener() {
        //    override fun onHidden(fab: FloatingActionButton?) {
        //        fab?.show()
        //    }
        //})
    }


    class NavigationAwareFloatingActionButtonListener(private val visibleDestinationIds: Collection<Int>, private val fab: FloatingActionButton): Animator.AnimatorListener, NavController.OnDestinationChangedListener, View.OnClickListener {
        private var currentDestination: Int? = null
        private val onClickListeners = mutableListOf<(view: View, destinationId: Int?)->Unit>()

        override fun onAnimationRepeat(a: Animator) = Unit
        override fun onAnimationEnd(a: Animator) = Unit
        override fun onAnimationCancel(a: Animator) = Unit
        override fun onAnimationStart(animator: Animator) {
            if (!visibleDestinationIds.contains(currentDestination)) {
                animator.cancel()
            }
        }

        override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
            currentDestination = destination.id

            if (visibleDestinationIds.contains(destination.id)) {
                fab.show()
            } else {
                fab.hide()
            }
        }

        fun addOnClickListener(listener: (view: View, destinationId: Int?) -> Unit) {
            onClickListeners.add(listener)
        }

        override fun onClick(v: View) {
            for (listener in onClickListeners) {
                listener(v, currentDestination)
            }
        }
    }

    private fun setupFabWithNavigationController(navController: NavController, fab: FloatingActionButton, visibleDestinationIds: Collection<Int>) {
        val listener = NavigationAwareFloatingActionButtonListener(visibleDestinationIds, fab)
        fab.addOnShowAnimationListener(listener)
        navController.addOnDestinationChangedListener(listener)
    }

    private fun setupFabWithNavigationController(navController: NavController, fab: FloatingActionButton, visibleDestinationIds: Collection<Int>, onClickListener: (view: View, destinationId: Int?) -> Unit) {
        val listener = NavigationAwareFloatingActionButtonListener(visibleDestinationIds, fab)
        listener.addOnClickListener(onClickListener)
        fab.addOnShowAnimationListener(listener)
        fab.setOnClickListener(listener)
        navController.addOnDestinationChangedListener(listener)
    }
}