package com.example.practicalexamronaldmacwan.ui.contact.addcontact

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.practicalexamronaldmacwan.ImageBitmapString
import com.example.practicalexamronaldmacwan.R
import com.example.practicalexamronaldmacwan.databinding.FragmentAddContactBinding
import com.example.practicalexamronaldmacwan.roomdb.db.PracticalExamDatabase
import com.example.practicalexamronaldmacwan.roomdb.repository.CategoryRepository
import com.example.practicalexamronaldmacwan.roomdb.repository.ContactRepository
import com.example.practicalexamronaldmacwan.ui.category.AddCategoryViewModel
import com.example.practicalexamronaldmacwan.ui.category.AddCategoryViewModelFactory
import com.example.practicalexamronaldmacwan.ui.contact.ContactViewModel
import com.example.practicalexamronaldmacwan.ui.contact.ContactViewModelFactory
import java.io.FileNotFoundException
import java.io.InputStream


class AddContactFragment : Fragment() {

    private var _binding: FragmentAddContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var categoryViewModel: AddCategoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dao = PracticalExamDatabase.getInstance(requireActivity().application).contactDAO
        val repository = ContactRepository(dao)
        val factory = ContactViewModelFactory(repository)
        contactViewModel =
            ViewModelProvider(requireActivity(), factory).get(ContactViewModel::class.java)
        binding.contactViewModel = contactViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as AppCompatActivity?)?.supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        Handler(Looper.getMainLooper()).postDelayed({
            (activity as AppCompatActivity?)?.supportActionBar?.title =
                contactViewModel.actionBarTitle.value
        }, 50)


        contactViewModel.message.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { it ->
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                if (it.contains("Contact Inserted Successfully") || it.contains("Row Updated Successfully")) {
                    findNavController().navigate(R.id.action_nav_add_contact_to_nav_contact_list)
                }
            }
        }

        initSpinner()
        initImage()
        return root
    }

    private fun initImage() {

        if (contactViewModel.image.value != null && contactViewModel.image.value != "") {
            try {
                setBitmapImage(ImageBitmapString.stringToBitMap(contactViewModel.image.value))

            } catch (e: Exception) {
                binding.profileImage.setImageResource(R.drawable.profile)
            }
        } else {
            binding.profileImage.setImageResource(R.drawable.profile)
        }

        var pickImageRequestResultLauncher: ActivityResultLauncher<Intent?>? = null

        binding.profileImage.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100
                )
                return@OnClickListener
            }

            pickImageRequestResultLauncher!!.launch(
                Intent(
                    Intent.ACTION_GET_CONTENT,
                ).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false).setType("image/*")
            )
        })

        pickImageRequestResultLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val clipData = result.data!!.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val imageUri: Uri = clipData.getItemAt(i).uri
                            try {
                                val inputStream: InputStream =
                                    requireActivity().contentResolver.openInputStream(imageUri)!!
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                val imageSource: String = ImageBitmapString.bitMapToString(bitmap)!!
                                contactViewModel.image.value = imageSource
                                setBitmapImage(bitmap)
                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        val imageUri: Uri? = result.data!!.data
                        try {
                            val inputStream: InputStream =
                                requireActivity().contentResolver.openInputStream(
                                    imageUri!!
                                )!!
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val imageSource: String = ImageBitmapString.bitMapToString(bitmap)!!
                            contactViewModel.image.value = imageSource
                            setBitmapImage(bitmap)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    }

    private fun setBitmapImage(bitmap: Bitmap?) {
        binding.profileImage.setImageBitmap(bitmap)
    }


    private fun initSpinner() {
        val dao = PracticalExamDatabase.getInstance(requireActivity().application).categoryDAO
        val repository = CategoryRepository(dao)
        val factory = AddCategoryViewModelFactory(repository)
        categoryViewModel = ViewModelProvider(this, factory).get(AddCategoryViewModel::class.java)
        binding.categoryViewModel = categoryViewModel
        binding.lifecycleOwner = requireActivity()

        val categoryName: ArrayList<String> = ArrayList()
        val categoryId: ArrayList<Int> = ArrayList()

        categoryViewModel.getSavedCategories().observe(viewLifecycleOwner) { spinnerData ->
            for (category in spinnerData) {
                categoryName.add(category.name)
                categoryId.add(category.id)
            }
            val spinnerAdapter =
                ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, categoryName)
            binding.spinnerCategory.adapter = spinnerAdapter
            binding.spinnerCategory.setSelection(categoryName.indexOf(contactViewModel.inputCategoryName.value))
            Log.d("CategoryName", contactViewModel.inputCategoryName.value.toString())


            if (categoryName.size <= 0 && categoryId.size <= 0
            ) {
                contactViewModel.initCategory(0, "")
            }
        }


        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val catId = categoryId[position]
                    val catName = categoryName[position]
                    contactViewModel.initCategory(catId, catName)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contactViewModel.resetContact()
        _binding = null
    }

}