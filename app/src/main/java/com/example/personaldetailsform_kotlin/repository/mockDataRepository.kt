package com.example.personaldetailsform_kotlin.repository

import com.example.personaldetailsform_kotlin.BuildConfig
import com.example.personaldetailsform_kotlin.model.User

object MockDataRepository {

    private val previewDevUsers = listOf(
        User(
            "Ananya Dev",
            22,
            "ananya@gmail.com",
            "9876543210"
        ),
        User(
            "John Dev",
            25,
            "john@gmail.com",
            "9876543211"
        )
    )

    private val previewQaUsers = listOf(
        User(
            "QA User 1",
            30,
            "qa1@test.com",
            "9000000001"
        ),
        User(
            "QA User 2",
            31,
            "qa2@test.com",
            "9000000002"
        )
    )

    private val mediaDevUsers = listOf(
        User(
            "Ananya Media",
            22,
            "ananya@gmail.com",
            "9876543210",
            "https://picsum.photos/id/0/5000/3333"
        ),
        User(
            "John Media",
            25,
            "john@gmail.com",
            "9876543211",
            "https://picsum.photos/id/0/5000/3333"
        )
    )

    private val mediaQaUsers = listOf(
        User(
            "QA Media 1",
            30,
            "qa1@test.com",
            "9000000001",
            "https://picsum.photos/id/1/5000/3333"
        ),
        User(
            "QA Media 2",
            31,
            "qa2@test.com",
            "9000000002",
            "https://picsum.photos/id/1/5000/3333"
        )
    )

    fun getUsers(): List<User> {

        return when (BuildConfig.FLAVOR) {

            "mediaDev" -> mediaDevUsers
            "mediaQa" -> mediaQaUsers
            "previewDev" -> previewDevUsers
            "previewQa" -> previewQaUsers

            else -> emptyList()
        }
    }
}