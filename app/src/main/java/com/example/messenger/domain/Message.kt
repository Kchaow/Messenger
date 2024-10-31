package com.example.messenger.domain

import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("SenderId") var senderId: String = ""
    @SerializedName("SendingTime") var sendingTime: String = ""
    @SerializedName("Text") var text: String = ""
    @SerializedName("UserName") var userName: String = ""
}