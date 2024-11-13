package com.example.messenger.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.example.messenger.api.ChatWebSocketClient
import com.example.messenger.api.MessengerServer
import com.example.messenger.domain.Message
import org.java_websocket.client.WebSocketClient
import retrofit2.Response
import java.net.URI

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity() {
    private lateinit var chatHeader : TextView
    private var chatName : String? = null
    private var chatId : String? = null
    private var userId : String? = null
    private var accessToken: String? = null
    private lateinit var chatHeaderLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private var adapter: MessageAdapter? = null
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var sendButton: Button
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatHeader = findViewById(R.id.chat_header)
        chatHeaderLayout = findViewById(R.id.chat_header_layout)
        chatName = intent.getStringExtra(EXTRA_CHAT_NAME)
        userId = intent.getStringExtra(EXTRA_USER_ID)
        chatHeader.text = chatName
        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)
        chatId = intent.getStringExtra(EXTRA_CHAT_ID)
        sendButton = findViewById(R.id.send_button)
        inputEditText = findViewById(R.id.input_edit_text)

        val serverUri = URI("ws://193.124.33.25:8080/api/v1/messenger/connect?chat-id=${chatId}")
        val headers = mapOf(Pair("Authorization", "Bearer $accessToken"))
        webSocketClient = ChatWebSocketClient(serverUri, headers) {
            updateRecyclerView()
        }

        chatHeaderLayout.setOnClickListener {
            openMembers()
        }

        sendButton.setOnClickListener {
            sendMessage()
        }

        recyclerView = findViewById(R.id.recycler_view_messages)
        recyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)
        updateRecyclerView()

        webSocketClient.connect()
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

    private fun sendMessage() {
        val text = inputEditText.text.trim()
        if (text.isBlank()) {
            return
        }
        val message = "{ \"text\": \"${text}\" }"
        webSocketClient.send(message)
        updateRecyclerView()
        inputEditText.text.clear()
    }

    private fun updateRecyclerView() {
        val messageServerLiveData: LiveData<Response<List<Message>>> = MessengerServer().getMessages(chatId!!, "0", accessToken!!)
        messageServerLiveData.observe(this, Observer {
                response ->
            if (response.code() == 200) {
                adapter = MessageAdapter(response.body()!!)
                recyclerView.adapter = adapter
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val lastPosition = recyclerView.adapter!!.itemCount - 1
                layoutManager!!.scrollToPositionWithOffset(lastPosition, 0)
                Log.i(TAG, "Get messages success")
            } else {
                Log.i(TAG, "Unable to get messages")
            }
        })

    }

    private fun openMembers() {
        val intent = Intent(this@ChatActivity, ChatMembersActivity::class.java)
        intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
        intent.putExtra(EXTRA_CHAT_NAME, chatName)
        intent.putExtra(EXTRA_CHAT_ID, chatId)
        startActivity(intent)
    }

    private inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageLayout: LinearLayout = itemView.findViewById<LinearLayout>(R.id.message_layout)
        private val contentMessage: TextView = itemView.findViewById<TextView>(R.id.message_content)
        private val senderMessage: TextView = itemView.findViewById<TextView>(R.id.message_login)

        lateinit var message: Message

        fun bind(message: Message) {
            this.message = message
            if (message.senderId == userId) {
                messageLayout.gravity = Gravity.END
            }
            contentMessage.text = message.text
            senderMessage.text = message.userName
            if (senderMessage.text.isBlank()) {
                senderMessage.text = "<имя не установлено>"
            }
        }
    }

    private inner class MessageAdapter(var messages: List<Message>) : RecyclerView.Adapter<MessageHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
            val view = layoutInflater.inflate(R.layout.message_item, parent, false)
            return MessageHolder(view)
        }

        override fun getItemCount() = messages.size

        override fun onBindViewHolder(holder: MessageHolder, position: Int) {
            holder.bind(messages[position])
        }

    }
}