package com.example.personaldetailsform_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personaldetailsform_kotlin.model.Photo


// Recyclerview
// * Used to load more data efficiently
// * When a screen loads, data visible in screen alone is rendered, when scrolled old view is used to render new data
// * view is built separately, data is loaded separately

class PhotoAdapter( private val photoList: ArrayList<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // what view must a viewholder have?
        val imageView: ImageView = view.findViewById(R.id.imageViewPhoto)
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater
            // parent -> recyclerview
            .from(parent.context)
            // R.layout.item_photo -> what ui must be loaded?
            // false -> loaded now (no). loading is handled on scroll in recyclerview
            .inflate(
                R.layout.item_photo,
                parent,
                false
            )
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder( holder: PhotoViewHolder, position: Int) {
        // Glide -> Library used to load images from URL easily
        Glide.with(holder.itemView.context)
            .load(photoList[position].download_url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}