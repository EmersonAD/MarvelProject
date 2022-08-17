package com.souzaemerson.marvelproject.view.detail.adapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.souzaemerson.marvelproject.data.model.comic.Result

class ComicsAndSeriesFields(private val item: Result) {

    private val page = item.pageCount.toString()
    private val price = item.prices
    private val itemPrice = item.prices?.first()?.price
    private val date = item.dates

    fun setPricesFields(view: TextView, text: String) {
        if (price.isNullOrEmpty() || itemPrice.toString() == "0.0") {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setPageFields(view: TextView, text: String) {
        if (page.isNullOrEmpty() || page == "0") {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setOnSaleDateFields(view: TextView, text: String) {
        if (date.isNullOrEmpty()) {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setVisibilityAs(textView: View, mode: Int) {
        textView.visibility = mode
    }
}