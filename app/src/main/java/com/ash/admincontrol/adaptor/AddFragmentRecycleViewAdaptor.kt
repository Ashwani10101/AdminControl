package com.ash.admincontrol.adaptor

import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ash.admincontrol.R
import com.ash.admincontrol.interfaces.AddFragmentRecycleViewInterface
import com.ash.admincontrol.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class AddFragmentRecycleViewAdaptor(val addFragmentRecycleViewInterface: AddFragmentRecycleViewInterface) : RecyclerView.Adapter<AddFragmentRecycleViewAdaptor.MyViewHolder>()//, Filterable
{
    private var permanentList = ArrayList<Product>()
    private var filteringList = ArrayList<Product>()  // Used for filtering in search bar


    fun addProduct(product : Product)
    {
        filteringList.add(product)
        permanentList.add(product)

        CoroutineScope(Dispatchers.Main).launch {
            notifyItemInserted(itemCount)//itemCount
        }
    }

    fun clear()
    {
        filteringList.clear()
        permanentList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            notifyDataSetChanged()
        }
    }

    fun removeProduct(pos : Int) {
        filteringList.removeAt(pos)
        permanentList.removeAt(pos)

        CoroutineScope(Dispatchers.Main).launch {
            notifyItemRemoved(pos)//itemCount
        }

    }

    fun removeProduct(product: Product)
    {
        var removedFrom:Int? = null
        for(i in 0..filteringList.size)
        {
            val p = filteringList[i]
            if(p.key == product.key)
            {
                filteringList.removeAt(i)
                permanentList.removeAt(i)
                removedFrom = i
                break
            }
        }

        if(removedFrom!=null)
        {
            CoroutineScope(Dispatchers.Main).launch {
                notifyItemRemoved(removedFrom)
            }

        } else
        {
            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
            }
        }

    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_fragment_add, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        return holder.bindData(filteringList[position])
    }

    override fun getItemCount(): Int {
        return filteringList.size
    }

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view),View.OnCreateContextMenuListener
    {
        private val productImage: ImageView = view.findViewById<ImageView>(R.id.imageView_ProductImage)
        private val productName: TextView = view.findViewById<TextView>(R.id.textView_ProductName)
        private val productDetails: TextView = view.findViewById<TextView>(R.id.textView_ProductDetails)
        private val productCategory: TextView = view.findViewById<TextView>(R.id.textView_ProductCategory)
        private val productPrice: TextView = view.findViewById<TextView>(R.id.textView_ProductPrice)
        private val cardProduct: CardView = view.findViewById<CardView>(R.id.cardProduct)
      //  private val progresBar= view.findViewById<ProgressBar>(R.id.progressBar_ProductImage)
        private lateinit var myProduct: Product
        fun bindData(product: Product)
        {
            myProduct = product
            productName.text = product.name
            productDetails.text = product.details
            productPrice.text = product.price.toString()
            productCategory.text = product.category

            cardProduct.setOnClickListener {
                addFragmentRecycleViewInterface.onCardClick(myProduct)
            }
            view.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
        {
            // val editMenuItem: MenuItem = menu!!.add(Menu.NONE, v!!.id, 2, "Edit")
            val deleteMenuItem: MenuItem = menu!!.add(Menu.NONE, v!!.id, 3, "Delete")
            // editMenuItem.setOnMenuItemClickListener(onEditMyActionClickListener)
            deleteMenuItem.setOnMenuItemClickListener(onDeleteMyActionClickListener)

        }



        private val onEditMyActionClickListener: MenuItem.OnMenuItemClickListener =
                MenuItem.OnMenuItemClickListener {
                    addFragmentRecycleViewInterface.onEditCardClick(myProduct)
                    true
                }

        private val onDeleteMyActionClickListener: MenuItem.OnMenuItemClickListener =
                MenuItem.OnMenuItemClickListener {

                    addFragmentRecycleViewInterface.onDeleteCardClick(myProduct)
                    true
                }



    }


    private fun show(message: String)
    {
        Log.i("###", message)
    }

    fun filter(searchTxt:String?, categoryTxt:String?)
    {
        val newList = ArrayList<Product>()

        if(searchTxt!=null && categoryTxt!=null )
        {
            for(p in permanentList)
            {
                if(p.name.toLowerCase(Locale.ROOT).trim().contains(searchTxt.toLowerCase(Locale.ROOT).trim()) &&
                        p.category.toLowerCase(Locale.ROOT).trim() == categoryTxt.toLowerCase(Locale.ROOT).trim())
                {
                    newList.add(p)
                }
            }
        } else if(searchTxt==null && categoryTxt==null )
        {
            newList.clear()
            newList.addAll(permanentList)

        } else if(searchTxt!=null && categoryTxt==null )
        {
            for(p in permanentList)
            {
                if(p.name.toLowerCase(Locale.ROOT).trim().contains(searchTxt.toLowerCase(Locale.ROOT).trim()))
                {
                    newList.add(p)
                }
            }
        }else if(searchTxt==null && categoryTxt!=null )
        {
            for(p in permanentList)
            {
                if(p.category.toLowerCase(Locale.ROOT).trim() == categoryTxt.toLowerCase(Locale.ROOT).trim())
                {
                    newList.add(p)
                }
            }
        }

        DiffUtil.calculateDiff(ProductCategoryDiffCallback(newList,filteringList), false).dispatchUpdatesTo(this)
        filteringList = newList

    }




}

//  Return the list of instruction to covert a recycle view list to something else
class ProductCategoryDiffCallback(private val newItem : List<Product>, private val oldItem : List<Product>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].key  == newItem[newItemPosition].key
    }

    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
        // return oldRow == newRow
    }
}


