package com.devapps.mypitch.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devapps.mypitch.data.auth.GoogleAuthClient
import com.devapps.mypitch.data.model.Pitch
import com.devapps.mypitch.data.model.PitchResponse
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.ui.EditPitch
import com.devapps.mypitch.ui.ReadPitch
import com.devapps.mypitch.ui.theme.feintGrey
import com.devapps.mypitch.ui.theme.teal
import com.devapps.mypitch.ui.theme.textGrey
import com.devapps.mypitch.ui.viewmodels.PitchViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun CategoryRow(itemList: List<String>) {

    var selectedCategory by remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(itemList) { index, category ->
            CategoryUiItem(
                category = category,
                isSelected = index == selectedCategory,
                onClick = { selectedCategory = index}
            )
        }
    }
}

@Composable
fun CategoryUiItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = if (isSelected) CardDefaults.cardColors(
            containerColor = teal,
            contentColor = Color.White

        ) else CardDefaults.cardColors(
            containerColor = feintGrey,
            contentColor = Color.DarkGray
        )
    ) {
        Text(
            text = category,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun MyMessageInboxItem() {

    val user = UserData("","artska msiska","arthursvenmsiska@gmail.com",
        "hvbsdhvs"
    )

    ElevatedCard(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            Card(onClick = { },
                modifier = Modifier
                    .size(60.dp)
                    .clip(shape = RoundedCornerShape(360.dp))) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                        Text(text = "${user.username?.get(0)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color.Black)
                }
            }
            Spacer(modifier = Modifier
                .width(20.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "${user.username}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier
                    .height(5.dp)
                )
                Text(text = "Project yanu yandisangalasa gergreegegegergegergreg",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth())
            }
        }
    }
}

@Composable
fun MyMessageInboxList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(10) {
            MyMessageInboxItem()
        }
    }
}

@Composable
fun StretchableOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Enter text...",
    maxLines: Int = Int.MAX_VALUE, // Allow multiple lines
    minLines: Int = 1 // Minimum lines to show
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minLines.dp * 24, max = maxLines.dp * 24), // Adjust height dynamically
        placeholder = { Text(text = placeholder) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = teal,
            focusedLabelColor = teal,
            focusedTextColor = Color.Black,
            cursorColor = Color.Black,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { /* Handle done action */ }
        ),
        label = {
            Text("Describe your pitch")
        },
        maxLines = maxLines,
        singleLine = false // Allow multiple lines
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    categoryList: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // State for dropdown expansion

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        // TextField to display the selected category
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { }, // No need to handle input manually
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true, // Make the field non-editable
            placeholder = { Text(text = "Category") },
            label = { Text(text = "Category") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF008080), // Teal color
                focusedLabelColor = Color(0xFF008080), // Teal color
                focusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        // Dropdown menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryList.forEach { category ->
                // Dropdown menu item
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        onCategorySelected(category) // Update selected category
                        expanded = false // Collapse the dropdown
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PitchItem(
    pitch: PitchResponse,
    myPitchHomeNavController: NavController
) {
    OutlinedCard(
        onClick = {
            myPitchHomeNavController.navigate(ReadPitch.route + "/${pitch.pitchid}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(195.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(3.dp, teal),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(pitch.category,
                color = textGrey,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier
                .height(7.dp)
            )
            Text(pitch.pitchname,
                color = teal,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier
                .height(5.dp)
            )
            Text(pitch.description,
                color = textGrey,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            Text("By " + pitch.username,
                color = textGrey,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun PitchList(
    pitchViewModel: PitchViewModel,
    myPitchHomeNavController: NavController) {

    val pitches by pitchViewModel.pitches.collectAsState()
    val isLoading by pitchViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        pitchViewModel.getPitches() // Fetch pitches when screen loads
    }

    if (isLoading) {
        // Show Loading Placeholder
        repeat(5) {
            LoadingPitchItem()
            Spacer(modifier = Modifier.height(10.dp))
        }
    } else {
        LazyColumn {
            items(pitches) { pitch ->
                PitchItem(pitch, myPitchHomeNavController)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun LoadingPitchItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray.copy(alpha = 0.5f))
    )
}

@Composable
fun MyPitchItem(
    pitch: PitchResponse,
    myPitchHomeNavController: NavController,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {

    val showOptions = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    OutlinedCard(
        onClick = {
         //   myPitchHomeNavController.navigate(ReadPitch.route + "/${pitch.pitchid}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(3.dp, teal),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(pitch.category,
                color = textGrey,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier
                .height(7.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(pitch.pitchname,
                    color = teal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .weight(1f), // Pushes the icon to the end
                    textAlign = TextAlign.Start
                )

                IconButton(
                    onClick = { showOptions.value = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = teal
                    )
                }
                DropdownMenu(
                    expanded = showOptions.value,
                    onDismissRequest = {
                        showOptions.value = false
                    },
                    modifier = Modifier
                        .background(color = Color.White)
                        .width(80.dp)) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Edit",
                                color = Color.Black)
                        },
                        onClick = {
                            onEdit()
                            showOptions.value = false
                        },
                        modifier = Modifier
                            .background(color = Color.White)
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Delete",
                                color = Color.Black)
                        },
                        onClick = {
                            onDelete()
                            showOptions.value = false
                        },
                        modifier = Modifier
                            .background(color = Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier
                .height(10.dp)
            )
            Text(pitch.description,
                color = textGrey,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Justify,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier
                .height(8.dp)
            )
            Text(
                "By: " + pitch.username,
                color = textGrey,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun MyPitchList(
    pitchViewModel: PitchViewModel,
    myPitchHomeNavController: NavController) {

    var pitchToDelete by remember { mutableStateOf<PitchResponse?>(null) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    val pitches by pitchViewModel.pitches.collectAsState()
    val isLoading by pitchViewModel.isLoading.collectAsState()

    val context = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()

    val googleClientAuth by lazy {
        GoogleAuthClient(
            context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val userId = googleClientAuth.getSignedInUser()?.userId
    LaunchedEffect(userId) {
        if (userId != null) {
            pitchViewModel.setCreatedBy(userId)
            pitchViewModel.getPitchesByUserId(userId)
        }
    }

    if (isLoading) {
        // Show Loading Placeholder
        repeat(5) {
            LoadingPitchItem()
            Spacer(modifier = Modifier.height(10.dp))
        }
    } else {
        LazyColumn {
            items(pitches) { pitch ->
                MyPitchItem(
                    pitch,
                    myPitchHomeNavController,
                    onEdit = {
                        myPitchHomeNavController.navigate(EditPitch.route + "/${pitch.pitchid}")
                    },
                    onDelete = {
                        pitchToDelete = pitch
                        showDeleteDialog.value = true
                    }

                )
                if (showDeleteDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog.value = false },
                        title = { Text("Delete Pitch",
                            color = Color.Black
                        ) },
                        containerColor = Color.White,
                        text = { Text("Are you sure you want to delete this Pitch idea?",
                            color = Color.Black
                        ) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    pitchToDelete?.let { flashcard ->
                                        coroutineScope.launch {
                                            //flashcardViewModel.deleteFlashcard(flashcard)
                                            pitchToDelete = null
                                        }
                                    }
                                    showDeleteDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Delete",
                                    color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDeleteDialog.value = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = teal
                                )) {
                                Text("No",
                                    color = teal
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewUiUtilities() {
//MyPitchItem()
}