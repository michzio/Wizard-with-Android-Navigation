package com.click5interactive.wizardnavigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_wizard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*

abstract class HamburgerMenuActivity : AppCompatActivity() {

    //region MODEL
    protected var hamburgerFragments = arrayOf(R.id.menu1Fragment, R.id.menu2Fragment, R.id.menu3Fragment)
    open var bottomNavFragments = emptyArray<Int>()

    protected var toggleButton : AppCompatImageButton? = null
    //endregion

    //region LISTENERS
    var toolbarListener : ToolbarListener? = null
    //endregion

    //region LIFE CYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //AndroidInjection.inject(this) // Dagger
    }
    override fun onResume() {
        super.onResume()
    }
    //endregion

    //region SETUP
    protected fun setupHamburgerMenuActivity() {
        setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)
        backButton.setOnClickListener(this::onBackButtonTouched)

        configureToolbarMenu()
        configureDrawerToggle()
        configureNavigationController()
    }
    //endregion

    //region NAVIGATION CONTROLLER
    open fun configureNavigationController() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnNavigatedListener {
            controller, destination ->

            //Toast.makeText(this, destination.label, Toast.LENGTH_LONG).show()
            //  if(bottomNavFragments.contains(destination.id)) {
            //      backButton.visibility = View.INVISIBLE
            //  } else
            if( hamburgerFragments.contains(destination.id)) {
                backButton.setImageResource(R.drawable.close)
                backButton.visibility = View.VISIBLE
            } else {
                backButton.setImageResource(R.drawable.back)
                backButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp() : Boolean {
        //return findNavController(R.id.main_nav_host_fragment).navigateUp()
        onBackPressed()
        return true
    }
    //endregion

    //region TOOLBAR, TOGGLE and DRAWER
    abstract protected fun configureToolbarMenu()

    protected fun configureDrawerToggle() {



        val toggle = object : RTLActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            override fun configureToogleButtonPositionIn(toolbar: Toolbar, toggleButton: AppCompatImageButton) {

                this@HamburgerMenuActivity.toggleButton = toggleButton


                toolbar.toolbar_layout.addView(toggleButton, 0)
                toggleButton.setColorFilter(ContextCompat.getColor(this@HamburgerMenuActivity, R.color.colorAccent))

                val constraintSet = ConstraintSet()
                constraintSet.clone(toolbar.toolbar_layout)
                constraintSet.connect(toolbar.toolbar_layout.toolbar_action_menu.id, ConstraintSet.END, toggleButton.id, ConstraintSet.START, 8)
                constraintSet.connect(toggleButton.id, ConstraintSet.TOP, toolbar.toolbar_layout.id, ConstraintSet.TOP)
                constraintSet.connect(toggleButton.id, ConstraintSet.BOTTOM, toolbar.toolbar_layout.id, ConstraintSet.BOTTOM)
                constraintSet.connect(toggleButton.id, ConstraintSet.END, toolbar.toolbar_layout.id, ConstraintSet.END, 0)
                //constraintSet.connect(toggleButton.id, ConstraintSet.CHAIN_PACKED, toolbar.toolbar_layout.toolbar_action_menu.id, ConstraintSet.CHAIN_PACKED, 8)
                constraintSet.applyTo(toolbar.toolbar_layout)
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    //region BACK BUTTON
    protected fun onBackButtonTouched(v: View?) {
        if(toolbarListener?.toolbarBackButtonTouched() == true) return
        onBackPressed()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
            drawer_layout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
    //endregion


    //region HAMBURGER MENU CONFIG
    fun disableHamburgerMenu() {
        toggleButton?.visibility = View.GONE
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    fun enableHamburgerMenu() {
        toggleButton?.visibility = View.VISIBLE
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    //endregion

    interface ToolbarListener {
        fun toolbarBackButtonTouched() : Boolean {
            // by default back button is not handled
            return false
        }
    }
}