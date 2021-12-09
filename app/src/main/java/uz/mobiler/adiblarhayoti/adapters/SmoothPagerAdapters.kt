package uz.mobiler.adiblarhayoti.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mobiler.adiblarhayoti.HomeFragment
import uz.mobiler.adiblarhayoti.LikeFragment
import uz.mobiler.adiblarhayoti.SettingsFragment

class SmoothPagerAdapters(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0-> {return HomeFragment()}
            1-> {return LikeFragment()}
            2-> {return SettingsFragment()}
        }
        return HomeFragment()
    }

}