package com.example.messenger.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.messenger.api.dto.AddMembersRequest
import com.example.messenger.api.dto.CreateChatRequest
import com.example.messenger.api.dto.CreateContactRequest
import com.example.messenger.api.dto.LoginRequest
import com.example.messenger.api.dto.LoginResponse
import com.example.messenger.api.dto.RegisterRequest
import com.example.messenger.api.dto.UsernameRequest
import com.example.messenger.domain.Chat
import com.example.messenger.domain.Contact
import com.example.messenger.domain.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MessengerServer"

class MessengerServer {
    private val messengerServerApi: MessengerServerApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://193.124.33.25:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        messengerServerApi = retrofit.create(MessengerServerApi::class.java)
    }

    fun login(loginRequest: LoginRequest): LiveData<Response<LoginResponse>> {
        val responseLiveData: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
        val messengerServerApiRequest: Call<LoginResponse> = messengerServerApi.login(loginRequest)

        messengerServerApiRequest.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun register(registerRequest: RegisterRequest): LiveData<Response<ResponseBody>> {
        val responseLiveData: MutableLiveData<Response<ResponseBody>> = MutableLiveData()
        val messengerServerApiRequest: Call<ResponseBody> = messengerServerApi.register(registerRequest)

        messengerServerApiRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun getChats(userId : String, accessToken: String): LiveData<Response<List<Chat>>> {
        val responseLiveData: MutableLiveData<Response<List<Chat>>> = MutableLiveData()
        val messengerServerApiRequest: Call<List<Chat>> = messengerServerApi.getChats(userId, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<List<Chat>> {
            override fun onFailure(call: Call<List<Chat>>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<List<Chat>>, response: Response<List<Chat>>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun getContacts(accessToken: String): LiveData<Response<List<Contact>>> {
        val responseLiveData: MutableLiveData<Response<List<Contact>>> = MutableLiveData()
        val messengerServerApiRequest: Call<List<Contact>> = messengerServerApi.getContacts("Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<List<Contact>> {
            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun createContact(createContactRequest: CreateContactRequest, accessToken: String): LiveData<Response<ResponseBody>> {
        val responseLiveData: MutableLiveData<Response<ResponseBody>> = MutableLiveData()
        val messengerServerApiRequest: Call<ResponseBody> = messengerServerApi.createContact(createContactRequest, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun createChat(createChatRequest: CreateChatRequest, accessToken: String): LiveData<Response<ResponseBody>> {
        val responseLiveData: MutableLiveData<Response<ResponseBody>> = MutableLiveData()
        val messengerServerApiRequest: Call<ResponseBody> = messengerServerApi.createChat(createChatRequest, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun getMembers(id: String, accessToken: String): LiveData<Response<List<Contact>>> {
        val responseLiveData: MutableLiveData<Response<List<Contact>>> = MutableLiveData()
        val messengerServerApiRequest: Call<List<Contact>> = messengerServerApi.getMembers(id, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<List<Contact>> {
            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun addMembers(addMembersRequest: AddMembersRequest, accessToken: String): LiveData<Response<ResponseBody>> {
        val responseLiveData: MutableLiveData<Response<ResponseBody>> = MutableLiveData()
        val messengerServerApiRequest: Call<ResponseBody> = messengerServerApi.addMembers(addMembersRequest, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun getMessages(chatId: String, pageId: String, accessToken: String): LiveData<Response<List<Message>>> {
        val responseLiveData: MutableLiveData<Response<List<Message>>> = MutableLiveData()
        val messengerServerApiRequest: Call<List<Message>> = messengerServerApi.getMessages(chatId, pageId, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<List<Message>> {
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }

    fun setUsername(usernameRequest: UsernameRequest, accessToken: String): LiveData<Response<ResponseBody>> {
        val responseLiveData: MutableLiveData<Response<ResponseBody>> = MutableLiveData()
        val messengerServerApiRequest: Call<ResponseBody> = messengerServerApi.setUsername(usernameRequest, "Bearer $accessToken")

        messengerServerApiRequest.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Unable to make request", t)
                responseLiveData.value = null
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                responseLiveData.value = response
            }
        })
        return responseLiveData
    }
}