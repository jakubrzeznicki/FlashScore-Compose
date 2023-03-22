package com.example.common.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by jrzeznicki on 09/02/2023.
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, this.resources.getText(resId), duration).show()
}
