package com.hyperdesign.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookDetailsScreen(
    bookId: Int,
    onBack: () -> Unit,
) {

    Text(text = "Book id is $bookId")
}