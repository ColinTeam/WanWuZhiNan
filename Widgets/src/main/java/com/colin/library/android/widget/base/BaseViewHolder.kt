package com.colin.library.android.widget.base

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Date  :2024-09-09
 *
 * Des   :ViewHolder基类
 */
class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val arrays: SparseArrayCompat<View> = SparseArrayCompat()

    /**
     * @param id view id
     * @return View
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@IdRes id: Int): T {
        val view: View = arrays.get(id) ?: findView(id)
        return view as T
    }

    /**
     * @param id view id
     * @return View
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ViewGroup> getViewGroup(@IdRes id: Int): T {
        val view: View = arrays.get(id) ?: findView(id)
        return view as T
    }

    fun getImageView(@IdRes id: Int): ImageView = getView(id)

    fun getTextView(@IdRes id: Int): TextView = getView(id)

    fun setVisibility(@IdRes id: Int, visibility: Int) = apply {
        getView<View>(id).visibility = visibility
    }

    fun setBackgroundResource(@IdRes id: Int, @DrawableRes res: Int) = apply {
        getView<View>(id).setBackgroundResource(res)
    }

    fun setBackgroundColor(@IdRes id: Int, @ColorInt color: Int) = apply {
        getView<View>(id).setBackgroundColor(color)
    }

    fun setBackground(@IdRes id: Int, drawable: Drawable?) = apply {
        getView<View>(id).background = drawable
    }

    fun setTag(@IdRes id: Int, tag: Any?) = apply {
        getView<View>(id).tag = tag
    }

    fun setTag(@IdRes id: Int, key: Int, tag: Any?) = apply {
        getView<View>(id).setTag(key, tag)
    }

    fun setSelected(@IdRes id: Int, selected: Boolean) = apply {
        getView<View>(id).isSelected = selected
    }

    fun setAlpha(@IdRes id: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) = apply {
        getView<View>(id).alpha = alpha
    }

    fun setOnClickListener(@IdRes id: Int, listener: OnClickListener?) = apply {
        getView<View>(id).setOnClickListener(listener)
    }

    fun setOnLongClickListener(@IdRes id: Int, listener: View.OnLongClickListener?) = apply {
        getView<View>(id).setOnLongClickListener(listener)
    }

    fun setOnTouchListener(@IdRes id: Int, listener: View.OnTouchListener?) = apply {
        getView<View>(id).setOnTouchListener(listener)
    }

    fun setOnCheckedChangeListener(
        @IdRes id: Int, listener: CompoundButton.OnCheckedChangeListener?
    ) = apply {
        getView<CompoundButton>(id).setOnCheckedChangeListener(listener)
    }

    fun setText(@IdRes id: Int, res: Int) = apply {
        getTextView(id).setText(res)
    }

    fun setText(@IdRes id: Int, text: CharSequence?) = apply {
        getTextView(id).text = text
    }

    fun setTextColor(@IdRes id: Int, @ColorInt color: Int) = apply {
        getTextView(id).setTextColor(color)
    }

    fun setTextColor(@IdRes id: Int, color: ColorStateList) = apply {
        getTextView(id).setTextColor(color)
    }

    fun setTextSize(@IdRes id: Int, size: Float) = apply {
        getTextView(id).setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setTextSize(@IdRes id: Int, size: Float, unit: Int) = apply {
        getTextView(id).setTextSize(unit, size)
    }

    fun setTextBold(@IdRes id: Int, bold: Boolean) = apply {
        getTextView(id).paint.isFakeBoldText = bold
    }

    fun setTypeFace(@IdRes id: Int, typeface: Typeface?) = apply {
        getTextView(id).apply {
            this.typeface = typeface
            this.paintFlags = this.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }

    }

    fun setTypeFace(vararg @IdRes ids: Int, typeface: Typeface?) = apply {
        ids.forEach { setTypeFace(it, typeface) }
    }


    fun setImageResource(@IdRes id: Int, @DrawableRes res: Int) = apply {
        getImageView(id).setImageResource(res)
    }

    fun setImageBitmap(@IdRes id: Int, bitmap: Bitmap?) = apply {
        getImageView(id).setImageBitmap(bitmap)
    }

    fun setImageURI(@IdRes id: Int, uri: Uri?) = apply {
        getImageView(id).setImageURI(uri)
    }

    fun setImageIcon(@IdRes id: Int, icon: Icon?) = apply {
        getImageView(id).setImageIcon(icon)
    }

    fun setImageLevel(@IdRes id: Int, level: Int) = apply {
        getImageView(id).setImageLevel(level)
    }

    fun setImageDrawable(@IdRes id: Int, drawable: Drawable?) = apply {
        getImageView(id).setImageDrawable(drawable)
    }

    fun setChecked(@IdRes id: Int, checked: Boolean) = apply {
        val view = getView<View>(id)
        if (view is Checkable) view.isChecked = checked
    }

    fun setMax(@IdRes id: Int, max: Int) = apply {
        getView<ProgressBar>(id).max = max
    }

    fun setProgress(@IdRes id: Int, progress: Int) = apply {
        getView<ProgressBar>(id).progress = progress
    }

    fun setProgress(@IdRes id: Int, progress: Int, range: IntRange) = apply {
        getView<ProgressBar>(id).apply {
            this.progress = progress
            this.min = range.first
            this.max = range.last
        }
    }

    fun setRating(@IdRes id: Int, rating: Float) = apply {
        getView<RatingBar>(id).rating = rating
    }


    private fun <T : View> findView(@IdRes id: Int): T {
        val view = itemView.findViewById<T>(id)
        arrays.put(id, view)
        return view
    }
}