package com.example.messenger.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.messenger.R
import com.example.messenger.api.MessengerServer
import com.example.messenger.api.dto.CreateContactRequest
import okhttp3.ResponseBody
import retrofit2.Response

class ContactCreateActivity : AppCompatActivity() {
    private lateinit var createContact: Button
    private lateinit var contactCreateEdit: EditText
    private lateinit var errorContactCreate: TextView
    private var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_create)
        createContact = findViewById(R.id.create_contact_button)
        contactCreateEdit = findViewById(R.id.contact_create_edit)
        errorContactCreate = findViewById(R.id.error_contact_message)

        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)

        createContact.setOnClickListener {
            createContact()
        }
    }

    private fun createContact() {
        val createContactRequest = CreateContactRequest()
        createContactRequest.login = contactCreateEdit.text.toString().trim()
        if (createContactRequest.login.isBlank()) {
            errorContactCreate.text = "Введите логин"
            return
        }
        val messageServerLiveData: LiveData<Response<ResponseBody>> = MessengerServer().createContact(createContactRequest, accessToken!!)
        messageServerLiveData.observe(this, Observer {
            response ->
                if (response.code() == 201) {
                    finish()
                } else {
                    errorContactCreate.text = "Произошла ошибка"
                }
        })
    }
}