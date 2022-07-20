package com.example.practicalexamronaldmacwan.ui.contact.contactslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.practicalexamronaldmacwan.ImageBitmapString
import com.example.practicalexamronaldmacwan.R
import com.example.practicalexamronaldmacwan.databinding.ContactListItemBinding
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact

class ContactRecyclerViewAdapter(private val clickListener: ((Contact, isDelete: Boolean) -> Unit)) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val contactList = ArrayList<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ContactListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.contact_list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contactList[position], clickListener)
    }

    fun setList(contacts: List<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)

    }

}

class MyViewHolder(val binding: ContactListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(contact: Contact, clickListener: (Contact, isDelete: Boolean) -> Unit) {
        binding.nameTextView.text = contact.firstName

        try {
            binding.profileImage.setImageBitmap(ImageBitmapString.stringToBitMap(contact.image))
        } catch (e: Exception) {
            binding.profileImage.setImageResource(R.drawable.profile)
        }

        binding.ivDelete.setOnClickListener {
            clickListener(contact, true)
        }

        binding.ivEdit.setOnClickListener {
            clickListener(contact, false)
        }
    }
}