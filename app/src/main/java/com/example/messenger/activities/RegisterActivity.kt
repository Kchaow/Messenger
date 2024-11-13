package com.example.messenger.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.messenger.R
import com.example.messenger.api.MessengerServer
import com.example.messenger.api.dto.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordAgainEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var errorMessageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loginEditText = findViewById(R.id.register_login_edit)
        passwordEditText = findViewById(R.id.register_password_edit)
        passwordAgainEditText = findViewById(R.id.register_password_edit_again)
        registerButton = findViewById(R.id.register_button)
        errorMessageTextView = findViewById(R.id.error_register_message)

        registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val registerRequest = RegisterRequest()
        registerRequest.login = loginEditText.text.toString().trim()
        registerRequest.password = passwordEditText.text.toString().trim()
        val againPassword = passwordAgainEditText.text.toString().trim()
        if (registerRequest.login.isBlank()) {
            errorMessageTextView.text = "Введите логин"
            return
        } else if (registerRequest.password.isBlank()) {
            errorMessageTextView.text = "Введите пароль"
            return
        } else if (againPassword.isBlank()) {
            errorMessageTextView.text = "Введите пароль повторно"
            return

        } else if (registerRequest.password != againPassword) {
            errorMessageTextView.text = "Пароли не совпадают"
            return
        }
        val messageServerLiveData: LiveData<Response<ResponseBody>> = MessengerServer().register(registerRequest)
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