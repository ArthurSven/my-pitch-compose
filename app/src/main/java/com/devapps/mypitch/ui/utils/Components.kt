package com.devapps.mypitch.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devapps.mypitch.data.model.UserData
import com.devapps.mypitch.ui.theme.feintGrey
import com.devapps.mypitch.ui.theme.teal

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
                    .size(80.dp)
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
                    .height(10.dp)
                )
                Text(text = "Project yanu yandisangalasa gergreegegegergegergreg",
                    color = Color.DarkGray,
                    modifier = Modifier
                        .height(16.dp))
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
@Preview(showBackground = true)
fun PreviewUiUtilities() {
    MyMessageInboxItem()
}