package com.aktechhub.livetvapp.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aktechhub.livetvapp.remote.LocalRepository

@Composable
fun CategoryList(onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.DarkGray)
            .border(2.dp, Color.LightGray) // Border for whole Category List
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(LocalRepository.categories) { category ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color.LightGray) // Border for each category
                        .clickable { onSelect(category.id) }
                        .padding(8.dp)
                ) {
                    Text(
                        text = category.name,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    CategoryList(onSelect = {})
}
