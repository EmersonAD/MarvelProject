package com.souzaemerson.marvelproject.view.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.souzaemerson.marvelproject.databinding.ItemCarouselBinding
import com.souzaemerson.marvelproject.util.Image
import kotlin.math.roundToInt

class CarouselAdapter(private val imageList: List<Image>) :
    RecyclerView.Adapter<CarouselAdapter.MyViewHolder>() {

    private var hasInitParentDimensions = false
    private var maxImageWidth: Int = 0
    private var maxImageHeight: Int = 0
    private var maxImageAspectRatio: Float = 1f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        setParentDimensions(parent)
        val view = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(view, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comics = imageList[position]

        holder.run {
            layoutParamsConfiguration(comics)
            scrollToItemClicked(position)
        }
    }

    override fun getItemCount(): Int = imageList.count()

    class MyViewHolder(private val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(comics: Image) {
            binding.run {
                Glide.with(itemView)
                    .load(comics.url)
                    .transition(withCrossFade())
                    .transform(
                        FitCenter()
                    )
                    .into(imageCarousel)

            }
        }
    }

    private fun setParentDimensions(parent: ViewGroup) {
        if (!hasInitParentDimensions) {
            maxImageWidth = (parent.width * 0.75f).roundToInt()
            maxImageHeight = parent.height
            maxImageAspectRatio = maxImageWidth.toFloat() / maxImageHeight.toFloat()
            hasInitParentDimensions = true
        }
    }

    private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
                val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
                val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

                return (dxToStart + dxToEnd) / 2
            }
        }

        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun MyViewHolder.scrollToItemClicked(
        position: Int
    ) {
        itemView.setOnClickListener {
            val rv = itemView.parent as RecyclerView
            rv.smoothScrollToCenteredPosition(position)
        }
    }


    private fun MyViewHolder.layoutParamsConfiguration(
        comics: Image
    ) {
        val targetImageWidth: Int = aspectRatioConfiguration(comics)

        itemView.layoutParams =
            RecyclerView.LayoutParams(targetImageWidth, RecyclerView.LayoutParams.MATCH_PARENT)
        bindView(comics)
    }

    private fun aspectRatioConfiguration(comics: Image): Int {
        val imageAspectRatio = comics.aspectRatio

        val targetImageWidth: Int = if (imageAspectRatio < maxImageAspectRatio) {
            (maxImageHeight * imageAspectRatio).roundToInt()
        } else {
            maxImageWidth
        }
        return targetImageWidth
    }

}