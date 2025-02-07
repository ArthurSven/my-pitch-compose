package com.devapps.mypitch.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.devapps.mypitch.R
import com.devapps.mypitch.ui.MyHome
import com.devapps.mypitch.ui.theme.teal
import com.devapps.mypitch.ui.utils.state.GetPitchByIdUiState
import com.devapps.mypitch.ui.viewmodels.PitchViewModel
import kotlinx.coroutines.launch

@Composable
fun ReadPitchScreen(
    pitchViewModel: PitchViewModel,
    pitchid: String,
    myPitchHomeNavController: NavController
) {

    // Observe the pitch state
    val pitchState by pitchViewModel.pitch.collectAsState()
    val context = LocalContext.current
    // Fetch the pitch details when the screen is launched or when pitchid changes
    LaunchedEffect(pitchid) {
        pitchViewModel.getPitchById(pitchid)
    }


    when (pitchState) {
        is GetPitchByIdUiState.Idle -> {
            // Show nothing or a placeholder
        }
        is GetPitchByIdUiState.Loading -> {
            // Show a loading indicator
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = teal)
            }
        }
        is GetPitchByIdUiState.Success -> {
            val pitch = (pitchState as GetPitchByIdUiState.Success).pitch
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Image(
                        painter = painterResource(R.drawable.business), // Replace with your image resource
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) // Adjust alpha for more/less darkness
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(110.dp))
                        Text(
                            text = pitch.category,
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(teal, shape = RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pitch.pitchname,
                            color = Color.White,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                Card(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .offset(y = (-30).dp), // Adjust this value to control the overlap
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp) // Rounded corners only at the top
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = pitch.description,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "By: " + pitch.username,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = {
                                // Handle email pitcher button click
                                val subject = Uri.encode(pitch.pitchname) // Encode the subject
                                val body = Uri.encode("Dear ${pitch.username},\n\n") // Encode the body

                                val uri = Uri.parse("mailto:${pitch.email}?subject=$subject&body=$body")

                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = uri
                                }

                                try {

                                    ContextCompat.startActivity(context, Intent.createChooser(intent, "Choose an email client"), null)
                                } catch (e: Exception) {
                                    Log.e("Msg", e.message.toString())
                                    Toast.makeText(context, "No email app found",
                                        Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = teal,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                        ) {
                            Text(
                                "Email Pitcher",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
        is GetPitchByIdUiState.Error -> {
            // Navigate back to the home screen on error
            LaunchedEffect(Unit) {
                myPitchHomeNavController.navigate(MyHome.route)
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun ViewSubScreens() {

}