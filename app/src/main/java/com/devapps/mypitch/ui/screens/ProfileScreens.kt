package com.devapps.mypitch.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.bitmapConfig
import com.devapps.mypitch.R
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.ui.Messages
import com.devapps.mypitch.ui.MyHome
import com.devapps.mypitch.ui.MyPitches
import com.devapps.mypitch.ui.Profile
import com.devapps.mypitch.ui.Signout
import com.devapps.mypitch.ui.theme.feintGrey
import com.devapps.mypitch.ui.theme.teal
import com.devapps.mypitch.ui.utils.BottomNavItem
import com.devapps.mypitch.ui.utils.CategoryRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPitchScreens(
    myPitchNavController: NavController,
    userData: UserData?,
    onSignOut: () -> Unit
) {

    val myPitchHomeNavController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val showMenu = remember { mutableStateOf(false) }
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val items = listOf(
        BottomNavItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = MyHome.route
        ),
        BottomNavItem(
            title = "My pitch",
            selectedIcon = Icons.Filled.Lightbulb,
            unselectedIcon = Icons.Outlined.Lightbulb,
            route = MyPitches.route
        ),
        BottomNavItem(
            title = "Messages",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email,
            badgeCount = 0,
            route = Messages.route
        ),
        BottomNavItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = Profile.route
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
             title = {
                 null
             },
               navigationIcon = {
                   IconButton(onClick = {

                   }) {
                       Icon(Icons.Default.Menu, contentDescription = null, tint = teal)
                   }
               },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    if(userData?.userProfileUrl != null) {
                        val req = ImageRequest.Builder(context)
                            .data(userData.userProfileUrl)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .allowHardware(false)
                            .build()
                        AsyncImage(
                            model = req,
                            contentDescription = "${userData.username}'s profile picture",
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .clickable {
                                    showMenu.value = !showMenu.value
                                },
                            contentScale = ContentScale.Crop
                        )
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = {
                                showMenu.value = false
                            },
                            modifier = Modifier
                                .background(color = Color.White)
                                .width(150.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Logout,
                                    contentDescription = "logout",
                                    tint = Color.DarkGray
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(5.dp)
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Logout",
                                            color = Color.Black)
                                    },
                                    onClick = {
                                        myPitchNavController.navigate(Signout.route)
                                        onSignOut()
                                    },
                                    modifier = Modifier
                                        .background(color = Color.White)
                                )
                            }
                        }
                    } else {
                        Image(
                            painter = painterResource(R.drawable.no_profile),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .clickable {
                                    showMenu.value = !showMenu.value
                                },
                            contentScale = ContentScale.Crop
                        )
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = {
                                showMenu.value = false
                            },
                            modifier = Modifier
                                .background(color = Color.White)
                                .width(80.dp)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                            ) {
                                Icon(imageVector = Icons.Outlined.Logout, contentDescription = "logout")
                                Spacer(
                                    modifier = Modifier
                                        .width(8.dp)
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Logout",
                                            color = Color.Black)
                                    },
                                    onClick = {
                                        myPitchNavController.navigate(Signout.route)
                                        onSignOut()
                                    },
                                    modifier = Modifier
                                        .background(color = Color.White)
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = feintGrey
            ) {
                items.fastForEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            myPitchHomeNavController.navigate(item.route) {
                                popUpTo(myPitchHomeNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        label = {
                            Text(text = item.title,
                                color = teal)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if(item.badgeCount!= null) {
                                        Text(text = item.badgeCount.toString())
                                    }
                            }) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = teal
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
      NavHost(myPitchHomeNavController, startDestination = MyHome.route, modifier = Modifier.padding(innerPadding)) {
            composable(MyHome.route) {
                MyHomeScreen()
            }
            composable(MyPitches.route) {
              MyPitchListScreen()
            }
      }
    }
}

@Composable
fun MyHomeScreen() {

    var search by rememberSaveable {
        mutableStateOf("")
    }
    Surface(
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier
                    .height(20.dp)
                )
                Text("Discover",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text("Potential projects and business partners",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier
                    .height(20.dp)
                )
                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search == it
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search, contentDescription = "search", tint = Color.DarkGray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = feintGrey,
                        unfocusedContainerColor = feintGrey,
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    placeholder = {
                        Text("Search",
                            fontSize = 14.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
                Spacer(modifier = Modifier
                    .height(2.dp)
                )
            }
            CategoryRow()
        }

    }

}

@Composable
fun MyPitchListScreen() {

    var search by rememberSaveable {
        mutableStateOf("")
    }
    Surface(
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier
                    .height(20.dp)
                )
                Text("My Pitches",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text("Your recently posted pitches",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier
                    .height(20.dp)
                )
                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search == it
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search, contentDescription = "search", tint = Color.DarkGray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = feintGrey,
                        unfocusedContainerColor = feintGrey,
                        unfocusedTextColor = Color.Gray,
                        focusedTextColor = Color.DarkGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    placeholder = {
                        Text("Search",
                            fontSize = 14.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
                Spacer(modifier = Modifier
                    .height(2.dp)
                )
            }
            CategoryRow()
        }

    }

}
@Composable
@Preview(showBackground = true)
fun ViewProfileScreens() {

}