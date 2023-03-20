package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Status(val elapsed: Int, val long: String, val short: String) : Parcelable {
    companion object {
        val EMPTY_STATUS = Status(0, "", "")
    }
}
