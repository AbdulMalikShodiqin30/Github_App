package com.example.mainactivity.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mainactivity.data.remote.response.SearchItem
import com.example.mainactivity.databinding.ItemUserBinding

class AdapterUser(private val listUser: List<SearchItem>): RecyclerView.Adapter<AdapterUser.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(private var binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: SearchItem) {
            binding.tvUsername.text = user.login
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivAvatarUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUser(listUser[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: SearchItem)
    }
}