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

object Signup : ScreenDestination {
    override val route = "sign_in_screen"
}

object Signout : ScreenDestination {
    override val route = "sign_out"
}

object MyHome : ScreenDestination {
    override val route = "home_screen"
}

object MyPitches : ScreenDestination {
    override val route = "my_pitches_screen"
}

object CreatePitch : ScreenDestination {
    override val route = "create_pitch_screen"
}

object Messages : ScreenDestination {
    override val route = "messages_screen"
}

object Profile : ScreenDestination {
    override val route = "profile_screen"
}

