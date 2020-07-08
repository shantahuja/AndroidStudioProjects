package com.example.picturepicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.collections_list_item.view.*

class MyCollectionsAdapter(val clickListener: MyCollectionAdapterClickListener) : RecyclerView.Adapter<MyCollectionsAdapter.MyCollectionsViewHolder>() {


    interface MyCollectionAdapterClickListener {
        fun onItemClicked(index: Int)
    }

    var listOfCollections: List<Collection>

    init {
        listOfCollections = listOf()
    }

    fun updateCollections(list: List<Collection>) {
        listOfCollections = list
        notifyDataSetChanged()
    }

   inner  class MyCollectionsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Collection, index: Int) = with(itemView) {
            name.text = item.name
            collections_list_item_id.setImageResource(item.listOfPhotos[0])

            setOnClickListener {
                clickListener.onItemClicked(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCollectionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.collections_list_item, parent, false)
        return MyCollectionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfCollections.size
    }

    override fun onBindViewHolder(holder: MyCollectionsViewHolder, position: Int) {
        holder.bind(listOfCollections[position], position)
    }

}