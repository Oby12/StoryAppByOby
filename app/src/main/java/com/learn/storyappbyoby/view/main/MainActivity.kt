package com.learn.storyappbyoby.view.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.databinding.ActivityMainBinding
import com.learn.storyappbyoby.view.ViewModelFactory
import com.learn.storyappbyoby.view.setting.SettingActivity
import com.learn.storyappbyoby.view.upload_story.UploadStoryActvity
import com.learn.storyappbyoby.view.welcome.WelcomeActivity


class MainActivity : AppCompatActivity() {

    private lateinit var factory : ViewModelFactory

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    private lateinit var adapter : StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //list manager
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        //setupView()
        setupViewModel()
        startUpload()

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else {
                mainViewModel.getListStoryUser(user.token)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile_menu -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun setupView() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.title = ""
//    }

    private fun startUpload() {
        binding.fabStoryUpload.setOnClickListener {
            val intent = Intent(this, UploadStoryActvity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel(){
        val myFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this,myFactory)[MainViewModel::class.java]

        mainViewModel.listStoryUser.observe(this){
            when(it){
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    adapter = StoryAdapter(it.data)
                    binding.rvStory.adapter = adapter
                }
                is ResultState.Error -> {
                    showLoading(false)
                }

                else -> {
                    showToast(message = "")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvStory.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


   /* private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }*/
}