package com.example.model.player

/**
 * Created by jrzeznicki on 15/01/2023.
 */
data class Birth(val date: String, val place: String, val country: String) {
    companion object {
        val EMPTY_BIRTH = Birth("", "", "")
    }
}
