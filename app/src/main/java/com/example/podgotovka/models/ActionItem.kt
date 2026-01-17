package com.example.podgotovka.models

import androidx.annotation.DrawableRes

data class ActionItem(
    val title: String,
    val salePrice: String,
    @DrawableRes val imageResId: Int
)