package com.devapps.mypitch.ui.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.window.SplashScreen
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devapps.mypitch.R
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.ui.Check
import com.devapps.mypitch.ui.Signup
import com.devapps.mypitch.ui.Start
import com.devapps.mypitch.ui.theme.teal
import com.devapps.mypitch.ui.theme.textGrey
import com.devapps.mypitch.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SplashScreens(myPitchNavController: NavController) {

    LaunchedEffect(Unit) {
        delay(2000)
        myPitchNavController.navigate(Check.route)
    }

        Column(
        modifier = Modifier
            .fillMaxSize()
            .background(teal),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Image(
                painterResource(R.drawable.pitch_refined),
                contentDescription = null
            )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignupScreen(myPitchNavController: NavController) {

    val context = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()

    // Retrieve AuthViewModel with the context parameter
    val authViewModel: AuthViewModel = koinViewModel(parameters = { parametersOf(context) })

    val state by authViewModel.state.collectAsStateWithLifecycle()

    val googleAuthClient by lazy {
        GoogleAuthClient(
            context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val termsUrl = "https://doc-hosting.flycricket.io/mypitch-terms-of-use/f390905b-448d-4e12-95b7-31aa1fcc58f0/terms"
    val privacyPolicyUrl = "https://doc-hosting.flycricket.io/mypitch-privacy-policy/d16694df-95f6-4689-a2e5-ecd0c332ea9c/privacy"

    BackHandler {
        myPitchNavController.popBackStack(Signup.route, false)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    authViewModel.onSignInResult(signInResult)
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(teal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(10.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(220.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(
                            R.drawable.pitch_refined),
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp)
                    )
                }
                Spacer( modifier = Modifier
                    .height(10.dp)
                )
            }
            ElevatedCard(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Spacer( modifier = Modifier
                        .height(60.dp)
                    )
                    Text("Let MyPitch get you those business partner and investors",
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = textGrey,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer( modifier = Modifier
                        .height(30.dp)
                    )
                    ElevatedButton(
                        onClick = {
                            coroutineScope.launch {
                                val signInIntentSender = googleAuthClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = teal,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Sign in")
                    }
                    Spacer( modifier = Modifier
                        .height(50.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "T's and C's",
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = teal,
                            modifier = Modifier
                                .clickable {
                                    // Open the Ts & Cs URL in a web view
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                }
                                .padding(end = 8.dp)
                        )

                        // Divider
                        Text(
                            text = "|",
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = teal,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // Privacy Policy Link
                        Text(
                            text = "Privacy Policy",
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = teal,
                            modifier = Modifier
                                .clickable {
                                    // Open the privacy policy URL in a web view
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(intent)
                                }
                                .padding(start = 8.dp)
                        )
                    }
                    LaunchedEffect(key1 = state.isSignInSuccessful) {
                        if (state.isSignInSuccessful) {
                            Toast.makeText(
                                context,
                                "Sign in successful",
                                Toast.LENGTH_LONG
                            ).show()
                            myPitchNavController.navigate(Start.route)
                            authViewModel.resetState()
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ScreenPreview() {

}