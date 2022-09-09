package com.kuba.flashscorecompose.data.country.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 9/7/2022
 */
class Country {

    @SerializedName("country_id")
    @Expose
    var id: String? = null

    @SerializedName("country_logo")
    @Expose
    var logo: String? = null

    @SerializedName("country_name")
    @Expose
    var name: String? = null
}