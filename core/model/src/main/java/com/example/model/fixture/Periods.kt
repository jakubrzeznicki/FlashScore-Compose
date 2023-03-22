package com.example.model.fixture

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Periods(val first: Int, val second: Int) : Parcelable {
    companion object {
        val EMPTY_PERIODS = Periods(0, 0)
    }
}
