package com.example.messenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.example.messenger.api.dto.CreateChatRequest
import com.example.messenger.api.dto.LoginResponse
import com.example.messenger.domain.Contact
import okhttp3.ResponseBody
import retrofit2.Response

const val REQUEST_CODE_CONTACTS_SELECT = 234
const val USER_LOGIN = "USER_LOGIN"

class ChatCreateActivity : AppCompatActivity() {
    private var accessToken: String? = null
    private lateinit var recyclerView: RecyclerView
    private var adapter: ContactAdapter? = null
    private var members: List<Contact> = ArrayList<Contact>()
    private lateinit var createChatButton: Button
    private lateinit var addMemberButton: Button
    private lateinit var errorMessageTextView: TextView
    private lateinit var chatNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_create)
        createChatButton = findViewById(R.id.create_chat_button)
        addMemberButton = findViewById(R.id.add_member_button)
        errorMessageTextView = findViewById(R.id.error_create_chat_message)
        chatNameEditText = findViewById(R.id.chat_create_edit)

        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)

        recyclerView = findViewById(R.id.recycler_view_members)
        recyclerView.layoutManager = LinearLayoutManager(this@ChatCreateActivity)
        updateRecyclerView()

        addMemberButton.setOnClickListener {
            addMember()
        }
        createChatButton.setOnClickListener {
            createChat()
        }
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    private fun createChat() {
        if (members.isEmpty()) {
            errorMessageTextView.text = "Добавьте хотя бы одного участника"
            return
        }
        val createChatRequest = CreateChatRequest()
        createChatRequest.name = chatNameEditText.text.toString().trim()
        createChatRequest.membersIds = members.map { it.id!!.toInt() }
        if (createChatRequest.name.isBlank()) {
            errorMessageTextView.text = "Введите название чата"
            return
        }
        val messageServerLiveData: LiveData<Response<ResponseBody>> = MessengerServer().createChat(createChatRequest, accessToken!!)
        messageServerLiveData.observe(
            this,
            Observer { response ->
                if (response.code() == 201) {
                    finish()
                } else {
                    errorMessageTextView.text = "Произошла ошибка"
                }
            }
        )
    }

    private fun addMember() {
        val intent = Intent(this@ChatCreateActivity, ContactSelectActivity::class.java)
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
            members = members + contact
            updateRecyclerView()
        }
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