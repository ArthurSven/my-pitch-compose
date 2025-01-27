package com.devapps.mypitch.ui.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route : String,
    val badgeCount: Int? = null
)
