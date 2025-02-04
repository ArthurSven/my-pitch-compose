package com.devapps.mypitch.ui.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route : String,
    val badgeCount: Int? = null
)

val categoryList = listOf<String>("All", "Agriculture", "Art & Design", "Business", "Education",
    "Entertainment", "Fashion", "Food & Beverage", "Manufacturing", "Real estate", "Retail",
    "Tourism", "Misc"
)

val formCategoryList = mutableListOf<String>("Agriculture", "Art & Design", "Business", "Education",
    "Entertainment", "Fashion", "Food & Beverage", "Manufacturing", "Real estate", "Retail",
    "Tourism", "Misc"
)

val messageArray = listOf("All messages", "Unread", "read")