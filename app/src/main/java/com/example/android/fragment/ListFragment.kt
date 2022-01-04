package com.example.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android.R
import com.example.android.databinding.FragmentListBinding
import com.example.android.model.User
import com.example.android.ui.adapter.HideKeyboard
import com.example.android.ui.adapter.OnListItemClick
import com.example.android.ui.adapter.UserRecyclerView

class ListFragment : Fragment(), OnListItemClick {

    private lateinit var binding: FragmentListBinding
    private var userList: ArrayList<User> = ArrayList()
    private lateinit var name: String
    private val userRecyclerView: UserRecyclerView by lazy {
        UserRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater)
        binding.root

        binding.addBtn.setOnClickListener {
            userList.add(
                User(
                    name,
                    binding.edtTextMessage.text.toString(),
                    R.drawable.programmer
                )
            )
            userRecyclerView.setList(userList)
            binding.edtTextMessage.setText("")

            /** hide Keyboard when button Add click **/
            val hideClass = HideKeyboard()
            hideClass.hide(requireActivity(),requireView())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        name = arguments?.getString("name").toString()
        binding.recyclerView.adapter = userRecyclerView

        userRecyclerView.onListItemClick = this

    }

    override fun onItemClick(user: User) {
        Toast.makeText(context, "Message is: ${user.message}", Toast.LENGTH_SHORT).show()
    }
}
