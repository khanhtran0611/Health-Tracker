package com.example.healthtracker.ui.mainshell

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.healthtracker.ui.navigation.Route

/**
 * Điều hướng khi bấm 1 tab bottom nav. Nhận vào backstack CỦA TAB đang active
 * (mỗi tab giữ backstack riêng — xem [rememberTabBackStacks]), KHÔNG phải
 * backstack tầng ngoài ở `HealthTrackerApp.kt`.
 * - Nếu route đã có trong backstack của tab đó → quay về đúng nó (bỏ các màn
 *   nằm phía trên), không thêm bản trùng. Với tab hiện tại chỉ có đúng 1 route
 *   gốc (không có màn con đẩy thêm vào — mọi màn con đều bubble ra backstack
 *   tầng ngoài qua `onNavigateOuter`), nên thực chất đây là reset về gốc.
 * - Nếu chưa có → thêm vào.
 *
 * (rememberNavBackStack luôn trả NavBackStack<NavKey> nên tham số để kiểu này.)
 */
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
