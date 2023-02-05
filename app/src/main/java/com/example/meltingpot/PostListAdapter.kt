package com.example.meltingpot

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meltingpot.databinding.PostcardBinding
import com.example.meltingpot.model.StandardPost
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostListAdapter(private val options: FirebaseRecyclerOptions<StandardPost>,
                      private val currentUserName: String?
                      ): FirebaseRecyclerAdapter<StandardPost, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.postcard, parent, false)
        val binding = PostcardBinding.bind(view)
        return PostViewHolder(binding)
    }

    inner class PostViewHolder(private val binding: PostcardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var show_translated: Boolean = false
        fun showingTr(): Boolean {
            return show_translated
        }
        fun toggleTr() {
            show_translated = !show_translated
        }
        fun bind(item: StandardPost, pos: Int) {
            binding.uppost.setOnClickListener {
                binding.downpost.isEnabled = false
                binding.uppost.isEnabled = false
                val keyId: String? = getRef(pos).key
                val db: FirebaseDatabase = Firebase.database
                if (keyId != null) {
                    db.reference.child("gen_posts").child(item.lang).child(keyId).child("votes")
                        .setValue(item.votes + 1)
                }
            }
            binding.downpost.setOnClickListener {
                binding.downpost.isEnabled = false
                binding.uppost.isEnabled = false
                val keyId: String? = getRef(pos).key
                val db: FirebaseDatabase = Firebase.database
                if (keyId != null) {
                    db.reference.child("gen_posts").child(item.lang).child(keyId).child("votes")
                        .setValue(item.votes - 1)
                }

            }
            binding.votesTv.text = item.votes.toString()
            binding.postContent.text = item.content
            binding.translatedTv.text = item.translated
            binding.translateBtn.setOnClickListener {
                toggleTr()
                if(showingTr()) {
                    binding.translateBtn.text = "HIDE"
                    binding.translatedTv.visibility = View.VISIBLE
                    binding.divideLine.visibility = View.VISIBLE
                } else {
                    binding.translateBtn.text = "TRANSLATE"
                    binding.translatedTv.visibility = View.GONE
                    binding.divideLine.visibility = View.GONE
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: StandardPost
    ) {
        (holder as PostViewHolder).bind(model, position)
    }

}