package com.example.android.ui.adapter

import com.example.android.model.entity.User

interface OnListItemClick {
    fun onItemClick(user: User)
}