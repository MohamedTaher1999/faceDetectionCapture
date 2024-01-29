package com.krishnaZyala.faceRecognition.ui.screen.home

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.krishnaZyala.faceRecognition.data.model.AppState
import com.krishnaZyala.faceRecognition.ui.screen.selfie.AddFaceScreen

@Composable
fun HomeScreen(activity: Activity, vm: HomeViewModel = hiltViewModel()) {

    val home: NavHostController = rememberNavController()
    val state: HomeScreenState by remember(vm.state) { vm.state }

    DisposableEffect( home) {
        vm.onCompose( home)
        onDispose { vm.onDispose() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = contentColorFor(MaterialTheme.colorScheme.background),
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                AddFaceScreen()
            }

        }
    )
    state.firstPermission(activity)?.run {
        state.permissions[this]?.let { isDenied -> invoke(isDenied, onDeny = { vm.onPermissionDeny(this, it) }, onClick = { vm.onPermissionDeny(this, false) }) }
    }

}
