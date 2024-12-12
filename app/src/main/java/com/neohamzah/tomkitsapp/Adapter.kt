package com.neohamzah.tomkitsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.neohamzah.tomkitsapp.data.remote.response.HistoriesItem
import com.neohamzah.tomkitsapp.databinding.ItemHistoryBinding
import com.neohamzah.tomkitsapp.ui.detailHistory.DetailHistoryActivity
import com.neohamzah.tomkitsapp.ui.detailHistory.DetailHistoryActivity.Companion.EXTRA_ID

class Adapter : ListAdapter<HistoriesItem, Adapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class MyViewHolder(
        private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoriesItem) {
            binding.textViewName.text = history.diseaseName
            binding.textViewType.text = history.createdAt
            Glide.with(itemView.context)
                .load(history.imageLink)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                intent.putExtra(EXTRA_ID, history.id)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoriesItem>() {
            override fun areItemsTheSame(oldItem: HistoriesItem, newItem: HistoriesItem): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: HistoriesItem, newItem: HistoriesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}