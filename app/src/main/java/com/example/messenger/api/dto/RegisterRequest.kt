package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class RegisterRequest {
    var login = ""
    var password = ""
    @SerializedName("username") var userName = ""
}