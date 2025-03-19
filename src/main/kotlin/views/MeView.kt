package com.imarkoff.views

import com.imarkoff.services.AuthService
import io.ktor.client.HttpClient

/**
 * View to show the current user's information.
 */
class MeView(private val client: HttpClient): IView {
    override suspend fun call() {
        val authService = AuthService(client)
        val me = authService.getMe()
        val client = me.entity

        println("Hello, ${client.account.firstName} ${client.account.lastName}!")
        println("You are logged in as ${client.account.role}.")

        client.pronoun ?.let { println("Gender: $it") }

        client.trainingPlan ?.let { plan ->
            println("You are currently on the ${plan.name} training plan.")
            println("Description: ${plan.description}")
            println("Complexity: ${plan.complexity}")
            println("Assigned at: ${client.dateAssigned}")
        }

        client.activeWorkoutSession ?.let {
            println("You are currently in a workout session. Good luck!")
        }
    }
}