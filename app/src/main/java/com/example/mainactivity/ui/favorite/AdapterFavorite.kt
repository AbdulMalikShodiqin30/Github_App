package com.example.mainactivity.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mainactivity.data.local.entity.EntityUser
import com.example.mainactivity.databinding.FavoriteUserBinding

class AdapterFavorite(private val listFavoriteUsers: List<EntityUser>) :
    RecyclerView.Adapter<AdapterFavorite.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(private var binding: FavoriteUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: EntityUser) {
            binding.tvUsernameFavorite.text = user.username
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivAvatarUserFavorite)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavoriteUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavoriteUsers[position])
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EntityUser)
    }
}