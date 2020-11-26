package com.ash.admincontrol.adaptor

import android.util.Log
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ash.admincontrol.R
import com.ash.admincontrol.interfaces.AddFragmentRecycleViewInterface
import com.ash.admincontrol.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class AddFragmentRecycleViewAdaptor(val addFragmentRecycleViewInterface: AddFragmentRecycleViewInterface) : RecyclerView.Adapter<AddFragmentRecycleViewAdaptor.MyViewHolder>(), Filterable
{


    private val productList = ArrayList<Product>()
    private var productListAll = ArrayList<Product>()// Used for filtering in search bar

    fun addProductList(productEntityList: ArrayList<Product>)
    {


        productList.addAll(productEntityList)
        productListAll.addAll(productEntityList) //= ArrayList<Product>().apply { addAll(productEntityList) }

        notifyDataSetChanged()
    }

    fun sort()
    {
      // productEntityList.sortWith { lhs: Product, rhs: Product -> lhs.name.toLowerCase().compareTo(rhs.name.toLowerCase()) }
    }

    fun addProduct(product: Product)
    {
        productList.add(product)
        productListAll.add(product)
        CoroutineScope(Dispatchers.Main).launch {
            notifyItemInserted(itemCount)//itemCount
        }

    }

    fun removeProduct(product: Product)
    {
        var removedFrom:Int? = null
        for(i in 0..productListAll.size)
        {
            val p = productListAll[i]
            if(p.key == product.key)
            {
                productListAll.removeAt(i)
                productList.remove(p)
                removedFrom = i
                break
            }
        }
        //trimming
        productList.trimToSize()
        productListAll.trimToSize()

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

    fun clear()
    {
        productList.clear()
        productListAll.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_fragment_add, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        return holder.bindData(productList[position])
    }

    override fun getItemCount(): Int
    {
        return productList.size
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
            /*            if (product.image != null)
            {
                Picasso.get().load(product.image).resize(50, 50).centerCrop().into(productImage)

              //  productImage.setImageBitmap(product.image)// setImageBitmap(product.image)
            } else
            {
                productImage.setImageResource(R.drawable.no_image_selected)
            }*/

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
            //  if(myProfile.title.isNotEmpty()){   menu!!.setHeaderTitle(myProfile.title)} else { menu!!.setHeaderTitle("-> No Title <-")}

           // val editMenuItem: MenuItem = menu!!.add(Menu.NONE, v!!.id, 2, "Edit")
            val deleteMenuItem: MenuItem = menu!!.add(Menu.NONE, v!!.id, 3, "Delete")


          //  editMenuItem.setOnMenuItemClickListener(onEditMyActionClickListener)
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


    override fun getFilter(): Filter
    {
        return filter()
    }


    private fun filter(): Filter
    {
        return object : Filter()
        {
            //Run in background thread
            override fun performFiltering(charSequence: CharSequence?): FilterResults
            {
                val filterList = ArrayList<Product>()


                if (charSequence == null || charSequence.toString().isEmpty() || charSequence.toString() == "")
                {
                    filterList.addAll(productListAll)
                } else
                {
                    for (productEntity in productListAll) {
                        if (productEntity.name.toLowerCase(Locale.ROOT).trim().contains(charSequence.toString().toLowerCase(Locale.ROOT).trim()) )
                        {
                            filterList.add(productEntity)
                        }
                        if (productEntity.details.toLowerCase(Locale.ROOT).trim().contains(charSequence.toString().toLowerCase(Locale.ROOT).trim()))
                        {
                            filterList.add(productEntity)
                        }

                        if (productEntity.category.toLowerCase(Locale.ROOT).trim().contains(charSequence.toString().toLowerCase(Locale.ROOT).trim()))
                        {
                            filterList.add(productEntity)
                        }



                    }
                }

                val filterResult = FilterResults()
                filterResult.values = filterList

                return filterResult
            }

            //Run in UI thread
            override fun publishResults(constraint: CharSequence?, filterResult: FilterResults?)
            {
                productList.clear()
                productList.addAll(filterResult!!.values as Collection<Product>)
                notifyDataSetChanged()
            }
        }
    }


    private fun show(message: String)
    {
        Log.i("###", message)
    }


}