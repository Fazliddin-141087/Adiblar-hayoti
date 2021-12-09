package uz.mobiler.adiblarhayoti

import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
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
    private  var imgUrl:String?=null
    lateinit var typeList:ArrayList<String>
    lateinit var list: ArrayList<Literature>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddBinding.inflate(layoutInflater)

        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseStorage= FirebaseStorage.getInstance()
        reference=firebaseStorage.getReference("images/")


        typeList= ArrayList()
        typeList.add("Mumtoz")
        typeList.add("O'zbek")
        typeList.add("Jahon")

        ArrayAdapter(
            binding.root.context,
            R.layout.simple_list_item_1,
            typeList
        ).also { adapter ->
            binding.ganre.setAdapter(adapter)
        }


        binding.addImageBtn.setOnClickListener {
            askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) {



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




    Snackbar.make(binding.root,"ooo",Snackbar.LENGTH_LONG).show()

        binding.addBtn.setOnClickListener {

        }


        return binding.root
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