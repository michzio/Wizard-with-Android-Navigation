package com.click5interactive.wizardnavigation

import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener
import android.view.Surface
import android.view.WindowManager

abstract class SimpleOrientationEventListener(context: Context) : OrientationEventListener(context, SensorManager.SENSOR_DELAY_UI) {

    var orientation : Int = ORIENTATION_UNKNOWN
    var rotation : Int

    private val windowManager: WindowManager
    private var isInitialized: Boolean = false

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        rotation = degreeRotation(windowManager.defaultDisplay.rotation)
    }

    override fun onOrientationChanged(rawOrientation: Int) {

        if(!isInitialized) {
            orientation = normalizeOrientation(rawOrientation)
            if(orientation != ORIENTATION_UNKNOWN) {
                isInitialized = true
            }
            return
        }

        val newOrientation = normalizeOrientation(rawOrientation)
        if(newOrientation != orientation && newOrientation != ORIENTATION_UNKNOWN) {
            onOrientationChanged(orientation, newOrientation)
            orientation = newOrientation
        }

        val newRotation = degreeRotation(windowManager.defaultDisplay.rotation)
        if(newRotation != rotation) {
            onRotationChanged(rotation, newRotation)
            rotation = newRotation
        }
    }

    abstract fun onOrientationChanged(oldOrientation: Int, newOrientation: Int)
    abstract fun onRotationChanged(oldRotation: Int, newRotation: Int)

    companion object {
        private val TAG = "SmplOrientEventListener"

        val ORIENTATION_PORTRAIT = 0
        val ORIENTATION_LANDSCAPE = 1
        val ORIENTATION_PORTRAIT_REVERSE = 2
        val ORIENTATION_LANDSCAPE_REVERSE = 3

        const val DEGREE_OFFSET = 16

        fun normalizeOrientation(orientation: Int) : Int {

            if(orientation < 0) {
                // device is flat (perhaps on a table) and orientation is unknown
                return ORIENTATION_UNKNOWN
            } else if(orientation >= 0 && orientation <= 0 + DEGREE_OFFSET) {
                return ORIENTATION_PORTRAIT
            } else if(orientation >= 90 - DEGREE_OFFSET  && orientation <= 90 + DEGREE_OFFSET) {
                return ORIENTATION_LANDSCAPE_REVERSE
            } else if(orientation >= 180 - DEGREE_OFFSET && orientation <= 180 + DEGREE_OFFSET) {
                return ORIENTATION_PORTRAIT_REVERSE
            } else if(orientation >= 270 - DEGREE_OFFSET && orientation <= 270 + DEGREE_OFFSET) {
                return ORIENTATION_LANDSCAPE
            } else if(orientation >= 360 - DEGREE_OFFSET && orientation <= 360) {
                return  ORIENTATION_PORTRAIT
            }

            //Log.d(TAG, "Unknown orientation: $orientation")
            return ORIENTATION_UNKNOWN
        }

        fun orientationName(normalizedOrientation: Int) : String {
            when(normalizedOrientation) {
                ORIENTATION_PORTRAIT -> return "Portrait"
                ORIENTATION_LANDSCAPE_REVERSE -> return "Landscape Reverse"
                ORIENTATION_LANDSCAPE -> return "Landscape"
                ORIENTATION_PORTRAIT_REVERSE -> return "Portrait Reverse"
                else -> return "Unknown"
            }
        }

        fun degreeRotation(screenRotation : Int) : Int {
            when(screenRotation) {
                Surface.ROTATION_0 -> return 0
                Surface.ROTATION_90 -> return 90
                Surface.ROTATION_180 -> return 180
                Surface.ROTATION_270 -> return 270
                else -> return -1
            }
        }
    }
}