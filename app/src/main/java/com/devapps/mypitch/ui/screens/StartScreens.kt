package com.devapps.mypitch.ui.screens

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.ui.Signup
import com.devapps.mypitch.ui.Start
import com.devapps.mypitch.ui.theme.teal
import com.devapps.mypitch.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignupScreen(myPitchNavController: NavController) {

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
                        .height(270.dp)
                )
                Text("MyPitch",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                    )
                Spacer( modifier = Modifier
                    .height(10.dp)
                )
                Text("Let MyPitch get you those business partner and investors",
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            ElevatedCard(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Text("Get started",
                        color = teal,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer( modifier = Modifier
                        .height(80.dp)
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
                        Text("Google")
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