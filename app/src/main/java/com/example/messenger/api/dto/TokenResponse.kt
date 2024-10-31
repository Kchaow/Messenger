package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class TokenResponse {
    @SerializedName("AccessToken") var accessToken = ""
    @SerializedName("RefreshToken") var refreshToken = ""
}