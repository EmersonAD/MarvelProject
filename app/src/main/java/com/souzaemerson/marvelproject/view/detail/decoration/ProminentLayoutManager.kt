package com.souzaemerson.marvelproject.view.detail.decoration

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

internal class ProminentLayoutManager(
    context: Context,
    private val minScaleDistanceFactor: Float = 1.5f,
    private val scaleDownBy: Float = 0.5f
) : LinearLayoutManager(context, HORIZONTAL, false) {

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleChildren() }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleChildren()
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return (width / (1 - scaleDownBy)).roundToInt()
    }

    private fun scaleChildren() {
        val containerCenter = width / 2f

        val scaleDistanceThreshold = minScaleDistanceFactor * containerCenter

        var translationXForward = 0f

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!

            val childCenter = (child.left + child.right) / 2f
            val distanceToCenter = kotlin.math.abs(childCenter - containerCenter)

            val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f)
            val scale = 1f - scaleDownBy * scaleDownAmount

            child.scaleX = scale
            child.scaleY = scale

            val translationDirection = if (childCenter > containerCenter) -1 else 1
            val translationXFromScale = translationDirection * child.width * (1 - scale) / 2f
            child.translationX = translationXFromScale + translationXForward

            translationXForward = 0f

            if (translationXFromScale > 0 && i >= 1) {
                getChildAt(i - 1)!!.translationX += 2 * translationXFromScale

            } else if (translationXFromScale < 0) {
                translationXForward = 2 * translationXFromScale
            }
        }
    }
}