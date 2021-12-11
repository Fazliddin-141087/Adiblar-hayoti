package uz.mobiler.adiblarhayoti.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.mobiler.adiblarhayoti.databinding.ItemGenreBinding
import uz.mobiler.adiblarhayoti.models.Literature

class GenreAdapters(var list: ArrayList<Literature>,var myItemOnClickListener: MyItemOnClickListener) :RecyclerView.Adapter<GenreAdapters.Vh>(){

    inner class Vh(var  itemGenreBinding: ItemGenreBinding) :RecyclerView.ViewHolder(itemGenreBinding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(literature: Literature,position: Int){
            Picasso.get().load(literature.imageUrl).into(itemGenreBinding.img)
            itemGenreBinding.nameTv.text=literature.name
            itemGenreBinding.years.text="(${literature.birthYear} - ${literature.dieYear})"

//            itemGenreBinding.likeBtn.setOnClickListener {
//                myItemOnClickListener.likeOnClick(literature, position)
//            }

            itemGenreBinding.root.setOnClickListener {
                myItemOnClickListener.itemOnClick(literature, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemGenreBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface MyItemOnClickListener{
        fun itemOnClick(literature: Literature,position: Int)
        fun likeOnClick(literature: Literature,position: Int)
    }

}