package com.example.personaldetailsform_kotlin.model

data class User (
    val name: String,
    val age: Int,
    val email: String,
    val phone: String,
    val photo: String = ""
)