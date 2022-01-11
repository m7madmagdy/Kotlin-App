package com.example.android.ui.fragment

import android.content.SharedPreferences
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
import com.example.android.R
import com.example.android.databinding.FragmentListBinding
import com.example.android.model.entity.User
import com.example.android.model.local.LocalRepositoryImp
import com.example.android.model.local.UserDatabase
import com.example.android.ui.adapter.OnListItemClick
import com.example.android.ui.adapter.UserRecyclerView
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class ListFragment : Fragment(), OnListItemClick {

    private lateinit var binding: FragmentListBinding
    private var userList: List<User> = emptyList()
    private lateinit var name: String
    private lateinit var localRepositoryImp: LocalRepositoryImp

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
        name = arguments?.getString("name").toString()

        val database = UserDatabase.getInstance(requireContext())
        localRepositoryImp = LocalRepositoryImp(database)

        binding.recyclerView.adapter = userRecyclerView

        getAllUsers()
        binding.addBtn.setOnClickListener {
            val msg = binding.edtTextMessage.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                localRepositoryImp.insertUser(
                    User(
                        0,
                        name,
                        msg,
                        R.drawable.programmer
                    )
                )
            }
            getAllUsers()
            binding.edtTextMessage.setText("")
        }
        userRecyclerView.onListItemClick = this
    }

    private fun getAllUsers() {
        GlobalScope.launch(Dispatchers.IO) {
            val returnedUsers = async {
                localRepositoryImp.getUsers()
            }
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
                userList = returnedUsers.await()
                binding.progressBar.visibility = View.GONE
                userRecyclerView.setList(userList)
            }
        }
    }

    private fun refresh() {
        val handler = Handler(Looper.getMainLooper()!!)
        binding.swipe.setColorSchemeResources(R.color.white)
        binding.swipe.setProgressBackgroundColorSchemeResource(R.color.blue200)
        binding.swipe.setOnRefreshListener {
            handler.postDelayed({
                binding.swipe.isRefreshing = false
                getAllUsers()
            }, 1000)
        }
    }

    override fun onItemClick(user: User) {
        val title = "Alert Delete !"
        val message = "Are You sure delete this user ?"
        val positiveButton = "Yes"
        val negativeButton = "No"
        val editButton = "Edit"

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher)
        alertDialogBuilder.setCancelable(true)
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#59A5E1'>$positiveButton</font>")) { _, _ ->
            GlobalScope.launch(Dispatchers.IO) {
                localRepositoryImp.deleteUser(user)
            }
            notify("User is Cleared !")
            getAllUsers()
        }
        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#59A5E1'>$negativeButton</font>")) { _, _ ->
            notify("Click No !")
        }
        alertDialogBuilder.setNeutralButton(Html.fromHtml("<font color='#59A5E1'>$editButton</font>")) { _, _ ->
            GlobalScope.launch (Dispatchers.IO){
                localRepositoryImp.updateUser(user)
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun notify(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
