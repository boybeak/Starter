package com.github.boybeak.starter.widget

import android.content.Context
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.widget.ListAdapter
import com.github.boybeak.starter.R

class SearchBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SearchView(context, attrs, defStyleAttr) {

    private val searchBtn: AppCompatImageView = findViewById(R.id.search_mag_icon)
    private val searchEt: AppCompatAutoCompleteTextView = findViewById(R.id.search_src_text)

    init {
        val attrsArray = intArrayOf(
                R.attr.actionBarItemBackground
        )
        val ta = context.obtainStyledAttributes(attrs, attrsArray)

        val btnDrawable = ta.getDrawable(0)

        ta.recycle()

        searchBtn.background = btnDrawable
    }

    fun setSearchButtonListener (listener: OnClickListener) {
        searchBtn.setOnClickListener(listener)
    }

    fun <T> setSuggestionsAdapter (adapter: T)
            where T : ListAdapter, T : android.widget.Filterable  {
        searchEt.setAdapter(adapter)
    }

    override fun setQuery(query: CharSequence?, submit: Boolean) {
        super.setQuery(query, submit)
        searchEt.dismissDropDown()
    }

}