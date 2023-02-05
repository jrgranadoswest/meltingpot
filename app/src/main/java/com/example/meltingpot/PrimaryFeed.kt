package com.example.meltingpot

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meltingpot.databinding.FragmentPrimaryFeedBinding
import com.example.meltingpot.model.StandardPost
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [PrimaryFeed.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrimaryFeed : Fragment() {
    companion object {
        val TAG: String = "PrimaryFeedFragment"
    }
    private lateinit var db: FirebaseDatabase
    private var lang_setting = "Spanish"
    private var _binding: FragmentPrimaryFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: PostListAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            it.getString("LANG")?.let{ value -> lang_setting = value }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPrimaryFeedBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_primaryFeed_to_createFragment, Bundle().apply {
                putString("LANG", lang_setting)
            })
        }
        // Initialize Realtime Database
        db = Firebase.database
        val messagesRef = db.reference.child("gen_posts").child(lang_setting)
        Log.d(TAG, "Lang setting: " + lang_setting)
        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options = FirebaseRecyclerOptions.Builder<StandardPost>()
            .setQuery(messagesRef, StandardPost::class.java)
            .build()
        adapter = PostListAdapter(options, getUserName())
        manager = LinearLayoutManager(context)
        manager.stackFromEnd = false
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<HashMap<String, StandardPost>>()
                val values: MutableCollection<StandardPost>? = post?.values
                Log.d("ABACABA", "A: " + dataSnapshot.toString())
                Log.d("ABACABA", "A: " + values.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        messagesRef.addValueEventListener(postListener)
        return binding.root
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.displayName
        } else "anonymous"
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
}