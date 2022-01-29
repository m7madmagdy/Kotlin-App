package com.example.android.ui.userlist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.R
import com.example.android.databinding.FragmentListBinding
import com.example.android.model.entity.User
import com.example.android.ui.adapter.OnListItemClick
import com.example.android.ui.adapter.UserRecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class ListFragment : Fragment(), OnListItemClick {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: UsersViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val userRecyclerView: UserRecyclerView by lazy {
        UserRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.layoutManager = GridLayoutManager(activity,1)
        binding.recyclerView.adapter = userRecyclerView

        viewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]

        getAllUsers()
        binding.addBtn.setOnClickListener {
            val task = binding.edtTextTask.text.toString()
            val message = binding.edtTextMessage.text.toString()
            viewModel.insertUser(User(0, task, message, R.drawable.programmer))
            binding.edtTextMessage.setText("")
            binding.edtTextTask.setText("")
            getAllUsers()
        }
        userRecyclerView.onListItemClick = this
        viewModel.usersLiveData.observe(viewLifecycleOwner, {
            if (it != null){
                userRecyclerView.setList(it)
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
    }

    private fun getAllUsers() {
        viewModel.getUsers()
    }

    private fun refresh() {
        val handler = Handler(Looper.getMainLooper()!!)
        binding.swipe.setColorSchemeResources(R.color.blue200)
        binding.swipe.setProgressBackgroundColorSchemeResource(R.color.white)
        binding.swipe.setOnRefreshListener {
            handler.postDelayed({
                binding.swipe.isRefreshing = false
                getAllUsers()
            }, 1000)
        }
    }

    override fun onItemClick(user: User) {
        val title = "Alert Delete !"
        val message = "Are You sure delete this Task ?"
        val positiveButton = "Yes"
        val negativeButton = "No"

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setIcon(R.drawable.ic_launcher_round)
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#59A5E1'>$positiveButton</font>")) { _, _ ->
            viewModel.deleteUser(user)
            notify("${user.message} Deleted")
            getAllUsers()
        }
        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#59A5E1'>$negativeButton</font>")) { _, _ ->
            notify("Click No !")
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun notify(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
