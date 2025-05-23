package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.wanwuzhinan.mingchang.data.AddressData
import com.wanwuzhinan.mingchang.data.CityListData
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.net.repository.ShopRepository
import com.ssm.comm.ext.StateMutableLiveData
import com.ssm.comm.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class ShopViewModel : BaseViewModel<UserInfoData, ShopRepository>(ShopRepository()) {

    val getAddressListLiveData = StateMutableLiveData<MutableList<AddressData>>()
    val addAddressLiveData = StateMutableLiveData<MutableList<String>>()
    val editAddressListData = StateMutableLiveData<MutableList<String>>()
    val deleteAddressListData = StateMutableLiveData<MutableList<String>>()
    val getDefaultAddressListData = StateMutableLiveData<AddressData>()
    val cityListLiveData = StateMutableLiveData<MutableList<CityListData>>()

    //获取我的收货地址
    fun getAddressList() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getAddressListLiveData.value = repository.getAddressList()
        }
    }

    //添加我的收货地址
    fun addAddress(map: MutableMap<String, Any>) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            addAddressLiveData.value = repository.addAddress(map)
        }
    }



    //获取我的默认收货地址
    fun getDefaultAddress() {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            getDefaultAddressListData.value = repository.getDefaultAddress()
        }
    }

    //地址列表
    fun getCityList(city_id: String = "") {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            cityListLiveData.value = repository.getCityList(city_id)
        }
    }

}