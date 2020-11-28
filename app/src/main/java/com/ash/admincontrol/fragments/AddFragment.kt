package com.ash.admincontrol.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ash.admincontrol.AddProductActivity
import com.ash.admincontrol.MainActivity
import com.ash.admincontrol.R
import com.ash.admincontrol.adaptor.AddFragmentRecycleViewAdaptor
import com.ash.admincontrol.helper.Snackbar
import com.ash.admincontrol.interfaces.AddFragmentRecycleViewInterface
import com.ash.admincontrol.models.Product
import com.ash.admincontrol.networking.ConnectivityStateHolder
import com.ash.admincontrol.networking.Event
import com.ash.admincontrol.networking.NetworkConnectivityListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFragment : Fragment(), AddFragmentRecycleViewInterface
{
    private val TAG ="AddFragmentActivity"
    private lateinit var recyclerviewAdaptor: AddFragmentRecycleViewAdaptor
    private lateinit var  recycleView :RecyclerView
    private lateinit var  homeLayout: CoordinatorLayout
    private lateinit var  spinner: Spinner
    private lateinit var  searchView: SearchView


    companion object
    {
        var allCate = arrayOf("All", "Atta", "Masale", "Grains", "Pulses", "Rice", "Dry Fruits", "Sweetners", "Satto", "Others")
        private val AddProductCode = 10
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view: View = inflater.inflate(R.layout.fragment_add, container, false)


        recycleView = view.findViewById<RecyclerView>(R.id.addFragment_recycleview)
        homeLayout = view.findViewById(R.id.addFragment_home)

        spinner = view.findViewById<Spinner>(R.id.addFragment_spinner)
        searchView = view.findViewById<SearchView>(R.id.addFragment_Searchbar)
        val textViewTitle = view.findViewById<TextView>(R.id.addFragment_textviewTitle)

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.addFragment_refreshLayout)

        val addBtn = view.findViewById<ImageButton>(R.id.addFragment_AddButton)
        addBtn.setOnClickListener{onClickAdd()}

        initSpinner(spinner)
        initRecycleView(recycleView)
        initSearchView(searchView, textViewTitle)
        initRefreshLayout(refreshLayout)

        Handler().postDelayed({
            refreshLayout.isRefreshing = true
            refreshData(refreshLayout)
        }, 500)

        return view
    }




    private fun onClickAdd()
    {
        val intent = Intent(requireContext(), AddProductActivity::class.java)
        intent.putExtra("CATEGORY_LIST", allCate)

        startActivityForResult(intent, AddProductCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK)
        {
            when(requestCode)
            {
                AddProductCode ->
                {
                    val productName = data!!.getStringExtra("PRODUCT_NAME")
                    Snackbar.show(homeLayout, "[$productName] is Added", R.color.green)
                    recycleView.smoothScrollToPosition(recyclerviewAdaptor.itemCount - 1)

                }
            }
        }
    }


    private fun initRefreshLayout(refreshLayout: SwipeRefreshLayout)
    {
        refreshLayout.setColorSchemeResources(R.color.red)
        refreshLayout.setOnRefreshListener {
            refreshData(refreshLayout)
         }
    }

    private fun refreshData(refreshLayout: SwipeRefreshLayout)
    {
        CoroutineScope(Dispatchers.IO).launch {
            initFirebase()

            CoroutineScope(Dispatchers.Main).launch {
                Handler().postDelayed({
                    refreshLayout.isRefreshing = false
                }, 1000)}
        }
    }


    private fun initFirebase()
    {
            recyclerviewAdaptor.clear()
            MainActivity.firebaseDatabaseRefAllProducts.orderByKey().addChildEventListener(object : ChildEventListener
            {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
                {
                    val product = snapshot.getValue(Product::class.java)
                    recyclerviewAdaptor.addProduct(product!!)

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?)
                {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot)
                {
                    val product = snapshot.getValue(Product::class.java)
                  // recyclerviewAdaptor.removeProduct(product!!)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?)
                {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError)
                {
                    TODO("Not yet implemented")
                }
            })

    }


    private fun initRecycleView(recycleView: RecyclerView)
    {
        recyclerviewAdaptor = AddFragmentRecycleViewAdaptor(this)
        recycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.adapter = recyclerviewAdaptor
    }

    private fun initSpinner(spinner: Spinner)
    {

        CoroutineScope(Dispatchers.Default).launch {


            val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, allCate)
            {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
                {
                    return setCentered(super.getView(position, convertView, parent))
                }
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
                {
                    return setCentered(super.getDropDownView(position, convertView, parent))
                }
                private fun setCentered(view: View): View
                {
                    val textView = view.findViewById<View>(android.R.id.text1) as TextView
                    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    return view
                }
            }

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            //Lister
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long)
                {
                    //val selectedItem = selectedItemView.findViewById<TextView>(android.R.id.text1).text.toString()
                   // var selectedItem = spinner.selectedItem
                    //var searchedTxt = searchView.query

                    val selectedItem :String? = if(spinner.selectedItem.toString() =="All")
                    {
                        null
                    }else
                    {
                        spinner.selectedItem.toString()
                    }
                    val searchedTxt :String? = if(searchView.query==null ||  searchView.query.toString().trim().isBlank())
                    {
                        null
                    } else
                    {
                        searchView.query.toString()
                    }
                    recyclerviewAdaptor.filter(searchedTxt,selectedItem)
                }
                override fun onNothingSelected(parentView: AdapterView<*>?)
                {
                }
            }
        }



    }

    private fun initSearchView(searchView: SearchView, textViewTitle: TextView)
    {
        val title = textViewTitle.text
        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus)
            {
                textViewTitle.text = ""
            } else
            {
                textViewTitle.text = title
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean
            {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean
            {
                val selectedItem :String? = if(spinner.selectedItem.toString() =="All")
                {
                    null
                }else
                {
                    spinner.selectedItem.toString()
                }
                val searchedTxt :String? = if(searchView.query==null ||  searchView.query.toString().trim().isBlank())
                {
                    null
                } else
                {
                    searchView.query.toString()
                }
                recyclerviewAdaptor.filter(searchedTxt,selectedItem)

                return false

            }

        })
    }

    override fun onCardClick(product: Product)
    {

    }

    override fun onEditCardClick(product: Product)
    {

    }

    override fun onDeleteCardClick(product: Product)
    {

    }

/*    override fun networkConnectivityChanged(event: Event)
    {
        when (event) {
            is Event.ConnectivityEvent ->
            {
                if (event.isConnected)
                {
                    showToast("Internet connected!")
                    noInternetLayout.visibility = View.GONE
                } else
                {
                    showToast("No Internet")
                    noInternetLayout.visibility = View.VISIBLE
                }
            }
        }
    }*/





}