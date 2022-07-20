package com.example.practicalexamronaldmacwan.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicalexamronaldmacwan.DividerItemDecorator
import com.example.practicalexamronaldmacwan.databinding.FragmentAddCategoryBinding
import com.example.practicalexamronaldmacwan.roomdb.db.PracticalExamDatabase
import com.example.practicalexamronaldmacwan.roomdb.entity.Category
import com.example.practicalexamronaldmacwan.roomdb.repository.CategoryRepository
import com.example.practicalexamronaldmacwan.ui.category.adapter.CategoryRecyclerViewAdapter


class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryViewModel: AddCategoryViewModel
    private lateinit var categoryRecyclerViewAdapter: CategoryRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as AppCompatActivity?)?.supportActionBar?.setHomeAsUpIndicator(com.example.practicalexamronaldmacwan.R.drawable.menu)

        val dao = PracticalExamDatabase.getInstance(requireActivity().application).categoryDAO
        val repository = CategoryRepository(dao)
        val factory = AddCategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(AddCategoryViewModel::class.java)
        binding.categoryViewModel = categoryViewModel
        binding.lifecycleOwner = requireActivity()

        categoryViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { it ->
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
        initRecyclerView()
        return root
    }

    private fun initRecyclerView() {
        val drawable =
            requireActivity().getDrawable(com.example.practicalexamronaldmacwan.R.drawable.category_recycler_view_item_divider)
        val itemDecorator = DividerItemDecorator(drawable!!)
        binding.categoryRecyclerView.addItemDecoration(itemDecorator)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        categoryRecyclerViewAdapter =
            CategoryRecyclerViewAdapter { selectedItem: Category, isDelete: Boolean ->
                listItemClicked(
                    selectedItem,
                    isDelete
                )
            }
        binding.categoryRecyclerView.adapter = categoryRecyclerViewAdapter
        displayCategoryList()
    }

    private fun displayCategoryList() {
        categoryViewModel.getSavedCategories().observe(viewLifecycleOwner) {
            categoryRecyclerViewAdapter.setList(it)
            categoryRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(category: Category, isDelete: Boolean) {
        categoryViewModel.initUpdateAndDelete(category)
        if(isDelete){
            categoryViewModel.deleteCategory(category)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}