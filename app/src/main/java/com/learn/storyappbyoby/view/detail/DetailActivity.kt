package com.learn.storyappbyoby.view.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.databinding.ActivityDetailBinding
import com.learn.storyappbyoby.view.main.MainActivity
import com.learn.storyappbyoby.view.utils.loadImage

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var itemList : ListStoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //val id = intent.getStringExtra(TAG)

        itemList = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(TAG, ListStoryItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(TAG)!!
        }

        binding.fabBackMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        detail()
    }


    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun detail(){
        binding.apply {
            showLoading(true)
            tvUsernameStoryDetail.text = itemList.name
            descriptionDetail.text = itemList.description
            imgDetail.loadImage(itemList.photoUrl.toString())
            imgUserDetail.setImageResource(R.drawable.wendy)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object{
        const val TAG = "StoryList"
    }
}