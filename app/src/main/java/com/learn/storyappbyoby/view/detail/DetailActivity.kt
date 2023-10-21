package com.learn.storyappbyoby.view.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.databinding.ActivityDetailBinding
import com.learn.storyappbyoby.view.main.MainActivity
import com.learn.storyappbyoby.view.utils.loadImage

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

//    private lateinit var detailViewModel: DetailViewModel

//    private val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var itemList : ListStoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //val id = intent.getStringExtra(TAG)

        itemList = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("storyItem", ListStoryItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("storyItem")!!
        }

        binding.fabBackMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        detailViewModel.detail.observe(this){storyDetail ->
//            binding.apply {
//                tvUsernameStoryDetail.text = storyDetail.name
//            }
//        }

        detail()

    }


    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun detail(){
        binding.apply {
            tvUsernameStoryDetail.text = itemList.name
            descriptionDetail.text = itemList.description
            imgDetail.loadImage(itemList.photoUrl.toString())
            imgUserDetail.setImageResource(R.drawable.wendy)
        }
    }

    companion object{
        const val TAG = "DetailActivity"
        const val USERNAME = "image"
        const val DESCRIPTION = "description"
        const val IMG_STORY = "img_story"
        const val IMG_DETAIL = "img_detail"
    }
}