 package com.devapps.mypitch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.identity.Identity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.ui.Check
import com.devapps.mypitch.ui.Signout
import com.devapps.mypitch.ui.Signup
import com.devapps.mypitch.ui.Splashscreen
import com.devapps.mypitch.ui.Start
import com.devapps.mypitch.ui.screens.MyPitchScreens
import com.devapps.mypitch.ui.screens.SignupScreen
import com.devapps.mypitch.ui.screens.SplashScreens
import com.devapps.mypitch.ui.theme.MyPitchTheme
import com.devapps.mypitch.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPitchTheme {
                MainMyPitchNavigation()
            }
        }
    }
}

 @Composable
 fun MainMyPitchNavigation() {
     val context = LocalContext.current.applicationContext
     val coroutineScope = rememberCoroutineScope()
     val authViewModel: AuthViewModel = koinViewModel()
     val state by authViewModel.state.collectAsStateWithLifecycle()

     val googleAuthClient by lazy {
         GoogleAuthClient(
             context,
             oneTapClient = Identity.getSignInClient(context)
         )
     }

     val myPitchNavController = rememberNavController()

     NavHost(myPitchNavController, startDestination = Splashscreen.route) {
         composable(Splashscreen.route) {
             SplashScreens(myPitchNavController)
         }
         composable(Check.route) {
             LaunchedEffect(key1 = Unit) {
                if(googleAuthClient.getSignedInUser() != null) {
                    if (state.isSignInSuccessful) {

                    }
                    myPitchNavController.navigate(Start.route)
                } else {
                    myPitchNavController.navigate(Signup.route)
                }
             }
         }
        composable(Signup.route) {
            SignupScreen(myPitchNavController)
        }
         composable(Start.route) {
             MyPitchScreens(
                 myPitchNavController,
                 googleAuthClient.getSignedInUser(),
                 onSignOut = {
                     coroutineScope.launch {
                         googleAuthClient.signOut()
                         Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                     }
                 }
             )
         }
         composable(Signout.route) {
             LaunchedEffect(Unit) {
                 googleAuthClient.signOut()
                 myPitchNavController.navigate(Signup.route)
             }
         }
     }
 }


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPitchTheme {

    }
}