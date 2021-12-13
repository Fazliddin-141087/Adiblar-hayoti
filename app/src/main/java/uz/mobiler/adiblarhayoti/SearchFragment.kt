package uz.mobiler.adiblarhayoti

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import uz.mobiler.adiblarhayoti.adapters.GenreAdapters
import uz.mobiler.adiblarhayoti.databinding.FragmentSearchBinding
import uz.mobiler.adiblarhayoti.models.Literature
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SearchFragment : Fragment() {
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


    lateinit var binding: FragmentSearchBinding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var list: ArrayList<Literature>
    lateinit var genreAdapters: GenreAdapters
    private  val TAG = "SearchFragment"


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(layoutInflater)

        firebaseFirestore = FirebaseFirestore.getInstance()
        list= ArrayList()

        firebaseFirestore.collection("adib").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    list.clear()
                    val result = it.result
                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                        list.add(literature)

                    }
                    genreAdapters.notifyDataSetChanged()
                }
            }.addOnFailureListener { error->

            }

        genreAdapters= GenreAdapters(list,object :GenreAdapters.MyItemOnClickListener{
            override fun itemOnClick(literature: Literature, position: Int) {
                val bundle=Bundle()
                bundle.putSerializable("literature",literature)
                bundle.putInt("position",position)
                findNavController().navigate(R.id.finishFragment,bundle)
            }

            override fun likeOnClick(literature: Literature, position: Int,imageButton: ImageButton) {
                if (literature.like==false){
                    literature.like=true
                    firebaseFirestore.collection("adib").document(literature.name!!).update("like",true)
                    imageButton.setImageResource(R.drawable.ic_vectoronlike)
                    imageButton.setBackgroundResource(R.drawable.circle_serch_style2)
                }else {
                    literature.like=false
                    firebaseFirestore.collection("adib").document(literature.name!!).update("like",false)
                    imageButton.setBackgroundResource(R.drawable.circle_serch_style3)
                    imageButton.setImageResource(R.drawable.ic_vectorlike)
                }
                genreAdapters.notifyItemChanged(position)
            }
        })

        binding.shimmerRv.adapter=genreAdapters

        binding.searchEt.requestFocus()
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)


        searchText()

        binding.exitSearch.setOnClickListener {
            if (binding.searchEt.text.isNotEmpty()) {
                binding.searchEt.setText("")
            } else {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }


    private fun searchText() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }


    private fun filter(text: String) {
        val filteredList = ArrayList<Literature>()
        for (item in list) {
            if (item.name!!.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                filteredList.add(item)
            }
        }
       genreAdapters.filterList(filteredList)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}