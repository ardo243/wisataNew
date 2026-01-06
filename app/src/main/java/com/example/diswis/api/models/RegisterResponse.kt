package com.example.diswis.api.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("message")
    val message: String
)
