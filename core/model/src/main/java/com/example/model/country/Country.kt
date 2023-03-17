package com.example.model.country

/**
 * Created by jrzeznicki on 9/7/2022
 */
data class Country(val code: String, val name: String, val flag: String) {
    companion object {
        val EMPTY_COUNTRY = Country("", "", "")
    }
}