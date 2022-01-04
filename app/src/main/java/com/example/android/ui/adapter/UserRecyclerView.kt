package com.example.android.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.model.User

class UserRecyclerView : RecyclerView.Adapter<UserRecyclerView.UserViewHolder>() {

    var onListItemClick:OnListItemClick? = null
    var userList: ArrayList<User> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(userList: ArrayList<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    //ViewHolder
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progImage: ImageView = itemView.findViewById(R.id.programmer)
        var name: TextView = itemView.findViewById(R.id.name)
        var message: TextView = itemView.findViewById(R.id.message)

        fun bind(user: User) {
            progImage.setImageResource(user.imageId)
            name.text = user.name
            message.text = user.message

            itemView.setOnClickListener {
                onListItemClick?.onItemClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}