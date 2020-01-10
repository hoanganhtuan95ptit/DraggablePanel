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

    private var init = false

    private var scrolling = false

    private var velocityY = 0f
    private var velocityTracker: VelocityTracker? = null

    private var dragBehavior: DragBehavior? = null

    private var mCurrentPercent = -1f
    private var mCurrentMarginTop = -1

    private var mHeightWhenMaxDefault = 200.toPx()
    private var mHeightWhenMax = 350.toPx()

//    private var mHeightWhenMiddleDefault = 200.toPx()
//    private var mHeightWhenMiddle = 350.toPx()
//    private var mPercentWhenMiddle = 0.9f

    private var mHeightWhenMinDefault = 80.toPx()
    private var mHeightWhenMin = 80.toPx()

    private var mMarginTopWhenMin = 0
    private var mMarginEdgeWhenMin = 8.toPx()
    private var mMarginBottomWhenMin = 8.toPx()

    init {
        inflate(context, R.layout.layout_draggable_panel, this)


        frameDrag.onTouchListener = object : DragFrame.OnTouchListener {

            private var downY = 0f
            private var deltaY = 0

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
                    if (mCurrentMarginTop > mMarginTopWhenMin - mCurrentMarginTop) mMarginTopWhenMin else 0
                } else {
                    if (velocityY < 0) 0 else mMarginTopWhenMin
                }

                val springX = SpringForce(finalPosition.toFloat())
                springX.dampingRatio = 0.7f
                springX.stiffness = 300f

                val springAnimation = SpringAnimation(FloatValueHolder())
                springAnimation.setStartVelocity(velocityY)
                        .setMinValue(0.toFloat())
                        .setMaxValue(mMarginTopWhenMin.toFloat())
                        .setStartValue(mCurrentMarginTop.toFloat())
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

                init = true

                dragBehavior = DragBehavior(frameSecond)

                val params = frameFirst.layoutParams as CoordinatorLayout.LayoutParams
                params.behavior = dragBehavior
                frameFirst.layoutParams = params

                mMarginTopWhenMin = height - mHeightWhenMin - mMarginBottomWhenMin

//                mHeightWhenMiddle = (height - mPercentWhenMiddle * bottom - mPercentWhenMiddle * mMarginTopWhenMin).toInt()

                setMarginTop(0)
            }
        })

        appbarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            private var verticalOffsetOld = 0

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (init && mCurrentPercent == 0f && !scrolling) {
                    val offset = abs(verticalOffset)
                    val delta = offset - verticalOffsetOld
                    verticalOffsetOld = offset

                    mHeightWhenMin += delta
//                    mHeightWhenMiddle += delta
                }
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
            top > mMarginTopWhenMin -> mMarginTopWhenMin
            else -> top
        }

        if (marginTop == mCurrentMarginTop) return
        mCurrentMarginTop = marginTop

        val percent: Float = mCurrentMarginTop * 1f / mMarginTopWhenMin
        setPercent(percent)
    }

    private fun setPercent(percent: Float) {
        if (mCurrentPercent == percent || percent > 1 || percent < 0) return
        mCurrentPercent = percent

        val layoutParams = frameDrag.layoutParams as LayoutParams
        layoutParams.topMargin = (mMarginTopWhenMin * mCurrentPercent).toInt()
        layoutParams.leftMargin = (mMarginEdgeWhenMin * mCurrentPercent).toInt()
        layoutParams.rightMargin = (mMarginEdgeWhenMin * mCurrentPercent).toInt()
        layoutParams.bottomMargin = (mMarginBottomWhenMin * mCurrentPercent).toInt()
        frameDrag.layoutParams = layoutParams

        val toolBarHeight = (mHeightWhenMaxDefault - (mHeightWhenMaxDefault - mHeightWhenMinDefault) * mCurrentPercent).toInt()
        toolbar.reHeight(toolBarHeight)

        val frameFistHeight = (mHeightWhenMax - (mHeightWhenMax - mHeightWhenMin) * mCurrentPercent).toInt()
        appbarLayout.reHeight(frameFistHeight)

    }

    private fun View.reHeight(height: Int) {
        resize(-1, height)
    }


}