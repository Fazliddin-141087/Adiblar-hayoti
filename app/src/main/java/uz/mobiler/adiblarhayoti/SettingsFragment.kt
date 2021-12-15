package uz.mobiler.adiblarhayoti

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.mobiler.adiblarhayoti.databinding.FragmentSettingsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
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

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentSettingsBinding.inflate(layoutInflater)


        binding.addCard.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }


        binding.shareCard.setOnClickListener {
            var intent=Intent(Intent.ACTION_SEND)
            var shareBody="Adiblar hayoti va ijodi\n"+"https://play.google.com/store/apps/details?id=uz.mobiler.adiblar"
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,shareBody)
            startActivity(Intent.createChooser(intent,"Ulashish"))
        }


        binding.infoCard.setOnClickListener {
            var materialAlertDialogBuilder=MaterialAlertDialogBuilder(binding.root.context)
            val dialog = materialAlertDialogBuilder.create()
           materialAlertDialogBuilder.setTitle("Biz haqimizda!!!")
            materialAlertDialogBuilder.setMessage("\nVersion: 1.0.0\n\nCompany: Mobiler\n\nMail: uzmobiler@gmail.com\n\nPhone: +998 97 007 96 20")
            materialAlertDialogBuilder.setPositiveButton("Ok",object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    dialog.cancel()
                }
            })
            materialAlertDialogBuilder.show()
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}