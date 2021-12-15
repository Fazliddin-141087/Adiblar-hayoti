package uz.mobiler.adiblarhayoti

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.athkalia.emphasis.EmphasisTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import uz.mobiler.adiblarhayoti.databinding.FragmentFinishBinding
import uz.mobiler.adiblarhayoti.models.Literature

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FinishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FinishFragment : Fragment() {
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

    lateinit var binding: FragmentFinishBinding
    private var emphasisTextView: EmphasisTextView? = null
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishBinding.inflate(layoutInflater)

        emphasisTextView = EmphasisTextView(binding.root.context)
        firebaseFirestore = FirebaseFirestore.getInstance()

        val literature = arguments?.getSerializable("literature") as Literature

        setHasOptionsMenu(true)

        if (literature != null) {


            binding.text.text = literature.descriptions
            Picasso.get().load(literature.imageUrl).into(binding.image)
            if (literature.like == true) {
                binding.save.setImageResource(R.drawable.ic_vectoronlike)
                binding.save.setBackgroundResource(R.drawable.circle_serch_style2)
            }


            binding.searchEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    binding.text.setTextToHighlight(s.toString())
                    binding.text.setTextHighlightColor(R.color.yellow)
                    binding.text.highlight()
                }
            })

            binding.save.setOnClickListener {
                if (literature.like==false){
                    literature.like=true
                    firebaseFirestore.collection("adib").document(literature.name!!).update("like",true)
                   binding.save.setImageResource(R.drawable.ic_vectoronlike)
                    binding.save.setBackgroundResource(R.drawable.circle_serch_style2)
                }else {
                    literature.like=false
                    firebaseFirestore.collection("adib").document(literature.name!!).update("like",false)
                    binding.save.setBackgroundResource(R.drawable.circle_serch_style3)
                    binding.save.setImageResource(R.drawable.ic_vectorlike)
                }
            }


            binding.navigation.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.name.text = literature.name
            binding.years.text = "(${literature.birthYear} - ${literature.dieYear})"

            binding.search.setOnClickListener {
                binding.navigation.visibility = View.INVISIBLE
                binding.save.visibility = View.INVISIBLE
                binding.search.visibility = View.INVISIBLE

                binding.searchEt.visibility = View.VISIBLE
                binding.cancelSearch.visibility = View.VISIBLE

                binding.searchEt.requestFocus()
                val imm: InputMethodManager? =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
            }

            binding.cancelSearch.setOnClickListener {
                if (binding.searchEt.text.toString().trim().isEmpty()) {
                    binding.navigation.visibility = View.VISIBLE
                    binding.save.visibility = View.VISIBLE
                    binding.search.visibility = View.VISIBLE

                    binding.searchEt.visibility = View.INVISIBLE
                    binding.cancelSearch.visibility = View.INVISIBLE
                } else {
                    binding.searchEt.text = null
                    binding.text.setTextToHighlight("");
                    binding.text.setTextHighlightColor("#FFFFFF")
                    binding.text.highlight()
                }
            }
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
         * @return A new instance of fragment FinishFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinishFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}