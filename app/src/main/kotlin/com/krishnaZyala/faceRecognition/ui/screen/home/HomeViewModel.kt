package com.krishnaZyala.faceRecognition.ui.screen.home

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.krishnaZyala.faceRecognition.data.Repository
import com.krishnaZyala.faceRecognition.data.model.AppState
import com.krishnaZyala.faceRecognition.lib.LOG
import com.krishnaZyala.faceRecognition.ui.screen.permission.PermissionProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repo: Repository) : ViewModel() {
    val state: MutableState<HomeScreenState> = mutableStateOf(HomeScreenState())
    fun onCompose( home: NavHostController) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            state.value = state.value.copy(host = home)
         //   state.value.host?.addOnDestinationChangedListener(navChangeListener)
            initPermissions()
            LOG.d("Home Screen Composed")
        }.onFailure { LOG.e(it, it.message) }
    }

    fun onDispose() = runCatching {
       // state.value.host?.removeOnDestinationChangedListener(navChangeListener)
        LOG.d("Home Screen Disposed")
    }.onFailure { LOG.e(it, it.message) }

    private fun initPermissions() = runCatching {
        val permissions = mutableMapOf<PermissionProvider, Boolean>()
        state.value.permissions.keys.forEach { permissions[it] = false }
        state.value = state.value.copy(permissions = permissions)
    }.onFailure { LOG.e(it, it.message) }

    fun onPermissionDeny(provider: PermissionProvider, isDenied: Boolean) = runCatching {
        val permissions = mutableMapOf<PermissionProvider, Boolean>()
        state.value.permissions.keys.forEach { if (it.name == provider.name) permissions[it] = isDenied else permissions[it] = false }
        state.value = state.value.copy(permissions = permissions)
    }.onFailure { LOG.e(it, it.message) }


}
