package com.learn.storyappbyoby.view.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


//Glide
fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .circleCrop()
        .fitCenter()
        .into(this)
}