package com.devapps.mypitch.ui

interface ScreenDestination {

    val route: String
}

object Check : ScreenDestination {
    override val route = "check_auth"
}

object Start : ScreenDestination {
    override val route = "start_screen"
}

object MyHome : ScreenDestination {
    override val route = "home_screen"
}

object MyPitches : ScreenDestination {
    override val route = "my_pitches_screen"
}

object Messages : ScreenDestination {
    override val route = "messages_screen"
}

object Profile : ScreenDestination {
    override val route = "profile_screen"
}

