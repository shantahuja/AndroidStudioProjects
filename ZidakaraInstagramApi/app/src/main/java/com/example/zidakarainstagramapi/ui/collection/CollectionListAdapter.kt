package com.example.zidakarainstagramapi.ui.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.Collection
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.collection_list_item.view.*

class CollectionListAdapter(val collectionSelectedCallback: (Collection) -> Unit) : ListAdapter<Collection, CollectionListAdapter.CollectionViewHolder>(CollectionDiffCallback()) {

    class CollectionViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(collection: Collection, collectionSelectedCallback: (Collection) -> Unit) = with(itemView) {
            collection_title.text = collection.name
            collection_count.text = "${collection.videos.size} videos"

            setOnClickListener {
                collectionSelectedCallback(collection)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collection_list_item, parent, false)
        return CollectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position), collectionSelectedCallback)
    }
}


class CollectionDiffCallback : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem.documentId == newItem.documentId
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem == newItem
    }

}