package com.learn.storyappbyoby.view.signup

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
import com.learn.storyappbyoby.databinding.ActivitySignupBinding
import com.learn.storyappbyoby.view.ViewModelFactory
import com.learn.storyappbyoby.view.custom.MyButton
import com.learn.storyappbyoby.view.custom.MyEditText
import com.learn.storyappbyoby.view.welcome.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    //checking validation
    private lateinit var myButton: MyButton
    private lateinit var myEditTextPassword: MyEditText
    private lateinit var myEditTextEmail : MyEditText
    private lateinit var myEditTextName : MyEditText

    //view model
    private lateinit var signUpViewModel : SignupViewModel
//    private val viewModel by viewModels<SignupViewModel> {
//        ViewModelFactory.getInstance(this)
//    }



    //showloading
    private fun showLoading(isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = binding.signupButton
        myEditTextPassword = binding.passwordEditText
        myEditTextEmail = binding.emailEditText
        myEditTextName = binding.nameEditText

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        setMyButtonEnable(myEditTextPassword)




        //checking password
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
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
        //checking name
        myEditTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val passwordValid = s.toString().length >= 2

                if (!passwordValid) {
                    myEditTextName.setError(getString(R.string.name_set_eror), null)
                } else {
                    myEditTextName.error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

//        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
//        signUpViewModel = ViewModelProvider(this,myFactory)[SignupViewModel::class.java]
//


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

    //action
    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            binding.apply {
                if (nameEditText.error.isNullOrEmpty() && emailEditText.error.isNullOrEmpty() && passwordEditText.error.isNullOrEmpty()) {
                    val name = nameEditText.text.toString().trim()
                    val email = emailEditText.text.toString().trim()
                    val password = passwordEditText.text.toString().trim()
                    signUpViewModel.signup(name, email, password)
                } else {
                    showToast(message = "account not created")
                }
            }

        }
    }
    //animation
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)
        val editName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val editEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val editPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(signup)
        }

        AnimatorSet().apply {
            playSequentially(title,nameText,editName,emailText, editEmail,passwordText,editPassword, together)
            start()
        }
    }

    private fun setupViewModel(){
        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        signUpViewModel = ViewModelProvider(this,myFactory)[SignupViewModel::class.java]

        signUpViewModel.signupResponse.observe(this){
            when (it){
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    val email = binding.emailEditText.text.toString()
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.expression_first))
                        setMessage(getString(R.string.message_signup, email))
                        setPositiveButton(getString(R.string.bottom_next_signup_login)) { _, _ ->
                            val intent = Intent(context, WelcomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is ResultState.Error -> {
                    showToast(it.error)
                    showLoading(false)
                }
                else -> {
                    showToast(message = "Nothing")
                }
            }
        }
    }

    //toast
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