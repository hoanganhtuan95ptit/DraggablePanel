package com.hoanganhtuan95ptit.draggable.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.isGone() = visibility == View.GONE

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInVisible() = visibility == View.INVISIBLE

fun View.setMargins(
        leftMarginDp: Int? = null,
        topMarginDp: Int? = null,
        rightMarginDp: Int? = null,
        bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.toPx() }
        topMarginDp?.run { params.topMargin = this.toPx() }
        rightMarginDp?.run { params.rightMargin = this.toPx() }
        bottomMarginDp?.run { params.bottomMargin = this.toPx() }
        requestLayout()
    }
}

fun RelativeLayout.add(view: View) {
    val layoutParams = RelativeLayout.LayoutParams(view.width, view.height)
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
    add(view, layoutParams)
}

fun ViewGroup.add(view: View, layoutParams: ViewGroup.LayoutParams) {
    view.parent?.let {
        (it as ViewGroup).removeView(view)
    }
    addView(view, layoutParams)
}


fun View.translationYAnimation(toTranslationY: Int,
                               duration: Long = 350,
                               onEnd: () -> Unit) {

    val pointList = ArrayList<ValuesHolder>()
    pointList.add(ValuesHolder("translation", translationY, toTranslationY))

    pointList.animation(duration, { keyData, valueAnimator ->
        translationY = keyData.get("translation") as Float
    }, {
        onEnd()
    })
}


fun View.alphaAnimation(
        toAlpha: Float,
        duration: Long = 350
) {
    alphaAnimation(toAlpha, duration) {}
}

fun View.alphaAnimation(
        toAlpha: Float,
        duration: Long,
        onEnd: () -> Unit
) {
    visible()

    val pointList = ArrayList<ValuesHolder>()
    pointList.add(ValuesHolder("alpha", this.alpha, toAlpha))

    pointList.animation(duration, { keyData, valueAnimator ->
        alpha = keyData.get("alpha") as Float
    }, {
        if (toAlpha == 0f) {
            gone()
        }
        onEnd()
    })
}

fun View.resizeAnimation(
        width: Int,
        height: Int,
        duration: Long = 350
) {
    resizeAnimation(width, height, duration) {}
}

fun View.resizeAnimation(
        width: Int,
        height: Int,
        duration: Long = 350,
        onEnd: () -> Unit
) {

    val pointList = ArrayList<ValuesHolder>()
    pointList.add(
            ValuesHolder(
                    "width",
                    if (layoutParams.width <= 0 && width > 0) this.width else layoutParams.width,
                    width
            )
    )
    pointList.add(
            ValuesHolder(
                    "height",
                    if (layoutParams.height <= 0 && height > 0) this.height else layoutParams.height,
                    height
            )
    )

    pointList.animation(duration, { keyData, valueAnimator ->
        resize(keyData.get("width") as Int, keyData.get("height") as Int)
    }, {
        onEnd()
    })
}

fun View.paddingAnimation(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int,
        duration: Long
) {
    marginAnimation(leftPadding, topPadding, rightPadding, bottomPadding, duration) {}
}

fun View.paddingAnimation(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int,
        duration: Long,
        onEnd: () -> Unit
) {
    val pointList = ArrayList<ValuesHolder>()
    pointList.add(
            ValuesHolder("topPadding", paddingTop, topPadding)
    )
    pointList.add(
            ValuesHolder("bottomPadding", paddingBottom, bottomPadding)
    )
    pointList.add(
            ValuesHolder("leftPadding", paddingLeft, leftPadding)
    )
    pointList.add(
            ValuesHolder("rightPadding", paddingRight, rightPadding)
    )

    pointList.animation(duration, { keyData, valueAnimator ->
        val paddingTop = keyData.get("topPadding") as Int
        val paddingBottom = keyData.get("bottomPadding") as Int
        val paddingLeft = keyData.get("leftPadding") as Int
        val paddingRight = keyData.get("rightPadding") as Int

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }, {
        onEnd()
    })
}

fun View.marginAnimation(
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int,
        duration: Long
) {
    marginAnimation(leftMargin, topMargin, rightMargin, bottomMargin, duration) {}
}

fun View.marginAnimation(
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int,
        duration: Long,
        onEnd: () -> Unit
) {
    val pointList = ArrayList<ValuesHolder>()
    val layoutParams = layoutParams as RelativeLayout.LayoutParams
    pointList.add(ValuesHolder("topMargin", layoutParams.topMargin, topMargin))
    pointList.add(ValuesHolder("bottomMargin", layoutParams.bottomMargin, bottomMargin))
    pointList.add(ValuesHolder("leftMargin", layoutParams.leftMargin, leftMargin))
    pointList.add(ValuesHolder("rightMargin", layoutParams.rightMargin, rightMargin))

    pointList.animation(duration, { keyData, valueAnimator ->
        layoutParams.topMargin = keyData.get("topMargin") as Int
        layoutParams.bottomMargin = keyData.get("bottomMargin") as Int
        layoutParams.leftMargin = keyData.get("leftMargin") as Int
        layoutParams.rightMargin = keyData.get("rightMargin") as Int
        this.layoutParams = layoutParams
    }, {
        onEnd()
    })
}

fun View.translationYAnim(
        value: Float,
        duration: Long,
        onEnd: () -> Unit
) {
    val pointList = ArrayList<ValuesHolder>()
    pointList.add(ValuesHolder("translationY", translationY, value))
    pointList.animation(duration, { keyData, valueAnimator ->
        translationY = keyData.get("translationY") as Float
    }, {
        onEnd()
    })
}

fun View.reHeight(height: Int) {
    resize(-1, height)
}

fun View.reWidth(width: Int) {
    resize(width, -1)
}

fun View.resize(width: Int, height: Int) {
    var newWidth = layoutParams.width
    var newHeight = layoutParams.height

    if (width >= 0) newWidth = width
    if (height >= 0) newHeight = height

    if (newWidth != layoutParams.width || newHeight != layoutParams.height) {
        layoutParams.width = width
        layoutParams.height = height
        layoutParams = layoutParams
    }
}

fun ViewGroup.inflate(@LayoutRes l: Int): View {
    return LayoutInflater.from(context).inflate(l, this, false)
}
