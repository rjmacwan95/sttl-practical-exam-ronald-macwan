package com.example.practicalexamronaldmacwan.ui.contact.contactslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.practicalexamronaldmacwan.R
import com.example.practicalexamronaldmacwan.databinding.CategoryFilterListItemBinding
import com.example.practicalexamronaldmacwan.roomdb.entity.Category

class CategoryFilterRecyclerViewAdapter(private val clickListener: ((Category) -> Unit)) :
    RecyclerView.Adapter<MyNewViewHolder>() {

    private val categoryList = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CategoryFilterListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.category_filter_list_item, parent, false)
        return MyNewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyNewViewHolder, position: Int) {
        holder.bind(categoryList[position], clickListener)
    }

    fun setList(contacts: List<Category>) {
        categoryList.clear()
        categoryList.addAll(contacts)

    }

}

class MyNewViewHolder(val binding: CategoryFilterListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category, clickListener: (Category) -> Unit) {
        binding.nameTextView.text = category.name

        binding.listItemLayout.setOnClickListener {
            clickListener(category)
        }
    }
}