package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Status(val elapsed: Int, val long: String, val short: String) {
    companion object {
        val EMPTY_STATUS = Status(0, "", "")
    }
}