package com.example.anothertryofmusicanim

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.anothertryofmusicanim.entities.Radio

@BindingAdapter("loadImageFromUrl")
fun loadImage(view: ImageView, radio: Radio){
    val imageUrl = radio.picLink
    Glide.with(view.context)
        .asBitmap()
        .load(imageUrl)
        .into(object : SimpleTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                radio.pic = resource
                view.setImageBitmap(radio.pic)
            }

        })
}