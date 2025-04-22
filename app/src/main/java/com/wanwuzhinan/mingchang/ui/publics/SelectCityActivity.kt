package com.wanwuzhinan.mingchang.ui.publics

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.CityListAdapter
import com.wanwuzhinan.mingchang.data.CityEntityEvent
import com.wanwuzhinan.mingchang.data.CityListData
import com.wanwuzhinan.mingchang.databinding.ActivitySelectCityBinding
import com.wanwuzhinan.mingchang.view.indexlib.indexbar.widget.CustomLinearLayoutManager
import com.wanwuzhinan.mingchang.view.indexlib.suspension.SuspensionDecoration
import com.wanwuzhinan.mingchang.vm.ShopViewModel
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.post
import com.ssm.comm.ext.setOnClickNoRepeat
import com.ssm.comm.ui.base.BaseActivity

//选择城市
class SelectCityActivity : BaseActivity<ActivitySelectCityBinding, ShopViewModel>(ShopViewModel()) {

    var mProvinceCode = ""
    var mCityCode = ""
    var mAreaCode = ""
    var mType = 1;//1省 2市 3区
    var mProvinceAdapter: CityListAdapter? = null
    var mCityAdapter: CityListAdapter? = null
    var mAreaAdapter: CityListAdapter? = null
    var mProvinceManager: CustomLinearLayoutManager? = null
    var mCityManager: CustomLinearLayoutManager? = null
    var mAreaManager: CustomLinearLayoutManager? = null
    var mProvinceDecoration: SuspensionDecoration? = null
    var mCityDecoration: SuspensionDecoration? = null
    var mAreaDecoration: SuspensionDecoration? = null
    var mProvinceList: MutableList<CityListData>? = null
    var mCityList: MutableList<CityListData>? = null
    var mAreaList: MutableList<CityListData>? = null
    var mCityEntityEvent: CityEntityEvent? = null

    override fun initView() {

        initClick()
        initList()
        initIndexBar()

        showBaseLoading()
        mViewModel.getCityList()
    }

    private fun initIndexBar() {
        mDataBinding.indexProvince.setPressedShowTextView(mDataBinding.tvSideProvince)
            .setNeedRealIndex(false)
            .setLayoutManager(mProvinceManager)

        mDataBinding.indexCity.setPressedShowTextView(mDataBinding.tvSideCity)
            .setNeedRealIndex(false)
            .setLayoutManager(mCityManager)

        mDataBinding.indexArea.setPressedShowTextView(mDataBinding.tvSideArea)
            .setNeedRealIndex(false)
            .setLayoutManager(mAreaManager)
    }

    private fun initList() {
        mProvinceManager = CustomLinearLayoutManager(this)
        mProvinceAdapter = CityListAdapter()
        mProvinceList = mutableListOf()

        mProvinceDecoration = SuspensionDecoration(this, mProvinceList)
        mDataBinding.reProvince.layoutManager = mProvinceManager
        mDataBinding.reProvince.adapter = mProvinceAdapter
        mDataBinding.reProvince.addItemDecoration(mProvinceDecoration!!)


        mCityManager = CustomLinearLayoutManager(this)
        mCityAdapter = CityListAdapter()
        mCityList = mutableListOf()
        mCityDecoration = SuspensionDecoration(this, mCityList)
        mDataBinding.reCity.layoutManager = mCityManager
        mDataBinding.reCity.adapter = mCityAdapter
        mDataBinding.reCity.addItemDecoration(mCityDecoration!!)

        mAreaManager = CustomLinearLayoutManager(this)
        mAreaAdapter = CityListAdapter()
        mAreaList = mutableListOf()
        mAreaDecoration = SuspensionDecoration(this, mAreaList)
        mDataBinding.reArea.layoutManager = mAreaManager
        mDataBinding.reArea.adapter = mAreaAdapter
        mDataBinding.reArea.adapter = mAreaAdapter
        mDataBinding.reArea.addItemDecoration(mAreaDecoration!!)

        mProvinceAdapter!!.setOnItemClickListener { adapter, view, position ->
            mProvinceCode = mProvinceList!![position].city_id
            mDataBinding.linAddress.visibility = View.VISIBLE

            mDataBinding.tvProvince.text = mProvinceList!!.get(position).name
            mDataBinding.tvCity.text = "请选择"
            mDataBinding.tvArea.text = ""
            selectView(
                mDataBinding.tvCity,
                mDataBinding.tvProvince,
                mDataBinding.tvArea,
                mDataBinding.rlCity,
                mDataBinding.rlProvince,
                mDataBinding.rlArea
            )

            mType = 2
            showBaseLoading()
            mViewModel.getCityList(mProvinceList!![position].city_id)
        }

        mCityAdapter!!.setOnItemClickListener { adapter, view, position ->
            mCityCode = mCityList!![position].city_id

            mDataBinding.tvCity.text = mCityList!!.get(position).name
            mDataBinding.tvArea.text = "请选择"
            selectView(
                mDataBinding.tvArea,
                mDataBinding.tvProvince,
                mDataBinding.tvCity,
                mDataBinding.rlArea,
                mDataBinding.rlCity,
                mDataBinding.rlProvince
            )

            mType = 3
            showBaseLoading()
            mViewModel.getCityList(mCityList!![position].city_id)
        }

        mAreaAdapter!!.setOnItemClickListener { adapter, view, position ->
            mAreaCode = mAreaList!![position].city_id
            mDataBinding.tvArea.text = mAreaList!![position].name

            finishResult()
        }

        mProvinceAdapter!!.isEmptyViewEnable=true
        mCityAdapter!!.isEmptyViewEnable=true
        mAreaAdapter!!.isEmptyViewEnable=true
        mProvinceAdapter!!.setEmptyViewLayout(this,com.ssm.comm.R.layout.base_empty_layout)
        mCityAdapter!!.setEmptyViewLayout(this,com.ssm.comm.R.layout.base_empty_layout)
        mAreaAdapter!!.setEmptyViewLayout(this,com.ssm.comm.R.layout.base_empty_layout)
    }

    private fun finishResult() {
        val province = mDataBinding.tvProvince.text.toString()
        val city = mDataBinding.tvCity.text.toString()
        val area = mDataBinding.tvArea.text.toString()

        mCityEntityEvent =
            CityEntityEvent(mProvinceCode, province, mCityCode, city, mAreaCode, area)
        post(MessageEvent.SELECT_CITY, mCityEntityEvent!!)
        finish()

    }

    override fun initRequest() {
        mViewModel.cityListLiveData.observeState(this) {
            onSuccess = { data, msg ->
                when (mType) {
                    1 -> {
                        mProvinceList!!.clear()
                        mProvinceList!!.addAll(data!!)

                        mDataBinding.indexProvince.setSourceDatas(mProvinceList).invalidate()
                        mProvinceAdapter!!.submitList(mProvinceList)
                        mProvinceDecoration!!.setDatas(mProvinceList)

                        mDataBinding.reProvince.scrollToPosition(0)
                    }
                    2 -> {
                        mCityList!!.clear()
                        mCityList!!.addAll(data!!)
                        mDataBinding.indexCity.setSourceDatas(mCityList).invalidate()
                        mCityAdapter!!.submitList(mCityList)
                        mCityDecoration!!.setDatas(mCityList)

                        mDataBinding.reCity.scrollToPosition(0)
                    }
                    3 -> {
                        mAreaList!!.clear()
                        mAreaList!!.addAll(data!!)
                        mDataBinding.indexArea.setSourceDatas(mAreaList).invalidate()
                        mAreaAdapter!!.submitList(mAreaList)
                        mAreaDecoration!!.setDatas(mAreaList)

                        mDataBinding.reArea.scrollToPosition(0)
                    }
                }
            }

            onDataEmpty2 = { msg ->
                if (mType == 3) {
                    finishResult()
                }
            }
        }
    }

    override fun initClick() {
        setOnClickNoRepeat(
            mDataBinding.tvProvince,
            mDataBinding.tvCity,
            mDataBinding.tvArea
        ) {
            when (it) {
                mDataBinding.tvProvince -> {//省
                    selectView(
                        mDataBinding.tvProvince,
                        mDataBinding.tvCity,
                        mDataBinding.tvArea,
                        mDataBinding.rlProvince,
                        mDataBinding.rlCity,
                        mDataBinding.rlArea
                    )
                }
                mDataBinding.tvCity -> {//市
                    selectView(
                        mDataBinding.tvCity,
                        mDataBinding.tvProvince,
                        mDataBinding.tvArea,
                        mDataBinding.rlCity,
                        mDataBinding.rlProvince,
                        mDataBinding.rlArea
                    )
                }
                mDataBinding.tvArea -> {//区
                    selectView(
                        mDataBinding.tvArea,
                        mDataBinding.tvProvince,
                        mDataBinding.tvCity,
                        mDataBinding.rlArea,
                        mDataBinding.rlCity,
                        mDataBinding.rlProvince
                    )
                }
            }
        }
    }

    private fun selectView(
        tv1: TextView,
        tv2: TextView,
        tv3: TextView,
        rv1: RelativeLayout,
        rv2: RelativeLayout,
        rv3: RelativeLayout
    ) {
        tv1.setTextColor(resources!!.getColor(R.color.color_default))
        tv2.setTextColor(resources!!.getColor(R.color.color_333333))
        tv3.setTextColor(resources!!.getColor(R.color.color_333333))

        rv1.visibility = View.VISIBLE
        rv2.visibility = View.GONE
        rv3.visibility = View.GONE
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_city
    }
}