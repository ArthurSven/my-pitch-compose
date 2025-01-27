package com.devapps.mypitch.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devapps.mypitch.ui.theme.feintGrey
import com.devapps.mypitch.ui.theme.teal

@Composable
fun CategoryRow() {
    val categoryList = listOf<String>("All", "Agriculture", "Art & Design", "Business", "Education",
        "Entertainment", "Fashion", "Food & Beverage", "Manufacturing", "Real estate", "Retail",
        "Tourism", "Misc")
    var selectedCategory by remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(categoryList) { index, category ->
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
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewUiUtilities() {

}