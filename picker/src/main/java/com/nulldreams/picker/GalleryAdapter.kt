package com.nulldreams.picker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import java.util.ArrayList

class GalleryAdapter(context: Context) : RecyclerView.Adapter<ItemHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    private val mThumbnails: MutableList<Thumb>

    private val mSelectedItems: MutableList<Thumb>

    init {
        mThumbnails = ArrayList()
        mSelectedItems = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        return ItemHolder(mInflater.inflate(R.layout.layout_picker_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val thumb = mThumbnails[position]
        Glide.with(holder.itemView).asBitmap().load(thumb.path)
                .apply(RequestOptions.placeholderOf(R.drawable.bg_place_holder))
                .into(holder.thumb)
        holder.flag.visibility = if (isPicked(thumb)) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener { select(thumb) }
    }

    override fun getItemCount(): Int {
        return mThumbnails.size
    }

    private fun indexOf(thumb: Thumb): Int {
        return mThumbnails.indexOf(thumb)
    }

    private fun isPicked (thumb: Thumb): Boolean {
        return mSelectedItems.contains(thumb)
    }

    fun addAll(thumbs: List<Thumb>) {
        mThumbnails.addAll(thumbs)
        notifyDataSetChanged()
    }

    private fun select(thumb: Thumb) {
        when (PickerManager.instance().getChoiceMode()) {
            PickerManager.MODE_SINGLE -> {
                if (mSelectedItems.contains(thumb)) {
                    return
                }
                if (mSelectedItems.isNotEmpty()) {
                    notifyItemChanged(indexOf(mSelectedItems.removeAt(0)))
                }
                mSelectedItems.add(thumb)
                notifyItemChanged(indexOf(thumb))
            }
            PickerManager.MODE_MULTIPLE -> {
                if (mSelectedItems.contains(thumb)) {
                    mSelectedItems.remove(thumb)
                } else {
                    mSelectedItems.add(thumb)
                }
                notifyItemChanged(indexOf(thumb))
            }
        }
    }

    fun getSelectedItems () : List<Thumb> {
        return mSelectedItems
    }
}
