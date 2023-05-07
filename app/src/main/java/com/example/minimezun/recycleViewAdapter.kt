package com.example.minimezun

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//haber gösterimi için home fragmentte kullanılan recycleview yapısının adapterı
class recycleViewAdapter(private val newslist: ArrayList<news>) : RecyclerView.Adapter<recycleViewAdapter.recycleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recycleViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return recycleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: recycleViewHolder, position: Int) {
        val currentItem = newslist[position]
        holder.title.text = currentItem.title
        holder.img.setImageResource(currentItem.img)
    }

    override fun getItemCount(): Int {
        return newslist.size
    }

    class recycleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.header)
        val img : ImageView = itemView.findViewById(R.id.item_img)
    }
}