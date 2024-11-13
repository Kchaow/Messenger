package com.example.messenger.api

import android.util.Log
import com.example.messenger.domain.Message
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

private const val TAG = "ChatWebSocketClient"

class ChatWebSocketClient(serverUri: URI,  httpHeaders: Map<String, String>, private val messageListener: (String) -> Unit) : WebSocketClient(serverUri, httpHeaders) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        //onOpen

    }

    override fun onMessage(message: String?) {
        Log.i(TAG, "Message received $message")
        //добавлять в list сообщение
        messageListener(message!!)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        //onClose
    }

    override fun onError(ex: Exception?) {
        //onError
    }

}