package com.example.personaldetailsform_kotlin

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaldetailsform_kotlin.model.Photo
import com.example.personaldetailsform_kotlin.network.ImageLoader


// Recyclerview
// * Used to load more data efficiently
// * When a screen loads, data visible in screen alone is rendered, when scrolled old view is used to render new data
// * view is built separately, data is loaded separately

class PhotoAdapter(private val activity: Activity, private val photoList: ArrayList<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
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
        ImageLoader.loadImage(photoList[position].download_url, holder.imageView)

        val currentItem = photoList[position]

        holder.itemView.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Set Profile Picture")
            builder.setMessage("Are you sure you want to set this image as profile picture?")

            builder.setPositiveButton("Yes") { dialog, which ->
                val resultIntent = Intent()
                resultIntent.putExtra("picture", currentItem.download_url)
                activity.setResult(AppCompatActivity.RESULT_OK, resultIntent)
                activity.finish()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}