package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("UserId") var userId = 0
    @SerializedName("Token") var token : TokenResponse = TokenResponse()
}