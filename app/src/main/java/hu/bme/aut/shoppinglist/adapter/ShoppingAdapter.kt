package hu.bme.aut.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.shoppinglist.R
import hu.bme.aut.shoppinglist.data.ShoppingItem

class ShoppingAdapter(private val listener: ShoppingItemClickListener) :
    RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    private val items = mutableListOf<ShoppingItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        // TODO implementation

        val item = items[position]
        holder.nameTextView.text = item.name
//        holder.descriptionTextView.text = "Itt volt a leírás."


        holder.item = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ShoppingItemClickListener {
        fun onItemChanged(item: ShoppingItem)
        fun onItemDelete(item: ShoppingItem)
    }


    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView
        val nameTextView: TextView
        val removeButton: ImageButton

        var item: ShoppingItem? = null

        init {
            iconImageView = itemView.findViewById(R.id.ShoppingItemIconImageView)
            nameTextView = itemView.findViewById(R.id.ShoppingItemNameTextView)
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton)

            removeButton.setOnClickListener{
                listener.onItemDelete(item!!)
            }
        }
    }

    fun removeItem(item: ShoppingItem){
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }
    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<ShoppingItem>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }
}