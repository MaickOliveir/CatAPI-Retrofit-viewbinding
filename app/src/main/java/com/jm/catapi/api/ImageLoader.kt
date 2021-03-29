package com.jm.catapi.api

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(imageUrl : String, imageView : ImageView)
}