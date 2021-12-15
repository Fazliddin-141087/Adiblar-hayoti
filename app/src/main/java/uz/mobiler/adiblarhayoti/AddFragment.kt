package uz.mobiler.adiblarhayoti

import android.Manifest
import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.mobiler.adiblarhayoti.databinding.FragmentAddBinding
import uz.mobiler.adiblarhayoti.models.Literature

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentAddBinding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    private var imgUrl: String? = null
    lateinit var typeList: ArrayList<String>
    lateinit var list: ArrayList<Literature>
    private  val TAG = "AddFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images/")

        list = ArrayList()

        typeList = ArrayList()
        typeList.add("Mumtoz")
        typeList.add("O'zbek")
        typeList.add("Jahon")

        ArrayAdapter(binding.root.context, R.layout.simple_list_item_1, typeList).also { adapter ->
            binding.ganre.setAdapter(adapter)
        }


        binding.addImageBtn.setOnClickListener {
            askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {

                getImageContent.launch("image/*")

            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(requireContext())
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain()
                        }
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {

                    e.goToSettings();
                }
            }
        }

        var isHas = false

        Log.d(TAG, "onCreateView: $imgUrl")

        binding.addBtn.setOnClickListener {
            val name = binding.addName.text.toString().trim()
            val birth = binding.addBirthdayYears.text.toString().trim()
            val die = binding.addDiedYears.text.toString().trim()
            val genre = binding.ganre.text.toString().trim()
            val desc = binding.addDescriptions.text.toString().trim()

            if (imgUrl != null) {
                if (name.isNotEmpty() && birth.isNotEmpty() && die.isNotEmpty() && genre.isNotEmpty() && desc.isNotEmpty()) {

                    for (literature in list) {
                        if (literature.name == name) {
                            isHas = true
                        }
                    }

                    if (!isHas) {
                        var literature = Literature(name, birth, die, genre, desc, imgUrl, false)
                        firebaseFirestore.collection("adib").document(name).set(literature)
                            .addOnSuccessListener {
                                findNavController().popBackStack()
                                Snackbar.make(binding.root, "Saqlandi!!!", Snackbar.LENGTH_LONG).show()
                            }.addOnFailureListener { e ->
                                Snackbar.make(binding.root, "Xatolik::  ${e.message}", Snackbar.LENGTH_LONG).show()
                            }
                    } else {
                        Snackbar.make(binding.root, "Bunday ism mavjud", Snackbar.LENGTH_LONG).show()
                        isHas=false
                    }

                } else {
                    Snackbar.make(binding.root, "Iltimos bo'sh maydonlarni to'ldiring?", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(binding.root, "Iltimos rasim qo'shing!!!", Snackbar.LENGTH_LONG).show()
            }
        }

        firebaseFirestore.collection("adib").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                        list.add(literature)
                    }
                }
            }.addOnFailureListener { e ->
                Log.d(TAG, "onCreateView: ${e.message}")
            }


        return binding.root
    }


    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.addImage.setImageURI(uri)
                var millis = System.currentTimeMillis()
                val uploadTask = reference.child(millis.toString()).putFile(uri)
                uploadTask.addOnSuccessListener {
                    if (it.task.isSuccessful) {
                        val downloadUrl = it.metadata?.reference?.downloadUrl
                        downloadUrl?.addOnSuccessListener { img ->
                            imgUrl = img.toString()
                        }
                    }
                }.addOnFailureListener { error ->
                    Log.d(TAG, "failure: ${error.message} ")
                }
            }
        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}