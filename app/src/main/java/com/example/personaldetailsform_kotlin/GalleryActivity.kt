package com.example.personaldetailsform_kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personaldetailsform_kotlin.model.Photo

class GalleryActivity : AppCompatActivity() {

    lateinit var toolBar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gallery)

        val photoList = intent.getSerializableExtra("data") as ArrayList<Photo>

        val recyclerView = findViewById<RecyclerView>( R.id.recyclerViewPhotos)

        // layoutManager -> How to display the views. linear, grid, staggeredGrid
        // * Linear - normal view, vertical display
        // * Grid - 2D layout like a table/ grid (even height/ width of each cell)
        // * staggered -  same like grid, uneven layout (like pinterest)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // adapter -> create view and bind data to the view
        recyclerView.adapter = PhotoAdapter(photoList)

        toolBar = findViewById(R.id.galleryToolbar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
                finish()
        }
    }
}