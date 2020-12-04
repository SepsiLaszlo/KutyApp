package hu.bme.aut.kutyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.kutyapp.R
import hu.bme.aut.kutyapp.data.DogItem
import hu.bme.aut.kutyapp.dogBreed

class DogAdapter(private val listener: DogItemClickListener) :
    RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    private val items = mutableListOf<DogItem>()
    private var context:Context?=null;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        context = parent.context;
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_dog_list, parent, false)
        return DogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = dogBreed(item.url)
        holder.item = item

        context?.let { Glide.with(it).load(item.url).into(holder.iconImageView) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface DogItemClickListener {
        fun onItemChanged(item: DogItem)
        fun onItemDelete(item: DogItem)
        fun onItemSelected(item: DogItem?)
    }


    inner class DogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView
        val nameTextView: TextView
        val removeButton: ImageButton

        var item: DogItem? = null

        init {
            iconImageView = itemView.findViewById(R.id.DogIconImageView)
            nameTextView = itemView.findViewById(R.id.DogItemNameTextView)
            removeButton = itemView.findViewById(R.id.DogRemoveButton)

            itemView.setOnClickListener{
              listener.onItemSelected(item);
            }
            removeButton.setOnClickListener{
                listener.onItemDelete(item!!)
            }
        }
    }

    fun removeItem(item: DogItem){
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }
    fun addItem(item: DogItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(dogItems: List<DogItem>) {
        items.clear()
        items.addAll(dogItems)
        notifyDataSetChanged()
    }
}