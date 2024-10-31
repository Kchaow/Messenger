package com.example.messenger.domain

import com.google.gson.annotations.SerializedName

class Owner {
    var id: String = ""
    @SerializedName("Login") var login: String = ""
    @SerializedName("UserName") var userName: String = ""
}