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

class PostListAdapter(private val options: FirebaseRecyclerOptions<StandardPost>,
                      private val currentUserName: String?
                      ): FirebaseRecyclerAdapter<StandardPost, RecyclerView.ViewHolder>(options) {
//    private var localPostData = emptyList<StandardPost>()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(lpdata: List<StandardPost>) {
//        this.localPostData = lpdata
//        Log.i("setData", "datalen: ${lpdata.size}")
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.postcard, parent, false)
        val binding = PostcardBinding.bind(view)
        return PostViewHolder(binding)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
//        val v = LayoutInflater.from(parent.context)
//            .inflate(R.layout.postcard, parent, false)
//        return PostViewHolder(v)
//    }

    inner class PostViewHolder(private val binding: PostcardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var show_translated: Boolean = false
//        init {
//            binding.setOnClickListener(this)
//        }
//        override fun onClick(view: View?) {
//            // Enter map screen
//            Log.d("ListAdapter", "clicked item")
//        }
        fun showingTr(): Boolean {
            return show_translated
        }
        fun toggleTr() {
            show_translated = !show_translated
        }
        fun bind(item: StandardPost) {
            binding.postContent.text = item.content
            binding.translateBtn.setOnClickListener {
                toggleTr()
                if(showingTr()) {
                    binding.translatedTv.visibility = View.VISIBLE
                } else {
                    binding.translatedTv.visibility = View.GONE
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        model: StandardPost
    ) {
        (holder as PostViewHolder).bind(model)
    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder /*PostViewHolder*/, position: Int, model: StandardPost) {
//        val post_tv: TextView = holder.postView.findViewById(R.id.post_content)
//        val translated_tv: TextView = holder.postView.findViewById(R.id.translated_tv)
//        val translationButton: Button = holder.postView.findViewById<Button>(R.id.translate_btn)
//        translationButton.setOnClickListener {
//            holder.toggleTr()
//            if(holder.showingTr()) {
//                translated_tv.visibility = View.VISIBLE
//            } else {
//                translated_tv.visibility = View.GONE
//            }
//
//        }
//    }

//    override fun getItemCount(): Int {
//        return localPostData.size
//    }

}