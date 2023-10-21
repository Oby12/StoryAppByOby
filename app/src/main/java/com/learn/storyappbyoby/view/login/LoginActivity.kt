package com.learn.storyappbyoby.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.pref.UserModel
import com.learn.storyappbyoby.databinding.ActivityLoginBinding
import com.learn.storyappbyoby.view.ViewModelFactory
import com.learn.storyappbyoby.view.custom.MyButton
import com.learn.storyappbyoby.view.custom.MyEditText
import com.learn.storyappbyoby.view.main.MainActivity
import com.learn.storyappbyoby.view.signup.SignupViewModel
import com.learn.storyappbyoby.view.welcome.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    //checking validation
    private lateinit var myButton: MyButton
    private lateinit var myEditTextPassword: MyEditText
    private lateinit var myEditTextEmail : MyEditText


    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel

    //showloading
    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = findViewById(R.id.loginButton)
        myEditTextPassword = findViewById(R.id.passwordEditText)
        myEditTextEmail = findViewById(R.id.emailEditText)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        setMyButtonEnable(myEditTextPassword)


        //checking password
        myEditTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable(myEditTextPassword)
            }
            override fun afterTextChanged(s: Editable) {

            }
        })
        //checking email
        myEditTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val validEmail = isValidEmail(s.toString())
                if (!validEmail){
                    myEditTextEmail.setError(getString(R.string.email_set_eror),null)
                }else {
                    myEditTextEmail.error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
           binding.apply {
               if (emailEditText.error.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()) {
                   val email = emailEditText.text.toString().trim()
                   val password = passwordEditText.text.toString().trim()
                   loginViewModel.login(email, password)
               }
           }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val messageText = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val editEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val editPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(login)
        }

        AnimatorSet().apply {
            playSequentially(title,messageText,emailText, editEmail,passwordText,editPassword, together)
            start()
        }
    }

    private fun setupViewModel(){
        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this,myFactory)[LoginViewModel::class.java]

        loginViewModel.loginResponse.observe(this){
            when (it){
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.expression_first))
                        setMessage(getString(R.string.message_login))
                        setPositiveButton(getString(R.string.bottom_next_signup_login)) { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is ResultState.Error -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.expression_second))
                        setMessage(getString(R.string.message_no_valid_email))
                        create()
                        show()
                    }
                    showLoading(false)
                }
                else -> {
                    showToast(message = "")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    //checking edit text fro button
    private fun setMyButtonEnable(editText: MyEditText) {
        val result = editText.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
    //validation email
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}