package com.example.model.fixture

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goals(val home: Int, val away: Int) : Parcelable {
    companion object {
        val EMPTY_GOALS = Goals(0, 0)
    }
}
