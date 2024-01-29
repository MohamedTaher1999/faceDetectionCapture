package com.krishnaZyala.faceRecognition.ui.screen.home

import android.app.Activity
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.Keep
import androidx.navigation.NavHostController
import com.krishnaZyala.faceRecognition.R
import com.krishnaZyala.faceRecognition.ui.screen.permission.Permission
import com.krishnaZyala.faceRecognition.ui.screen.permission.PermissionProvider

@Keep
data class HomeScreenState(
    val host: NavHostController? = null,
    val backPressDispatcher: OnBackPressedDispatcher? = null,
    val permissions: MutableMap<PermissionProvider, Boolean> = defaultPermissions,
) {
    fun firstPermission(activity: Activity): PermissionProvider? = permissions.keys.firstOrNull { !it.hasPermission(activity) }

    companion object {
        val defaultPermissions: MutableMap<PermissionProvider, Boolean> = mutableMapOf(Permission.Camera to false)
    }
}
