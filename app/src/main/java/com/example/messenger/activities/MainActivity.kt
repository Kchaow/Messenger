package com.example.messenger.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.example.messenger.api.MessengerServer
import com.example.messenger.domain.Chat
import retrofit2.Response

private const val REQUEST_CODE_SIGN_IN = 567
private const val REQUEST_CODE_REGISTER = 666

private const val TAG = "MainActivity"
private const val PREFS_NAME = "PREFERENCE"
private const val PREFS_USER_ID = "USER_ID"
private const val PREFS_ACCESS_TOKEN = "PREFS_ACCESS_TOKEN"
private const val PREFS_REFRESH_TOKEN = "PREFS_REFRESH_TOKEN"

const val EXTRA_CHAT_NAME = "EXTRA_CHAT_NAME"
const val EXTRA_USER_ID = "EXTRA_USER_ID"
const val EXTRA_CHAT_ID = "EXTRA_CHAT_ID"
const val EXTRA_ACCESS_TOKEN = "EXTRA_ACCESS_TOKEN"

class MainActivity : AppCompatActivity() {
    private var userId: String? = null
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private lateinit var recyclerView: RecyclerView
    private var adapter: ChatAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        loadPreferences()

        if (userId == null) {
            runLoginActivity()
        }

        recyclerView = findViewById(R.id.recycler_view_chats)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        updateRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    override fun onStop() {
        savePreferences()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
//        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        return when (item.itemId) {
            R.id.recycler_view_item -> {
                Log.i(TAG, "Удалить чат ${recyclerView.findViewHolderForAdapterPosition(item.groupId)?.itemView?.findViewById<TextView>(R.id.owner_chat)?.text.toString()}")
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.log_out) {
            userId = null
            accessToken = null
            refreshToken = null
            savePreferences()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
            return true
        } else if (id == R.id.contact) {
            val intent = Intent(this@MainActivity, ContactsActivity::class.java)
            intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
            startActivity(intent)
            return true
        } else if (id == R.id.chat_create) {
            val intent = Intent(this@MainActivity, ChatCreateActivity::class.java)
            intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
            startActivity(intent)
            return true
        } else if (id == R.id.username_set) {
            val intent = Intent(this@MainActivity, UsernameSetActivity::class.java)
            intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            finish()
        }

        if (requestCode == REQUEST_CODE_SIGN_IN || requestCode == REQUEST_CODE_REGISTER) {
            userId = data?.getStringExtra(USER_ID)
            accessToken = data?.getStringExtra(ACCESS_TOKEN)
            refreshToken = data?.getStringExtra(REFRESH_TOKEN)
            savePreferences()
            Log.i(TAG, userId.toString())
        }
    }

    private fun runLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    private fun savePreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(PREFS_USER_ID, userId)
        editor.putString(PREFS_ACCESS_TOKEN, accessToken)
        editor.putString(PREFS_REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    private fun loadPreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        userId = settings.getString(PREFS_USER_ID, null)
        accessToken = settings.getString(PREFS_ACCESS_TOKEN, null)
        refreshToken = settings.getString(PREFS_REFRESH_TOKEN, null)
    }

    private fun updateRecyclerView() {
        if (userId == null) {
            return
        }
        val messageServerLiveData: LiveData<Response<List<Chat>>> = MessengerServer().getChats(userId!!, accessToken!!)
        messageServerLiveData.observe(this, Observer {
            response ->
                if (response.code() == 200) {
                    adapter = ChatAdapter(response.body()!!)
                    response.body()!!.forEach {
                        chat-> Log.i(TAG, chat.name.toString())
                    }
                    recyclerView.adapter = adapter
                    Log.i(TAG, "Get chats success")
                } else {
                    Log.i(TAG, "Unable to get chats")
                }
        })

    }

    private inner class ChatHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener, View.OnClickListener {
        private val chatTitle: TextView = itemView.findViewById<TextView>(R.id.title_chat)
//        private val chatOwner: TextView = itemView.findViewById<TextView>(R.id.owner_chat)

        lateinit var chat: Chat

        init {
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(chat: Chat) {
            this.chat = chat
            chatTitle.text = chat.name
//            chatOwner.text = chat.ownerId
        }

        override fun onClick(v: View?) {
            val intent = Intent(this@MainActivity, ChatActivity::class.java)
            intent.putExtra(EXTRA_CHAT_NAME, chat.name)
            intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
            intent.putExtra(EXTRA_CHAT_ID, chat.id)
            intent.putExtra(EXTRA_USER_ID, userId)
            startActivity(intent)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
                menu!!.add(this.absoluteAdapterPosition, v!!.id, 0, R.string.delete_chat_title)
            Log.i(TAG, "Контекстное меню")
        }
    }

    private inner class ChatAdapter(var chats: List<Chat>) : RecyclerView.Adapter<ChatHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
            val view = layoutInflater.inflate(R.layout.chat_item, parent, false)
            return ChatHolder(view)
        }

        override fun getItemCount() = chats.size

        override fun onBindViewHolder(holder: ChatHolder, position: Int) {
            holder.bind(chats[position])
        }

    }
}
