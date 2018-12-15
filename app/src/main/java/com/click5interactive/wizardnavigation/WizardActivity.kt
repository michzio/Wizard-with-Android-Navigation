package com.click5interactive.wizardnavigation

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.MenuItem
import android.view.OrientationEventListener
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.click5interactive.wizardnavigation.R.id.nav_view
import kotlinx.android.synthetic.main.activity_wizard.*
import kotlinx.android.synthetic.main.activity_wizard.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_wizard.*
import kotlinx.android.synthetic.main.content_wizard_header.*
import com.google.android.material.navigation.NavigationView

abstract class WizardActivity : HamburgerMenuActivity(), NavigationView.OnNavigationItemSelectedListener {

    //region MODEL
    abstract protected var model : WizardModel
    //endregion

    //region LIFE CYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard)
        setupHamburgerMenuActivity()
    }

    override fun onResume() {
        super.onResume()
        orientationListener.enable()
    }

    override fun onPause() {
        orientationListener.disable()
        super.onPause()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        hasOrientationChanged = savedInstanceState?.getBoolean("hasOrientationChanged") ?: false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean("hasOrientationChanged", hasOrientationChanged)
        super.onSaveInstanceState(outState)
    }
    //endregion

    //region NAVIGATION CONTROLLER
    override fun configureNavigationController() {
        super.configureNavigationController()

        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(model.wizardGraph)
        navController.addOnDestinationChangedListener(this::onNavigatedListener)
        //navController.addOnNavigatedListener(this::onNavigatedListener)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)
        if(hamburgerFragments.contains(navController.currentDestination?.id)) {
            navController.popBackStack()
        }

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.menu1Fragment -> {
                navController.navigate(R.id.menu1Fragment)
            }
            R.id.menu2Fragment -> {
                navController.navigate(R.id.menu2Fragment)
            }
            R.id.menu3Fragment -> {
                navController.navigate(R.id.menu3Fragment)
            }
            else -> {
                navController.navigate(item.itemId)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.END)
        return true
    }

    private var lastDestination: NavDestination? = null

    private fun onNavigatedListener(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if(hamburgerFragments.contains(destination.id)) {
            wizard_header.visibility = View.GONE
            backButton.visibility = View.VISIBLE
        } else if(model.hasDestination(destination.id)) {
            wizard_header.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE

            setStepLabel(destinationId = destination.id)
            setStepProgress(destinationId = destination.id)

            lastDestination?.let { oldDestination ->
                val oldDestinationIdx = model.destinationIndex(oldDestination.id)
                val newDestinationIdx = model.destinationIndex(destination.id)

                if(oldDestinationIdx > newDestinationIdx) {
                    onWizardBackwardNavigation(oldDestination, destination)
                } else if(newDestinationIdx > oldDestinationIdx){
                    onWizardForwardNavigation(oldDestination, destination)
                }
            }
            lastDestination = destination

        } else if(model.confirm.destination == destination.id) {
            wizard_header.visibility = View.GONE
            backButton.visibility = View.INVISIBLE
        } else {
            //throw IllegalArgumentException("Illegal wizard destination!")
        }
    }

    protected fun navigateTo(step: Int) {
        val navController = findNavController(R.id.nav_host_fragment)

        if(step > 0 && step < model.steps.count()) {
            // go to step-th fragment
            if(navController.currentDestination?.id != model.steps.first().destination) return

            for(i in 0 until step) {
                navController.navigate(model.steps[i].action)
            }
            //    for( i in 1..step) {
            //        navController.navigate(model.steps[i].destination)
            //    }
        } else if(step == model.steps.count()) {
            // go to confirm fragment
            navController.navigate(model.confirm.destination)
        } else {
            // start at step 0
        }
    }

    protected fun isCurrentDestinationConfirm() : Boolean {
        return findNavController(R.id.nav_host_fragment).currentDestination?.id == model.confirm.destination
    }

    fun onWizardBackwardNavigation(oldDestination: NavDestination, newDestination: NavDestination) {
        Log.d(TAG, "Detected backward navigation in wizard.")
    }
    fun onWizardForwardNavigation(oldDestination: NavDestination, newDestination: NavDestination) {
        Log.d(TAG, "Detected forward navigation in wizard.")
    }
    //endregion

    //region WIZARD FUNCTIONS
    private fun setStepLabel(destinationId : Int) {
        val stepNumber = model.destinationIndex(destinationId) + 1
        val stepsCount = model.steps.count()

        val text = "Step $stepNumber of $stepsCount"
        val label = SpannableString(text)
        label.setSpan(StyleSpan(Typeface.BOLD), 5, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        //stepTextView.text = label
    }

    private fun setStepProgress(destinationId : Int) {
        val stepNumber = model.destinationIndex(destinationId) + 1
        val stepsCount = model.steps.count()

        if(stepsCount > 0) {
            progressBar.progress = stepNumber * 100 / stepsCount
        } else {
            progressBar.progress = 0
        }
    }
    //endregion

    //region TOOLBAR, TOGGLE and DRAWER
    override fun configureToolbarMenu() {
        // Action Menu View - assign main Menu

    }
    //endregion

    //region BACK PRESSED - CUSTOM BACK STACK HANDLING
    override fun onBackPressed() {
        if (isCurrentDestinationConfirm()) return
        super.onBackPressed()
       /* if(hasOrientationChanged) {
            Log.d(TAG, "Normal back press navigation")
            // new addtional back press handling after any orientation change
            super.onBackPressed()
        } else {
            Log.d(TAG, "Custom back press navigation")
            // old back press handling
            if (!findNavController(R.id.nav_host_fragment).popBackStack()) {
                super.onBackPressed()
            }
        }*/
    }
    //endregion

    //region ORIENTATION CHANGES LISTENER
    private val orientationListener : OrientationEventListener by lazy {
        object: SimpleOrientationEventListener(this@WizardActivity) {
            override fun onOrientationChanged(oldOrientation: Int, newOrientation: Int) {
                Log.d(TAG,"Orientation changed from: ${orientationName(oldOrientation)} to: ${orientationName(newOrientation)}")
                orientationChanged()
            }

            override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
                Log.d(TAG, "Rotated from: ${oldRotation} to ${newRotation}")
                orientationChanged()
            }
        }
    }

    private fun orientationChanged() {
        hasOrientationChanged = true
    }

    private var hasOrientationChanged : Boolean = false
    //endregion

    //region STATICS
    companion object {
        const val TAG = "WizardActivity"
    }
    //endregion
}