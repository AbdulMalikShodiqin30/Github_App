package com.example.mainactivity.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mainactivity.data.remote.response.ResponseFollow
import com.example.mainactivity.databinding.ItemUserBinding

class AdapterFollow(private val listUser: List <ResponseFollow>) : RecyclerView.Adapter<AdapterFollow.ViewHolder>() {
    class ViewHolder(private var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: ResponseFollow) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUser(listUser[position])
    }
}