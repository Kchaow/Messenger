package com.example.messenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.example.messenger.api.MessengerServer
import com.example.messenger.api.dto.AddMembersRequest
import com.example.messenger.domain.Contact
import okhttp3.ResponseBody
import retrofit2.Response

class ChatMembersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: ContactAdapter? = null
    private var members: List<Contact> = ArrayList<Contact>()
    private var chatName : String? = null
    private var chatId : String? = null
    private var accessToken: String? = null
    private lateinit var errorMessageTextView: TextView
    private lateinit var addMemberButton: Button
    private lateinit var chatTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_members)
        errorMessageTextView = findViewById(R.id.error_members_message)
        addMemberButton = findViewById(R.id.add_member_button)
        chatTitle = findViewById(R.id.chat_title)
        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)
        chatId = intent.getStringExtra(EXTRA_CHAT_ID)
        chatName = intent.getStringExtra(EXTRA_CHAT_NAME)
        recyclerView = findViewById(R.id.recycler_view_members)
        recyclerView.layoutManager = LinearLayoutManager(this@ChatMembersActivity)

        chatTitle.text = chatName

        addMemberButton.setOnClickListener {
            addMember()
        }

        loadMembers()
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    private fun addMember() {
        val intent = Intent(this@ChatMembersActivity, ContactSelectActivity::class.java)
        intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken)
        startActivityForResult(intent, REQUEST_CODE_CONTACTS_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CONTACTS_SELECT) {
            val contact = Contact()
            contact.id = data?.getStringExtra(USER_ID)
            contact.login = data?.getStringExtra(USER_LOGIN)
            val addMembersRequest = AddMembersRequest()
            addMembersRequest.chatId = chatId!!.toInt()
            addMembersRequest.membersIds = listOf(contact.id!!.toInt())
            val messageServerLiveData: LiveData<Response<ResponseBody>> = MessengerServer().addMembers(addMembersRequest, accessToken!!)
            messageServerLiveData.observe(
                this,
                Observer { response ->
                    if (response.code() == 201) {
                        loadMembers()
                    } else {
                        errorMessageTextView.text = "Произошла ошибка"
                    }
                }
            )
        }
    }

    private fun loadMembers() {
        val messageServerLiveData: LiveData<Response<List<Contact>>> = MessengerServer().getMembers(chatId!!, accessToken!!)
        messageServerLiveData.observe(
            this,
            Observer { response ->
                if (response.code() == 200) {
                    members = response.body()!!
                    updateRecyclerView()
                } else {
                    errorMessageTextView.text = "Произошла ошибка"
                }
            }
        )
    }

    private fun updateRecyclerView() {
        adapter = ContactAdapter(members)
        recyclerView.adapter = adapter
    }

    private inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactTitle: TextView = itemView.findViewById<TextView>(R.id.title_contact)

        lateinit var contact: Contact

        fun bind(contact: Contact) {
            this.contact = contact
            contactTitle.text = contact.login
        }
    }

    private inner class ContactAdapter(var contacts: List<Contact>) : RecyclerView.Adapter<ContactHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            val view = layoutInflater.inflate(R.layout.contact_item, parent, false)
            return ContactHolder(view)
        }

        override fun getItemCount() = contacts.size

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            holder.bind(contacts[position])
        }
    }
}