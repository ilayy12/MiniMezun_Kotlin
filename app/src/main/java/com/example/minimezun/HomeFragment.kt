package com.example.minimezun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var adapter: recycleViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<news>

    lateinit var imgID : Array<Int>
    lateinit var title : Array<String>
    lateinit var newss : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitilaze()
        val layoutManager = LinearLayoutManager(context)
        adapter = recycleViewAdapter(newsArrayList)

        recyclerView = view.findViewById(R.id.recycleHome)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
    private fun dataInitilaze() {
        newsArrayList = arrayListOf()
        imgID = arrayOf(
            R.mipmap.haber1,
            R.mipmap.haber2,
            R.mipmap.haber3,
            R.mipmap.haber4,
            R.mipmap.haber5,
            R.mipmap.haber6,
        )
        title = arrayOf(
            getString(R.string.haber1),
            getString(R.string.haber2),
            getString(R.string.haber3),
            getString(R.string.haber4),
            getString(R.string.haber5),
            getString(R.string.haber6),
        )
        newss = arrayOf(
            getString(R.string.haber1),
            getString(R.string.haber2),
            getString(R.string.haber3),
            getString(R.string.haber4),
            getString(R.string.haber5),
            getString(R.string.haber6),
        )
        for (i in imgID.indices){
            val new  = news(title[i],imgID[i])
            newsArrayList.add(new)
        }
    }
}