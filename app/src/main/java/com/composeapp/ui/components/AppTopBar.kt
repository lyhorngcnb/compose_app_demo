package com.composeapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    SmallTopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) }
    )
}
