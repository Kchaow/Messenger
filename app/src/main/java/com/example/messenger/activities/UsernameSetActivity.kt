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
import com.example.messenger.api.dto.UsernameRequest
import okhttp3.ResponseBody
import retrofit2.Response

class UsernameSetActivity : AppCompatActivity() {
    private lateinit var errorMessageTextView: TextView
    private lateinit var usernameButton: Button
    private lateinit var usernameEdit: EditText
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username_set)
        errorMessageTextView = findViewById(R.id.error_username)
        usernameButton = findViewById(R.id.username_button)
        usernameEdit = findViewById(R.id.username_edit)

        accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN).toString()

        usernameButton.setOnClickListener {
            setUsername()
        }
    }

    private fun setUsername() {
        val usernameRequest = UsernameRequest()
        usernameRequest.username = usernameEdit.text.toString()
        if (usernameRequest.username.isBlank()) {
            errorMessageTextView.text = "Введите имя"
            return
        }
        val messageServerLiveData: LiveData<Response<ResponseBody>> = MessengerServer().setUsername(usernameRequest, accessToken)
        messageServerLiveData.observe(this, Observer {
                response ->
            if (response.code() == 200) {
                finish()
            } else {
                errorMessageTextView.text = "Произошла ошибка"
            }
        })
    }
}