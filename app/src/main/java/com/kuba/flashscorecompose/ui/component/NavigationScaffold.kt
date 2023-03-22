package com.kuba.flashscorecompose.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Route
import com.ramcosta.composedestinations.utils.startDestination

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun NavigationScaffold(
    startRoute: Route,
    snackbarHost: @Composable () -> Unit,
    navController: NavHostController,
    bottomBar: @Composable (DestinationSpec<*>) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val startDestination = startRoute.startDestination
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    navController.navigatorProvider += bottomSheetNavigator
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(16.dp)
    ) {
        Scaffold(
            snackbarHost = snackbarHost,
            bottomBar = { bottomBar(startDestination) },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            content = content
        )
    }
}
