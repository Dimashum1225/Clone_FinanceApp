package com.example.clone_financeapp.model

data class Group(
    val title: String,
    val items: List<Item>,
    var isExpanded: Boolean = false
)