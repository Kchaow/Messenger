package com.example.messenger.domain

import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName(value = "SenderId", alternate = ["senderId"]) var senderId: String = ""
    @SerializedName(value = "SendingTime", alternate = ["sendingTime"]) var sendingTime: String = ""
    @SerializedName(value = "Text", alternate = ["text"]) var text: String = ""
    @SerializedName(value = "UserName", alternate = ["userName"]) var userName: String = ""
}