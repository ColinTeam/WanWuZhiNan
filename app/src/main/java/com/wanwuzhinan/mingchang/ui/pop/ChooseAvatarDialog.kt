package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.colin.library.android.utils.ext.onClick
import com.google.android.material.tabs.TabLayout
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.data.TabBean
import com.wanwuzhinan.mingchang.databinding.DialogChooseAvatarBinding
import com.wanwuzhinan.mingchang.ui.adapter.ChooseAvatarAdapter
import kotlin.math.max
import kotlin.math.min

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-13 11:00
 *
 * Des   :ChooseAvatarDialog
 */
class ChooseAvatarDialog private constructor(
    var tab: Int, var position: Int
) : AppDialogFragment<DialogChooseAvatarBinding>() {
    var adapter: ChooseAvatarAdapter? = null
    var sure: ((Int) -> Unit) = { }
    var cancel: ((View) -> Unit) = { }

    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {

        viewBinding.apply {
            adapter = ChooseAvatarAdapter(getTabList()).apply {
                onItemClickListener = itemListener
            }
            list.adapter = adapter
            tabChoose.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val isMan = tab?.text == getString(R.string.setting_man)
                    updateTab(if (isMan) TAB_MAN else TAB_WOMAN)
                }


                override fun onTabUnselected(position: TabLayout.Tab?) {
                }

                override fun onTabReselected(position: TabLayout.Tab?) {
                }

            })
            onClick(ivLeft, ivRight, btCancel, btSure) {
                when (it) {
                    ivLeft -> {
                        val selected = position - 1
                        selected(max(selected, 0))
                    }

                    ivRight -> {
                        val selected = position + 1
                        selected(min(selected, adapter?.itemCount ?: 0))
                    }

                    btCancel -> {
                        cancel.invoke(it)
                        dismiss()
                    }

                    btCancel -> {
                        sure.invoke(adapter?.selected ?: 0)
                        dismiss()
                    }
                }
            }
        }

    }

    private fun updateTab(position: Int) {
        if (tab == position) return
        this.tab = position
        selected(position)
    }

    private fun selected(position: Int, update: Boolean = true) {
        val itemCount = (adapter?.itemCount ?: 0)
        if (itemCount == 0) return
        val range = 0..itemCount
        val selected = if (position <= range.start) 0
        else if (position >= range.last) range.last - 1
        else position
        if (this.position == selected && !update) return
        this.position = selected
        adapter?.selected = selected
        val item = adapter!!.item[selected]
        viewBinding.ivAvatar.setImageResource(item.id)
    }

    private val itemListener: ((View, TabBean, Int) -> Unit) = { v, item, position ->
        selected(position, true)
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.tabChoose.getTabAt(tab)?.select()
        selected(position, true)
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    private fun getTabList(): ArrayList<TabBean> {
        val list = arrayListOf<TabBean>()
        val array = resources.obtainTypedArray(R.array.setting_avater)
        for (index in 0 until array.length()) {
            val iconResId = array.getResourceId(index, -1)
            if (iconResId != -1) {
                list.add(TabBean(iconResId, "avatar"))
            }
        }
        array.recycle()
        return list
    }


    companion object {
        const val TAB_MAN = 0
        const val TAB_WOMAN = 1
        const val EXTRAS_TAB = "EXTRAS_TAB"
        const val EXTRAS_POSITION = "EXTRAS_POSITION"

        @JvmStatic
        fun newInstance(tab: Int, position: Int): ChooseAvatarDialog {
            val bundle = Bundle().apply {
                putInt(EXTRAS_TAB, tab)
                putInt(EXTRAS_POSITION, position)
            }
            val fragment = ChooseAvatarDialog(tab, position)
            fragment.arguments = bundle
            fragment.gravity = Gravity.TOP
            return fragment
        }
    }
}
