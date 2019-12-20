package com.hoanganhtuan95ptit

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.google.android.material.appbar.AppBarLayout
import com.hoanganhtuan95ptit.utils.resize
import com.hoanganhtuan95ptit.utils.toPx
import com.hoanganhtuan95ptit.widget.DragBehavior
import com.hoanganhtuan95ptit.widget.DragFrame
import kotlinx.android.synthetic.main.layout_draggable_panel.view.*
import kotlin.math.abs
import kotlin.math.min


class DraggablePanel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {

        private var scaledTouchSlop = 0

        private fun getScaledTouchSlop(context: Context): Int {
            if (scaledTouchSlop == 0) {
                scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
            }
            return scaledTouchSlop
        }

        private fun isViewUnder(view: View?, ev: MotionEvent): Boolean {
            return if (view == null) {
                false
            } else {
                ev.x >= view.left && ev.x < view.right && ev.y >= view.top && ev.y < view.bottom
            }
        }

        private fun calculateDistance(event: MotionEvent, downY: Float): Int {
            return abs(event.rawY - downY).toInt()
        }
    }

    private var verticalOffsetOld = 0

    private var velocityY = 0f
    private var velocityTracker: VelocityTracker? = null

    private var mPercentCurrent = -1f

    private var mMarginTopMin = 0
    private var mMarginTopCurrent = -1

    private var mMarginEdgMin = 8.toPx()
    private var mMarginBottomMin = 8.toPx()

    private var mHeightMaxDefault = 200.toPx()
    private var mHeightMax = 350.toPx()
    private var mHeightMin = 80.toPx()

    init {
        inflate(context, R.layout.layout_draggable_panel, this)

        appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val delta = verticalOffset - verticalOffsetOld
            verticalOffsetOld = verticalOffset
            mHeightMin -= delta
        })

        frameDrag.onTouchListener = object : DragFrame.OnTouchListener {

            private var downY = 0f
            private var deltaY = 0

            private var scrolling = false
            private var firstViewDown = false

            override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                when (ev!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downY = ev.rawY
                        onTouchEvent(ev)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        scrolling = false
                        firstViewDown = false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (scrolling) {
                            return true
                        }
                        val calculateDiff: Int = calculateDistance(ev, downY)
                        val scaledTouchSlop: Int = getScaledTouchSlop(getContext())
                        if (calculateDiff > scaledTouchSlop && firstViewDown) {
                            scrolling = true
                            return true
                        }
                    }
                }
                return scrolling
            }

            override fun onTouchEvent(ev: MotionEvent?): Boolean {
                val motionY = ev!!.rawY.toInt()
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        firstViewDown = isViewUnder(frameFirst, ev)
                        println("onTouchEvent" + firstViewDown)
                        deltaY = motionY - (frameDrag.layoutParams as LayoutParams).topMargin
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        scrolling = false
                        firstViewDown = false
                        handleUp()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        handleMove(motionY)
                    }
                }
                return firstViewDown
            }

            private fun handleUp() {
                val finalPosition = if (abs(velocityY) < 200) {
                    if (mMarginTopCurrent > mMarginTopMin - mMarginTopCurrent) mMarginTopMin else 0
                } else {
                    if (velocityY < 0) 0 else mMarginTopMin
                }

                val springX = SpringForce(finalPosition.toFloat())
                springX.dampingRatio = 0.7f
                springX.stiffness = 300f

                val springAnimation = SpringAnimation(FloatValueHolder())
                springAnimation.setStartVelocity(velocityY)
                        .setMinValue(0.toFloat())
                        .setMaxValue(mMarginTopMin.toFloat())
                        .setStartValue(mMarginTopCurrent.toFloat())
                        .setSpring(springX)
                        .setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS)
                        .addUpdateListener { dynamicAnimation: DynamicAnimation<*>, value: Float, _: Float ->
                            setMarginTop(value.toInt())
                            if (value == finalPosition.toFloat()) dynamicAnimation.cancel()
                        }
                        .addEndListener { _: DynamicAnimation<*>?, b: Boolean, _: Float, _: Float ->
                        }
                        .start()
            }

            private fun handleMove(motionY: Int) {
                setMarginTop(motionY - deltaY)
            }

        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                val params = frameFirst.layoutParams as CoordinatorLayout.LayoutParams
                params.behavior = DragBehavior()
                frameFirst.layoutParams = params

                mMarginTopMin = height - mHeightMin - mMarginBottomMin

                setMarginTop(0)
            }
        })
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                } else {
                    velocityTracker!!.clear()
                }
                velocityTracker!!.addMovement(ev)
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker!!.computeCurrentVelocity(1000)
                velocityY = velocityTracker!!.yVelocity
                velocityTracker!!.recycle()
                velocityTracker = null
            }
            MotionEvent.ACTION_CANCEL -> {
                velocityY = velocityTracker!!.yVelocity
                velocityTracker!!.recycle()
                velocityTracker = null
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker!!.addMovement(ev)
                velocityTracker!!.computeCurrentVelocity(1000)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun setMarginTop(top: Int) {
        val marginTop = when {
            top < 0 -> 0
            top > mMarginTopMin -> mMarginTopMin
            else -> top
        }

        if (marginTop == mMarginTopCurrent) return
        mMarginTopCurrent = marginTop

        val percent: Float = mMarginTopCurrent * 1f / mMarginTopMin
        setPercent(percent)
    }

    private fun setPercent(percent: Float) {
        if (mPercentCurrent == percent || percent > 1 || percent < 0) return
        mPercentCurrent = percent

        val layoutParams = frameDrag.layoutParams as LayoutParams
        layoutParams.topMargin = (mMarginTopMin * mPercentCurrent).toInt()
        layoutParams.leftMargin = (mMarginEdgMin * mPercentCurrent).toInt()
        layoutParams.rightMargin = (mMarginEdgMin * mPercentCurrent).toInt()
        layoutParams.bottomMargin = (mMarginBottomMin * mPercentCurrent).toInt()
        frameDrag.layoutParams = layoutParams

        val height = (mHeightMax - (mHeightMax - mHeightMin) * mPercentCurrent).toInt()

        appbarLayout.resize(-1, height)
        toolbar.resize(-1, min(mHeightMaxDefault, (mHeightMaxDefault - (mHeightMaxDefault - mHeightMin) * mPercentCurrent).toInt()))
    }

}