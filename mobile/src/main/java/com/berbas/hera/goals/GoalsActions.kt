package com.berbas.hera.goals

/**
 * Interface for Goal actions.
 * This interface acts as a controller for the GoalFragments.
 */
interface GoalsActions {

    /**
     * Returns a list of all fitness programs.
     */
    fun viewPrograms(program: Array<Program>) {

    }

    fun getProgramDetails(program: Program) {

    }

    /**
     * Starts a fitness program.
     */
    fun startProgram(program: Program) {

    }
}