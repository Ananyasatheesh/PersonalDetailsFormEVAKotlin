package com.example.personaldetailsform_kotlin.network

import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.URL

object ImageLoader {

    fun loadImage(imageUrl: String, imageView: ImageView) {

        Thread {

            try {

                // * converts string into URL (URL(imageUrl))
                // * opens stream to receive bytes from internet to our app (hits API)
                // * received bytes must be converted into BitMap by BitmapFactory.decodeStream
                // * BitMap - reads JPEG, JPG, PNG byes, decompresses img and builds pixel
                val bitmap = BitmapFactory.decodeStream(URL(imageUrl).openStream())

                // as we are running this in new thread,
                // we are requesting main thread to display image from background thread using .post()

                imageView.post {

                    imageView.setImageBitmap(bitmap)

                }

            } catch (e: Exception) {

                e.printStackTrace()

            }

        }.start()
    }
}