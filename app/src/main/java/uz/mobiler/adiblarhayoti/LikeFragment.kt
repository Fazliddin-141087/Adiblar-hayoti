package uz.mobiler.adiblarhayoti

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import uz.mobiler.adiblarhayoti.adapters.GenreAdapters
import uz.mobiler.adiblarhayoti.databinding.FragmentLikeBinding
import uz.mobiler.adiblarhayoti.models.Literature

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LikeFragment : Fragment() {
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

    lateinit var binding: FragmentLikeBinding
    lateinit var genreAdapters: GenreAdapters
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var list: ArrayList<Literature>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikeBinding.inflate(layoutInflater)

        firebaseFirestore = FirebaseFirestore.getInstance()
        list = ArrayList()

        firebaseFirestore.collection("adib").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    list.clear()
                    val result = it.result
                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                        if (literature.like == true) {
                            list.add(literature)
                        }
                    }
                    genreAdapters.notifyDataSetChanged()
                } else {
                    binding.shimmerLikeRv.showShimmerAdapter()
                }
            }.addOnFailureListener {
                binding.shimmerLikeRv.showShimmerAdapter()
            }

        genreAdapters = GenreAdapters(list, object : GenreAdapters.MyItemOnClickListener {
            override fun itemOnClick(literature: Literature, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("literature", literature)
                bundle.putInt("position", position)
                findNavController().navigate(R.id.finishFragment, bundle)
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
                    genreAdapters.notifyItemRemoved(position)
                    list.remove(literature)
                }
                genreAdapters.notifyItemChanged(position)
            }
        })

        binding.shimmerLikeRv.adapter = genreAdapters


        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LikeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LikeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}