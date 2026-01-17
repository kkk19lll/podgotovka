package com.example.podgotovka.models

data class CatalogItem(
    val id: Int,
    val title: String,
    val category: String,
    val price: String,
    val isAdded: Boolean = false
)