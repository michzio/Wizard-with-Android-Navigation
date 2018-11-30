package com.click5interactive.wizardnavigation

data class ConfirmModel(val destination: Int, val name: String? = null)
data class StepModel( val destination : Int, val action: Int, val name: String? = null)

abstract class WizardModel {
    abstract val steps: Array<StepModel>
    abstract val confirm : ConfirmModel
    abstract val wizardGraph : Int

    fun hasDestination(destination: Int) : Boolean {
        return steps.any { step -> step.destination == destination }
    }

    fun destinationIndex(destination: Int) : Int {
        return steps.indexOfFirst { step -> step.destination == destination }
    }

    fun stepFor(destination: Int) : StepModel? {
        val stepIdx = destinationIndex(destination)
        if(stepIdx >= 0 && stepIdx < steps.size) {
            return steps.get(stepIdx)
        }
        return null
    }

    fun stepByName(name: String): StepModel? {
        val stepModel = steps.find { stepModel ->
            stepModel.name == name
        }
        return stepModel
    }

    fun confirmByName(name: String): ConfirmModel? {
        if (confirm.name == name) {
            return confirm
        }
        return null
    }

    fun findStepIndexByName(name: String) : Int {
        var stepIndex = steps.indexOfFirst { stepModel ->
            stepModel.name == name
        }
        if(stepIndex < 0 && confirm.name == name) {
            stepIndex = steps.size
        }
        return stepIndex
    }
}