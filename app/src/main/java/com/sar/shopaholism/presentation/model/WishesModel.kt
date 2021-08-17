package com.sar.shopaholism.presentation.model

import androidx.lifecycle.MutableLiveData
import com.sar.shopaholism.domain.entity.Wish

class WishesModel {
    var wishes = MutableLiveData<MutableList<Wish>>(mutableListOf())
}