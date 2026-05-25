package com.example.personaldetailsform_kotlin.model

import java.io.Serializable

// data -> represents that a class stores data.
// so it provides useful methods like toString(), copy()
// comparison of same objs from these class returns true
// EX: User("Ananya", 21) == User("Ananya", 21) ---> True
// Serializable = makes object transferable/storable from one activity to another activity
data class Photo(
    val id: String,
    val author: String,
    val download_url: String
) : Serializable