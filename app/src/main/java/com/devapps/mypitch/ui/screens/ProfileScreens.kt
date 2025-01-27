package com.devapps.mypitch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.ui.Messages
import com.devapps.mypitch.ui.MyHome
import com.devapps.mypitch.ui.MyPitches
import com.devapps.mypitch.ui.Profile
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
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
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
                    .height(25.dp)
                )
                Text("Discover",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
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
                        Text("Search")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
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
                    .height(25.dp)
                )
                Text("My Pitches",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
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
                        Text("Search")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
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