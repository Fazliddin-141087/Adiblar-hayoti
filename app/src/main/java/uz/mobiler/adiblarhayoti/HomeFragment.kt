package uz.mobiler.adiblarhayoti

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_tab.view.*
import uz.mobiler.adiblarhayoti.adapters.GenreViewPagerAdapter
import uz.mobiler.adiblarhayoti.databinding.FragmentHomeBinding
import uz.mobiler.adiblarhayoti.utils.NetworkHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

    lateinit var binding: FragmentHomeBinding
    lateinit var genreViewPagerAdapter: GenreViewPagerAdapter
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var list: ArrayList<String>
    lateinit var networkHelper: NetworkHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        networkHelper= NetworkHelper(binding.root.context)

        firebaseFirestore = FirebaseFirestore.getInstance()
        list= ArrayList()

        if (networkHelper.isNetworkConnected()){

            binding.progressBar.visibility=View.GONE

            genreViewPagerAdapter = GenreViewPagerAdapter(requireActivity())
            binding.viewPagerHome.adapter = genreViewPagerAdapter

            TabLayoutMediator(
                binding.tabLayout, binding.viewPagerHome
            ) { tab, position ->

            }.attach()

            setTab()

            binding.searchBtn.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }

        }else{
            binding.progressBar.visibility=View.VISIBLE
        }


        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setTab() {
        for (i in 0 until binding.tabLayout.tabCount) {
            val tabBind =
                LayoutInflater.from(binding.root.context).inflate(R.layout.item_tab, null, false)
            val tab = binding.tabLayout.getTabAt(i)
            tab?.customView = tabBind

            when (i) {
                0 -> tabBind.text_item.text = "Mumtoz Adabiyoti"
                1 -> tabBind.text_item.text = "O'zbek Adabiyoti"
                2 -> tabBind.text_item.text = "Jahon Adabiyoti"
            }

            if (i == 0) {
                tabBind.text_item.setBackgroundResource(R.drawable.itrem_tab_style)
                tabBind.text_item.setTextColor(resources.getColor(R.color.white))
            } else {
                tabBind.text_item.setBackgroundResource(R.drawable.tablayout_style)
                tabBind.text_item.setTextColor(resources.getColor(R.color.text_color))
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val view = tab?.customView
                view?.text_item?.setBackgroundResource(R.drawable.itrem_tab_style)
                view?.text_item?.setTextColor(resources.getColor(R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val view = tab?.customView
                view?.text_item?.setBackgroundResource(R.drawable.tablayout_style)
                view?.text_item?.setTextColor(resources.getColor(R.color.text_color))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}