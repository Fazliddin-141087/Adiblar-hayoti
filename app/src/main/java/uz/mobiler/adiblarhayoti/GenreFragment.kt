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
import uz.mobiler.adiblarhayoti.databinding.FragmentGenreBinding
import uz.mobiler.adiblarhayoti.models.Literature
import uz.mobiler.adiblarhayoti.utils.NetworkHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [GenreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class GenreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    lateinit var binding: FragmentGenreBinding
    lateinit var genreAdapters: GenreAdapters
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var list: ArrayList<Literature>
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGenreBinding.inflate(layoutInflater)

        firebaseFirestore= FirebaseFirestore.getInstance()
        list= ArrayList()

            firebaseFirestore.collection("adib").get()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        list.clear()
                        val result = it.result
                        result?.forEach { queryDocumentSnapshot ->
                            val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                            when(param1){
                                0->{
                                    if (literature.type!!.equals("Mumtoz",true)){
                                        list.add(literature)
                                    }
                                }
                                1->{
                                    if (literature.type!!.equals("O'zbek",true)){
                                        list.add(literature)
                                    }
                                }
                                2->{
                                    if (literature.type.equals("Jahon",true)){
                                        list.add(literature)
                                    }
                                }
                            }
                        }
                        genreAdapters.notifyDataSetChanged()
                    }
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
                        genreAdapters.notifyItemChanged(position)
                    }
                genreAdapters.notifyItemRangeChanged(position,list.size)
            }
        })

        binding.shimmerRv.adapter=genreAdapters


        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        genreAdapters.notifyDataSetChanged()
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            GenreFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}