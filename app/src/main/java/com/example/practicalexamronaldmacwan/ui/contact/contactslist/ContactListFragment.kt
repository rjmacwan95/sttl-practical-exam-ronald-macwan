package com.example.practicalexamronaldmacwan.ui.contact.contactslist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicalexamronaldmacwan.DividerItemDecorator
import com.example.practicalexamronaldmacwan.R
import com.example.practicalexamronaldmacwan.databinding.BottomSheetDialogBinding
import com.example.practicalexamronaldmacwan.databinding.FragmentContactListBinding
import com.example.practicalexamronaldmacwan.roomdb.db.PracticalExamDatabase
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import com.example.practicalexamronaldmacwan.roomdb.entity.Contact
import com.example.practicalexamronaldmacwan.roomdb.repository.CategoryRepository
import com.example.practicalexamronaldmacwan.roomdb.repository.ContactRepository
import com.example.practicalexamronaldmacwan.ui.category.AddCategoryViewModel
import com.example.practicalexamronaldmacwan.ui.category.AddCategoryViewModelFactory
import com.example.practicalexamronaldmacwan.ui.contact.ContactViewModel
import com.example.practicalexamronaldmacwan.ui.contact.ContactViewModelFactory
import com.example.practicalexamronaldmacwan.ui.contact.contactslist.adapter.CategoryFilterRecyclerViewAdapter
import com.example.practicalexamronaldmacwan.ui.contact.contactslist.adapter.ContactRecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ContactListFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var categoryViewModel: AddCategoryViewModel
    private lateinit var contactRecyclerViewAdapter: ContactRecyclerViewAdapter
    private lateinit var categoryFilterRecyclerViewAdapter: CategoryFilterRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (activity as AppCompatActivity?)?.supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)

        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val daoContact = PracticalExamDatabase.getInstance(requireActivity().application).contactDAO
        val repositoryContact = ContactRepository(daoContact)
        val factoryContact = ContactViewModelFactory(repositoryContact)
        contactViewModel =
            ViewModelProvider(requireActivity(), factoryContact).get(ContactViewModel::class.java)
        binding.contactViewModel = contactViewModel

        val dao = PracticalExamDatabase.getInstance(requireActivity().application).categoryDAO
        val repository = CategoryRepository(dao)
        val factory = AddCategoryViewModelFactory(repository)

        categoryViewModel =
            ViewModelProvider(requireActivity(), factory).get(AddCategoryViewModel::class.java)
        binding.contactViewModel = contactViewModel
        binding.lifecycleOwner = requireActivity()

        contactViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { it ->
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
        initRecyclerView()
        return root
    }

    private fun initRecyclerView() {
        val drawable =
            requireActivity().getDrawable(R.drawable.contact_recycler_view_item_divider)
        val itemDecorator = DividerItemDecorator(drawable!!)
        binding.contactsRecyclerView.addItemDecoration(itemDecorator)
        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        contactRecyclerViewAdapter =
            ContactRecyclerViewAdapter { selectedItem: Contact, isDelete: Boolean ->
                listItemClicked(
                    selectedItem,
                    isDelete
                )
            }
        binding.contactsRecyclerView.adapter = contactRecyclerViewAdapter
        displayContactsList()
    }

    private fun displayContactsList() {
        contactViewModel.getSavedContacts().observe(viewLifecycleOwner) {
            contactRecyclerViewAdapter.setList(it)
            contactRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(contact: Contact, isDelete: Boolean) {
        contactViewModel.initUpdateAndDelete(contact)
        if (isDelete) {
            contactViewModel.deleteContact(contact)
        } else {
            findNavController().navigate(R.id.action_nav_contact_list_to_nav_add_contact)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        contactViewModel.searchContacts(searchQuery).observe(this) { list ->
            list.let {
                contactRecyclerViewAdapter.setList(it)
                contactRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_filter){
            categoryViewModel.getSavedCategories().observe(viewLifecycleOwner) {
                if(it.isNotEmpty()){
                    showCategoryListBottomSheet()
                } else {
                    Toast.makeText(requireActivity(),"Filter not available",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }

    private fun showCategoryListBottomSheet() {
        val dialog = BottomSheetDialog(requireActivity())
        val binding: BottomSheetDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.bottom_sheet_dialog, null, false)
        val root: View = binding.root

        binding.clearFilterText.setOnClickListener {
            dialog.dismiss()
            contactViewModel.getSavedContacts().observe(this) { list ->
                list.let {
                    contactRecyclerViewAdapter.setList(it)
                    contactRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.categoryRecyclerView.adapter
        val drawable =
            requireActivity().getDrawable(R.drawable.contact_recycler_view_item_divider)
        val itemDecorator = DividerItemDecorator(drawable!!)
        binding.categoryRecyclerView.addItemDecoration(itemDecorator)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        categoryFilterRecyclerViewAdapter =
            CategoryFilterRecyclerViewAdapter { selectedItem: Category ->
                listCategoryItemClicked(
                    selectedItem,
                    dialog
                )
            }

        binding.categoryRecyclerView.adapter = categoryFilterRecyclerViewAdapter
        displayCategoryList()
        dialog.setContentView(root)
        dialog.show()
    }

    private fun displayCategoryList() {
        categoryViewModel.getSavedCategories().observe(viewLifecycleOwner) {
            categoryFilterRecyclerViewAdapter.setList(it)
            categoryFilterRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun listCategoryItemClicked(category: Category,dialog: BottomSheetDialog?) {
        dialog?.dismiss()
        contactViewModel.filterContacts(category.id).observe(this) { list ->
            list.let {
                contactRecyclerViewAdapter.setList(it)
                contactRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}