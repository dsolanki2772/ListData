package com.demo.listdata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.listdata.databinding.ItemDataBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private var items = mutableListOf<Item>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(private val binding: ItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.tvIdVal.text = items[position].id.toString()
            binding.tvName.text = items[position].title
            binding.tvSource.text = items[position].body
            itemView.setOnClickListener {
                onItemClick?.invoke(items[position])
            }
        }
    }

    fun setItems(list: List<Item>) {
        this.items.addAll(list.toMutableList())
        notifyDataSetChanged()
    }

    var onItemClick: ((Item) -> Unit)? = null
}