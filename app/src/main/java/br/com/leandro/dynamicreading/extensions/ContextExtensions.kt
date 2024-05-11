package br.com.leandro.dynamicreading.extensions

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

/**
 * Extension function to display a toast message.
 *
 * @param text The message to be displayed.
 * @param duration The duration for which the toast message is displayed. The default value
 * is Toast.LENGTH_LONG.
 * @return The Toast object.
 */
fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(this, text, duration).apply { show() }

/**
 * Extension function to vibrate the device.
 *
 * @param milliseconds The duration of the vibration in milliseconds.
 */
fun Context.vibrate(milliseconds: Long) {
    val vibrator = getSystemService(Vibrator::class.java)

    vibrator?.vibrate(
        VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
    )
}

/**
 * Extension function to check if the device is currently in dark mode.
 *
 * @return True if the device is in dark mode, false otherwise.
 */
fun Context.isDarkTheme(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val uiModeManager = this.getSystemService(UiModeManager::class.java)
        uiModeManager != null && uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    } else {
        val nightMode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightMode == Configuration.UI_MODE_NIGHT_YES
    }
}