package uz.mobiler.adiblarhayoti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import uz.mobiler.adiblarhayoti.adapters.SmoothPagerAdapters
import uz.mobiler.adiblarhayoti.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MainFragment : Fragment() {
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


    lateinit var binding: FragmentMainBinding
    lateinit var smoothPagerAdapters: SmoothPagerAdapters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentMainBinding.inflate(layoutInflater)

        smoothPagerAdapters= SmoothPagerAdapters(requireActivity())
        binding.viewPagerMain.adapter=smoothPagerAdapters


        binding.smoothBottomBar.onItemSelected={ position->
            when(position){
                0->{binding.viewPagerMain.currentItem=0}
                1->{binding.viewPagerMain.currentItem=1}
                2->{binding.viewPagerMain.currentItem=2}
            }
        }


        binding.viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0->{binding.smoothBottomBar.itemActiveIndex=0}
                    1->{binding.smoothBottomBar.itemActiveIndex=1}
                    2->{binding.smoothBottomBar.itemActiveIndex=2}
                }
            }
        })


        return binding.root
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}