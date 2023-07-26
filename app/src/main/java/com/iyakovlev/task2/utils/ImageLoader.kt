package com.iyakovlev.task2.utils

import android.content.Context
import android.widget.ImageView
//import coil.Coil
//import coil.load
//import coil.transform.CircleCropTransformation
//import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
//import com.facebook.drawee.backends.pipeline.Fresco
import com.iyakovlev.task2.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

import com.squareup.picasso.Transformation

//import com.squareup.picasso.Picasso

object ImageLoader {

    fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(imageView)

//        imageView.load(url) {
//            placeholder(R.drawable.baseline_person_24)
//            error(R.drawable.baseline_person_24)
//            transformations(CircleCropTransformation())
//        }


//        val roundedTransformation: Transformation = RoundedCornersTransformation(1000, 0)
//        Picasso.get()
//            .load(url)
//            .transform(roundedTransformation)
//            .placeholder(R.drawable.baseline_person_24)
//            .error(R.drawable.baseline_person_24)
//            .into(imageView)
    }

}