package com.example.practicalexamronaldmacwan.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.practicalexamronaldmacwan.R
import com.example.practicalexamronaldmacwan.databinding.CategoryListItemBinding
import com.example.practicalexamronaldmacwan.roomdb.entity.Category

class CategoryRecyclerViewAdapter(private val clickListener: ((Category,isDelete :Boolean) -> Unit)) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val categoryList = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CategoryListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.category_list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoryList[position], clickListener)
    }

    fun setList(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)

    }

}

class MyViewHolder(val binding: CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category, clickListener: (Category, isDelete: Boolean) -> Unit) {
        binding.nameTextView.text = category.name

        binding.ivDelete.setOnClickListener {
            clickListener(category,true)
        }

        binding.ivEdit.setOnClickListener {
            clickListener(category,false)
        }
    }
}