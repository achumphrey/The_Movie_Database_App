package com.example.themoviedatabaseapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.themoviedatabaseapp.R

/**
 *  A [BindingAdapter] that uses the Glide library to load the video image.
 */
@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, image : String?){
    val httpPrefix = "https://www.themoviedb.org/t/p/w220_and_h330_face"
    image.let {
        Glide.with(imageView.context)
            .load(httpPrefix + it)
            .placeholder(R.mipmap.ic_launcher)
            .into(imageView)
    }
}

/**
 * A [BindingAdapter] to show HTML text
 * **/
@BindingAdapter("htmlTxt")
fun loadDescription(textView: TextView, value : String?){
    textView.text = value?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
}