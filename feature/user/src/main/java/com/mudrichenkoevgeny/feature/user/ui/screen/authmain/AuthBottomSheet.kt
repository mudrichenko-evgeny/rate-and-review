package com.mudrichenkoevgeny.feature.user.ui.screen.authmain

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mudrichenkoevgeny.feature.user.ui.navigation.AuthNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val navController = rememberNavController()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        AuthNavGraph(navController = navController, onDismissRequest = onDismiss)
    }
}