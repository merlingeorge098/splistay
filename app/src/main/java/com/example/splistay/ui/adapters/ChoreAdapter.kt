package com.example.splistay.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.splistay.data.model.Chore
import com.example.splistay.databinding.ItemChoreBinding

class ChoreAdapter(private val onProofClick: (Chore) -> Unit) : ListAdapter<Chore, ChoreAdapter.ChoreViewHolder>(ChoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoreViewHolder {
        val binding = ItemChoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChoreViewHolder(private val binding: ItemChoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chore: Chore) {
            binding.textChoreTitle.text = chore.title
            binding.textDueDate.text = "Today, 5 PM" // Mock formatting
            
            if (chore.status == "completed") {
                binding.layoutVerified.visibility = View.VISIBLE
                binding.btnProof.visibility = View.GONE
            } else {
                binding.layoutVerified.visibility = View.GONE
                binding.btnProof.visibility = View.VISIBLE
                binding.btnProof.setOnClickListener { onProofClick(chore) }
            }
        }
    }

    class ChoreDiffCallback : DiffUtil.ItemCallback<Chore>() {
        override fun areItemsTheSame(oldItem: Chore, newItem: Chore): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chore, newItem: Chore): Boolean = oldItem == newItem
    }
}
