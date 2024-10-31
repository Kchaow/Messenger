package com.example.messenger.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.messenger.R
import com.example.messenger.api.MessengerServer
import com.example.messenger.api.dto.LoginRequest
import com.example.messenger.api.dto.LoginResponse
import retrofit2.Response

private const val TAG = "LoginActivity"
const val USER_ID = "USER_ID"
const val ACCESS_TOKEN = "ACCESS_TOKEN"
const val REFRESH_TOKEN = "REFRESH_TOKEN"

class LoginActivity : AppCompatActivity() {
    private lateinit var registerInviteTextView : TextView
    private lateinit var errorMessageTextView : TextView
    private lateinit var loginButton : TextView
    private lateinit var loginEditText : EditText
    private lateinit var passwordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        registerInviteTextView = findViewById(R.id.register_invite)
        loginButton = findViewById(R.id.login_button)
        loginEditText = findViewById(R.id.login_edit)
        passwordEditText = findViewById(R.id.password_edit)
        errorMessageTextView = findViewById(R.id.error_login_message)

        registerInviteTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val loginRequest = LoginRequest()
        loginRequest.login = loginEditText.text.toString().trim()
        loginRequest.password = passwordEditText.text.toString().trim()
        if (loginRequest.login.isBlank()) {
            errorMessageTextView.text = "Введите логин"
            return
        } else if (loginRequest.password.isBlank()) {
            errorMessageTextView.text = "Введите пароль"
            return
        }
        val messageServerLiveData: LiveData<Response<LoginResponse>> = MessengerServer().login(loginRequest)
        messageServerLiveData.observe(
            this,
            Observer { response ->
                if (response.code() == 200) {
                    val data = Intent().apply {
                        putExtra(USER_ID, response.body()?.userId.toString())
                        putExtra(ACCESS_TOKEN, response.body()?.token?.accessToken)
                        putExtra(REFRESH_TOKEN, response.body()?.token?.accessToken)
                    }
                    setResult(Activity.RESULT_OK, data)
                    finish()
                } else {
                    errorMessageTextView.text = "Неверный логин или пароль"
                }
            }
        )
    }
}