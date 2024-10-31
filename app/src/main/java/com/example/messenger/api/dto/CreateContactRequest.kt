package com.example.messenger.api.dto

import com.google.gson.annotations.SerializedName

class CreateContactRequest {
    @SerializedName("contact_login") var login = ""
}