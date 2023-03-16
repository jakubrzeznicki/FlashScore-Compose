package com.example.network.model.fixture

import com.google.gson.annotations.SerializedName

data class FixtureDataDto(@SerializedName("response") val response: List<FixtureDto>?)
