package com.ash.admincontrol.interfaces

import android.view.View
import com.ash.admincontrol.models.Product


interface AddFragmentRecycleViewInterface
{
    fun onCardClick(product: Product)
    fun onEditCardClick(product: Product)
    fun onDeleteCardClick(product: Product)

}
