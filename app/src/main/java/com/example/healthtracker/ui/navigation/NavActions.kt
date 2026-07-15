package com.example.healthtracker.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Điều hướng khi bấm 1 tab bottom nav.
 * - Nếu tab đã có trong backstack → quay về đúng nó (bỏ các màn nằm phía trên),
 *   không thêm bản trùng.
 * - Nếu chưa có → thêm vào.
 *
 * (rememberNavBackStack luôn trả NavBackStack<NavKey> nên tham số để kiểu này.)
 */
fun navigateToTab(backStack: NavBackStack<NavKey>, route: Route) {
    val existingIndex = backStack.indexOf(route)
    if (existingIndex != -1) {
        while (backStack.size > existingIndex + 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    } else {
        backStack.add(route)
    }
}
