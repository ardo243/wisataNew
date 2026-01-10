package com.example.diswis.api.models

import com.google.gson.annotations.SerializedName

data class DestinasiResponse(

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("data")
    val data: List<Destinasi>
)
