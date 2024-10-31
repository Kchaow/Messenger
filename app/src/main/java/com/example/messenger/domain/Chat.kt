package com.example.messenger.domain

import com.google.gson.annotations.SerializedName

class Chat {
    var id : String? = null
    @SerializedName("IsDirect") var isDirect = false
    @SerializedName("Name") var name : String? = null
    @SerializedName("OwnerId") var ownerId : String? = null
    @SerializedName("Owner") var owner: Owner? = null
}