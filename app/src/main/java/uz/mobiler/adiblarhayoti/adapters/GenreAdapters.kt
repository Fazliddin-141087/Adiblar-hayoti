package uz.mobiler.adiblarhayoti.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.mobiler.adiblarhayoti.databinding.ItemGenreBinding
import uz.mobiler.adiblarhayoti.models.Literature

class GenreAdapters(var list: ArrayList<Literature>) :RecyclerView.Adapter<GenreAdapters.Vh>(){

    inner class Vh(var  itemGenreBinding: ItemGenreBinding) :RecyclerView.ViewHolder(itemGenreBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(literature: Literature){
            Picasso.get().load(literature.imageUrl).into(itemGenreBinding.img)
            itemGenreBinding.nameTv.text=literature.name
            itemGenreBinding.years.text="(${literature.birthYear} - ${literature.dieYear})"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemGenreBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyItemOnClickListener{
        
    }

}