package com.example.android.ui.adapter

import com.example.android.models.User

interface OnListItemClick {
    fun onItemClick(user: User)
}