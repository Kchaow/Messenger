package com.example.messenger.api

import com.example.messenger.api.dto.AddMembersRequest
import com.example.messenger.api.dto.CreateChatRequest
import com.example.messenger.api.dto.CreateContactRequest
import com.example.messenger.api.dto.LoginRequest
import com.example.messenger.api.dto.LoginResponse
import com.example.messenger.api.dto.RegisterRequest
import com.example.messenger.domain.Chat
import com.example.messenger.domain.Contact
import com.example.messenger.domain.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MessengerServerApi {
    @POST("/api/v1/user/sign-in")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/user/sign-up")
    fun register(@Body registerRequest: RegisterRequest): Call<ResponseBody>

    @GET("/api/v1/chat/all")
    fun getChats(@Query("user-id") userId : String, @Header("Authorization") token : String): Call<List<Chat>>

    @GET("/api/v1/contact/all")
    fun getContacts(@Header("Authorization") token: String): Call<List<Contact>>

    @POST("/api/v1/contact/")
    fun createContact(@Body createContactRequest: CreateContactRequest, @Header("Authorization") token: String): Call<ResponseBody>

    @POST("/api/v1/chat/")
    fun createChat(@Body createChatRequest: CreateChatRequest, @Header("Authorization") token: String): Call<ResponseBody>

    @GET("/api/v1/chat/members/{id}/")
    fun getMembers(@Path("id") id: String, @Header("Authorization") token: String): Call<List<Contact>>

    @POST("/api/v1/chat/add/members")
    fun addMembers(@Body addMembersRequest: AddMembersRequest, @Header("Authorization") token: String): Call<ResponseBody>

    @GET("/api/v1/chat/messages")
    fun getMessages(@Query("chat-id") chatId: String, @Query("page-id") pageId: String, @Header("Authorization") token: String): Call<List<Message>>
}