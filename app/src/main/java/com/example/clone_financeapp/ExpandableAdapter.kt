package com.example.clone_financeapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clone_financeapp.model.Group
import com.example.clone_financeapp.model.Item


class ExpandableAdapter(private val groups: List<Group>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (isGroup(position)) VIEW_TYPE_GROUP else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_GROUP) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
            GroupViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            ItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isGroup(position)) {
            val group = getGroup(position)
            (holder as GroupViewHolder).bind(group)
            holder.itemView.setOnClickListener {
                group.isExpanded = !group.isExpanded
                notifyDataSetChanged()
            }
        } else {
            val item = getItem(position)
            (holder as ItemViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (group in groups) {
            count++
            if (group.isExpanded) {
                count += group.items.size
            }
        }
        return count
    }

    private fun isGroup(position: Int): Boolean {
        var count = 0
        for (group in groups) {
            if (position == count) {
                return true
            }
            count++
            if (group.isExpanded) {
                count += group.items.size
            }
        }
        return false
    }

    private fun getGroup(position: Int): Group {
        var count = 0
        for (group in groups) {
            if (position == count) {
                return group
            }
            count++
            if (group.isExpanded) {
                count += group.items.size
            }
        }
        throw IllegalStateException("Invalid position")
    }

    private fun getItem(position: Int): Item {
        var count = 0
        for (group in groups) {
            count++
            if (group.isExpanded) {
                for (item in group.items) {
                    if (position == count) {
                        return item
                    }
                    count++
                }
            }
        }
        throw IllegalStateException("Invalid position")
    }

    companion object {
        private const val VIEW_TYPE_GROUP = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            itemView.findViewById<TextView>(R.id.groupTitle).text = group.title
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item) {
            itemView.findViewById<TextView>(R.id.itemName).text = item.name
        }
    }
}
