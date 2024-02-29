package com.example.oechAppFinal.model

data class Chat(
    val profileImage: Int,
    val name: String,
    val lastMessage: String,
    val unreadCount: Int
)