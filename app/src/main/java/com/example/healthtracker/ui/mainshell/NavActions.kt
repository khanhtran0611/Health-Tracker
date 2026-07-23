package com.example.healthtracker.ui.mainshell

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.healthtracker.ui.navigation.Route

fun navigateToTab(activeTabBackStack: NavBackStack<NavKey>, route: Route) {
    val existingIndex = activeTabBackStack.indexOf(route)
    if (existingIndex != -1) {
        while (activeTabBackStack.size > existingIndex + 1) {
            activeTabBackStack.removeAt(activeTabBackStack.lastIndex)
        }
    } else {
        activeTabBackStack.add(route)
    }
}
