package com.example.android.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.android.R
import com.example.android.models.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var onListItemClick: OnListItemClick? = null
    private var userList: List<User> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    //ViewHolder
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var progImage: ImageView = itemView.findViewById(R.id.programmer)
        private var task: TextView = itemView.findViewById(R.id.task)
        private var message: TextView = itemView.findViewById(R.id.message)

        fun bind(user: User) {
            progImage.setImageResource(user.imageId)
            task.text = user.task
            message.text = user.message

            task.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    user.task,
                    Toast.LENGTH_SHORT
                ).show()
            }
            message.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    user.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

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