package com.jm.catapi.data

import com.squareup.moshi.Json

data class ImageResultData(
    @field : Json(name = "url") val imageUrl : String,
    val breeds : List<CatBreedData>
)

data class CatBreedData(
    val name : String,
    val temperament : String
)
