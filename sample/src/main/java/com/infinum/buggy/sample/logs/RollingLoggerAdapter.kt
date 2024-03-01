package com.infinum.buggy.sample.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinum.buggy.sample.databinding.ItemLogBinding

class RollingLoggerAdapter : ListAdapter<String, RollingLoggerViewHolder>(RollingLoggerDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollingLoggerViewHolder =
        RollingLoggerViewHolder(
            ItemLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(holder: RollingLoggerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RollingLoggerDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem === newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}

class RollingLoggerViewHolder(
    private val binding: ItemLogBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        log: String,
    ) {
        binding.tvLog.text = log
    }
}
