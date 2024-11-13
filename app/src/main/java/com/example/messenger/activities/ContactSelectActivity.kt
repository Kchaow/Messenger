package com.example.messenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import com.example.messenger.domain.Contact
import retrofit2.Response

private const val TAG = "ContactSelectActivity"

class ContactSelectActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var accessToken: String? = null
    private var adapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_select)

        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)

        recyclerView = findViewById(R.id.recycler_view_contacts)
        recyclerView.layoutManager = LinearLayoutManager(this@ContactSelectActivity)
        updateRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        val messageServerLiveData: LiveData<Response<List<Contact>>> = MessengerServer().getContacts(accessToken!!)
        messageServerLiveData.observe(this, Observer {
                response ->
            if (response.code() == 200) {
                adapter = ContactAdapter(response.body()!!)
                recyclerView.adapter = adapter
                Log.i(TAG, "Get contacts success")
            } else {
                Log.i(TAG, "Unable to get contacts")
            }
        })
    }

    private inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val contactTitle: TextView = itemView.findViewById<TextView>(R.id.title_contact)
        private val contactUsername: TextView = itemView.findViewById<TextView>(R.id.title_username_contact)

        lateinit var contact: Contact

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val data = Intent().apply {
                putExtra(USER_ID, contact.id)
                putExtra(USER_LOGIN, contact.login)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        fun bind(contact: Contact) {
            this.contact = contact
            contactTitle.text = contact.login
            contactUsername.text = contact.username
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