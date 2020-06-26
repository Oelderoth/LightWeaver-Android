package com.example.lightweaver.mobile

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("srcCompat")
fun setImg(view: ImageView, resId: Int) {
    view.setImageDrawable(ContextCompat.getDrawable(view.context, resId))
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:drawableEnd")
fun setDrawableEnd(view: TextView, resId: Int) {
    val drawable = ContextCompat.getDrawable(view.context, resId)
    val compoundDrawables = view.compoundDrawables
    view.setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
}

@BindingAdapter("android:text")
fun setAutocompleteTextWithoutFilter(view: AutoCompleteTextView, str: String) {
    view.setText(str, false)
}
