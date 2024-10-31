package com.example.messenger.api

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class ChatWebSocketClient(serverUri: URI,  httpHeaders: Map<String, String>, private val messageListener: (String) -> Unit) : WebSocketClient(serverUri, httpHeaders) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        //onOpen

    }

    override fun onMessage(message: String?) {
        messageListener.invoke(message ?: "")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        //onClose
    }

    override fun onError(ex: Exception?) {
        //onError
    }

}