package com.jm.catapi

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.jm.catapi.api.GlideImageLoader
import com.jm.catapi.api.ImageLoader
import com.jm.catapi.api.TheCatApiService
import com.jm.catapi.data.ImageResultData
import com.jm.catapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    private val theCatApiService by lazy {
        retrofit.create(TheCatApiService::class.java) }

    private val imageLoader : ImageLoader by lazy { GlideImageLoader(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btNextAgent.setOnClickListener {
            getCatImageResponse()
        }

        getCatImageResponse()

    }

    private fun getCatImageResponse() {
        val call = theCatApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<ImageResultData>> {
            override fun onFailure(call: Call<List<ImageResultData>>, t: Throwable) {
                Log.e("MainActivity", "Failed to get search results", t)
            }

            override fun onResponse(
                    call: Call<List<ImageResultData>>,
                    response: Response<List<ImageResultData>>
            ) {
                if (response.isSuccessful) {
                    val imageResults = response.body()
                    val firstImageUrl = imageResults?.firstOrNull()
                        ?.imageUrl ?: "No URL"

                    if (firstImageUrl.isNotBlank()) {
                        imageLoader.loadImage(firstImageUrl,
                            binding.mainProfileImage)
                            } else {
                                Log.d("MainActivity", "Missing image URL")
                            }
                     binding.mainAgentBreedValue.text =
                            imageResults?.firstOrNull()?.breeds?.firstOrNull()?.name ?: "Unknown"

                }
            }
        })
    }
}