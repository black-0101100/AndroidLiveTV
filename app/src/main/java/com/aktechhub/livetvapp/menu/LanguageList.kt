package com.aktechhub.livetvapp.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aktechhub.livetvapp.model.Language


@Composable
fun LanguageList(languages : List<Language>, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.DarkGray)
            .border(2.dp, Color.LightGray) // Border for whole Category List
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(languages) { language ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color.LightGray)
                        .clickable { onSelect(language.languageId)}
                        .padding(8.dp)
                ) {
                    Text(
                        text = language.languageName,
                        color = Color.White
                    )
                }
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewLanguageList() {
   // LanguageList(onSelect = {})
//}
