package com.click5interactive.wizardnavigation

import android.app.Activity
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.app_bar_main.view.*


open class RTLActionBarDrawerToggle(activity: Activity, drawerLayout: DrawerLayout, toolbar: Toolbar,
                               openDrawerContentDescRes: Int, closeDrawerContentDescRes: Int) : DrawerLayout.DrawerListener {

    private val drawerLayout: DrawerLayout
    private var arrowDrawable: DrawerArrowDrawable
    private var toggleButton: AppCompatImageButton
    private val openDrawerContentDesc: String
    private val closeDrawerContentDesc: String
    private val toolbar : Toolbar

    init {
        this.toolbar = toolbar
        this.drawerLayout = drawerLayout
        this.openDrawerContentDesc = activity.getString(openDrawerContentDescRes)
        this.closeDrawerContentDesc = activity.getString(closeDrawerContentDescRes)

        arrowDrawable = DrawerArrowDrawable(toolbar.context)
        arrowDrawable.direction = DrawerArrowDrawable.ARROW_DIRECTION_END

        this.toggleButton = AppCompatImageButton(toolbar.context, null,
                R.attr.toolbarNavigationButtonStyle)
        toggleButton.setImageDrawable(arrowDrawable)
        toggleButton.setOnClickListener {
            toggle()
        }
        toggleButton.id = View.generateViewId()

        configureToogleButtonPositionIn(toolbar, toggleButton)
    }

    open fun configureToogleButtonPositionIn(toolbar: Toolbar, toggleButton: AppCompatImageButton) {

        toolbar.addView(toggleButton, Toolbar.LayoutParams(GravityCompat.END))
    }

    fun syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            setPosition(1f)
        } else {
            setPosition(0f)
        }
    }

    fun toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    fun setPosition(position: Float) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true)
            toggleButton.contentDescription = closeDrawerContentDesc
        } else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false)
            toggleButton.contentDescription = openDrawerContentDesc
        }
        arrowDrawable.progress = position
    }


    override fun onDrawerStateChanged(newState: Int) {

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        setPosition(Math.min(1f, Math.max(0f, slideOffset)));
    }

    override fun onDrawerClosed(drawerView: View) {
        setPosition(0f);
    }

    override fun onDrawerOpened(drawerView: View) {
        setPosition(1f);
    }
}