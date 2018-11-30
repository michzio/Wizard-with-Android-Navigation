package com.click5interactive.wizardnavigation

import android.os.Bundle
import android.widget.ActionMenuView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.findNavController

class MyWizardModel : WizardModel() {
    override val steps = arrayOf(Step1Fragment.MODEL, Step2Fragment.MODEL, Step3Fragment.MODEL, Step4Fragment.MODEL, Step5Fragment.MODEL, Step6Fragment.MODEL, Step7Fragment.MODEL)
    override val confirm = ConfirmFragment.MODEL
    override val wizardGraph = R.navigation.wizard_nav_graph
}


class MyWizardActivity : WizardActivity() {

    //region MODEL
    override var model: WizardModel = MyWizardModel()
    //endregion

    //region LIFE CYCLE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        configureInitialStep()
    }
    //endregion

    private fun configureInitialStep() {

        val initialStep = intent.extras.getInt("INITIAL_STEP")
        navigateTo(initialStep)
    }

    //region STATICS
    companion object {
        const val TAG = "MyWizardActivity.TAG"
    }
    //endregion

}