package com.learn.storyappbyoby.view.main


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.learn.storyappbyoby.R
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.databinding.ItemStoryBinding
import com.learn.storyappbyoby.view.detail.DetailActivity
import com.learn.storyappbyoby.view.utils.loadImage
import androidx.core.util.Pair

class StoryAdapter(private val ListStoryActivity : List<ListStoryItem>):
        RecyclerView.Adapter<StoryAdapter.MyViewHolder>(){
    inner class MyViewHolder(
        private val binding : ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {

            fun bind(item : ListStoryItem){

                binding.apply {
                    tvUsernameStoryDetail.text = item.name
                    descriptionStory.text = item.description
                    imgStory.loadImage(item.photoUrl.toString())
                    imgUserDetail.setImageResource(R.drawable.wendy)

//                    binding.cardListStory.setOnClickListener{
//                        val intentDetail = Intent(itemView.context, DetailActivity::class.java)
//                        intentDetail.putExtra(DetailActivity.TAG,item)
//                    }

                    //set touch story
                    cardListStory.setOnClickListener {
                        val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                        intentDetail.putExtra(DetailActivity.TAG, item)

                        //animation
                        val option: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(binding.tvUsernameStoryDetail, "username"),
                                Pair(binding.descriptionStory, "description"),
                                Pair(binding.imgStory, "image"),
                                Pair(binding.imgUserDetail, "imageDetail")
                            )
                        itemView.context.startActivity(intentDetail, option.toBundle())
                    }
                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(ListStoryActivity[position])
    }

    override fun getItemCount(): Int = ListStoryActivity.size


}