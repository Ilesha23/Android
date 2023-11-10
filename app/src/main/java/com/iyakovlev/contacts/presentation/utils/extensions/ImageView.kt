package com.iyakovlev.contacts.presentation.utils.extensions

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.iyakovlev.contacts.R
import com.iyakovlev.contacts.common.constants.Constants
import com.iyakovlev.contacts.utils.log
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

fun ImageView.loadImageWithGlide(url: String?) {
    if (url != null) {
        Glide.with(this)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_person_24)
            .into(this)    }
}

fun ImageView.loadImageWithGlide(file: File?) {
    Glide.with(this)
        .load(file)
        .circleCrop()
        .placeholder(R.drawable.baseline_person_24)
        .error(R.drawable.baseline_person_24)
        .into(this)
}

fun ImageView.loadImageWithCoil(url: String) {
    this.load(url) {
        placeholder(R.drawable.baseline_person_24)
        error(R.drawable.baseline_person_24)
        transformations(CircleCropTransformation())
    }
}

fun ImageView.loadImageWithPicasso(url: String) {
    val roundedTransformation: Transformation = RoundedCornersTransformation(1000, 0)
    Picasso.get()
        .load(url)
        .centerCrop()
        .fit()
        .transform(RoundedCornersTransformation(R.dimen.avatar_list_size, 0))
        .placeholder(R.drawable.baseline_person_24)
        .error(R.drawable.baseline_person_24)
        .into(this)
}