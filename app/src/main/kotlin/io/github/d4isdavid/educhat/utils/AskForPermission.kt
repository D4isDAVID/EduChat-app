package io.github.d4isdavid.educhat.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

fun askForPermission(
    activity: ComponentActivity,
    permission: String,
    onGranted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
): ActivityResultLauncher<String> {
    val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onGranted?.invoke()
        } else {
            onDenied?.invoke()
        }
    }

    when {
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED ->
            onGranted?.invoke()

        else -> requestPermissionLauncher.launch(permission)
    }

    return requestPermissionLauncher
}
