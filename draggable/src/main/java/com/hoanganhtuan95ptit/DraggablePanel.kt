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
import com.hoanganhtuan95ptit.utils.toPx
import com.hoanganhtuan95ptit.widget.DragBehavior
import com.hoanganhtuan95ptit.widget.DragFrame
import kotlinx.android.synthetic.main.layout_draggable_panel.view.*
import kotlin.math.abs


class DraggablePanel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {

        private const val PERCENT_START_CHANGE_WIDTH = 0.9f
        private const val PERCENT_END_CHANGE_ALPHA = 0.7f

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

//    private lateinit var dragBehavior: DragBehavior
//
    private var velocityTracker: VelocityTracker? = null
    private var velocityY = 0f
//
//    private val expand = false
//
//    private val heightMinDefault = 0
//    private val heightMaxDefault = 0
//
//    private var heightMedNormal = 0
//    private var heightWaiting = 0
//
//    private var currentPercent = -1f
//
//    private var edge = 0
//    //    private var bottom = 0
//    private var radius = 0
//
//    private var ratio: Float = 16f / 9
//
//    private var topNew = 0
//    private var topMax = 0
//    private var topMin = 0
//
//    private var widthMax = 0
//    private var heightMax = 0
//
//    private var widthMin = 0
//    private var heightMin = 0
//
//    private var widthMed = 0
//    private var heightMed = 0

    init {
        inflate(context, R.layout.layout_draggable_panel, this)

//        appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
//            val i = abs(verticalOffset)
//            if (currentPercent == 0f && i > 0) {
//                heightMin = heightMinDefault + i
//                heightMed = (measuredHeight - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax).toInt() + i
//            } else if (currentPercent == 0f) {
//                heightMin = heightMinDefault
//                heightMed = (measuredHeight - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax).toInt()
//            }
//        })

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
                        deltaY = motionY - (frameDrag.layoutParams as LayoutParams).topMargin
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        scrolling = false
                        firstViewDown = false
//                        handleUp()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        handleMove(motionY)
                    }
                }
                return firstViewDown
            }

//            private fun handleUp() {
//                topNew = (frameDrag.layoutParams as LayoutParams).topMargin
//
//                val topMarginFinal: Int = if (abs(velocityY) < 200) {
//                    if (topNew - topMax > topMin - topNew) topMin else topMax
//                } else {
//                    if (velocityY < 0) topMax else topMin
//                }
//
//                velocityY.springAnimation(topMax.toFloat(), topMin.toFloat(), topNew.toFloat(), topMarginFinal.toFloat(), { setPercent(getPercent(it.toInt())) }, {
//                    // todo
//                })
//            }

            private fun handleMove(motionY: Int) {
                setMarginTop(motionY - deltaY)
//                println("handleMove: " + motionY + "   " + deltaY)
//                setPercent(getPercent())
            }
//
//            private fun getPercent(top: Int): Float {
//
//                var percent = (topNew - topMax) * 1f / (topMin - topMax)
//                percent = abs(percent)
//
//                println("getPercent: " + topNew + "   " + percent)
//                return percent
//            }
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
//        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                viewTreeObserver.removeOnGlobalLayoutListener(this)
//
//                dragBehavior = DragBehavior()
//                val params = frameFirst.layoutParams as CoordinatorLayout.LayoutParams
//                params.behavior = dragBehavior
//
//                widthMax = width
//                heightMax = (widthMax / ratio).toInt()
//
//                heightMin = 80.toPx()
//                widthMin = heightMin * 22 / 9
//
//                topMax = 0
//                topNew = topMax
//                topMin = height - heightMin - bottom
//
//                heightMed = (height - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax).toInt()
//                widthMed = (width - PERCENT_START_CHANGE_WIDTH * edge).toInt()
//                heightMedNormal = heightMed
//
//                setPercent(0f)
//            }
//        })
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
//        topNew = top
//        if (topNew < topMax) topNew = topMax else if (topNew > topMin) topNew = topMin

        val layoutParams = frameDrag.layoutParams as LayoutParams
        layoutParams.topMargin = top
        frameDrag.layoutParams = layoutParams
    }

//    private fun setPercent(percent: Float) {
//        if (currentPercent == percent || percent > 1 || percent < 0) return
//        currentPercent = percent
//
//        println(currentPercent)
//
//        val height = if (currentPercent < PERCENT_START_CHANGE_WIDTH) {
//            (heightMax - (heightMax - heightMed) * currentPercent / PERCENT_START_CHANGE_WIDTH).toInt()
//        } else {
//            (heightMed - (heightMed - heightMin) * (currentPercent - PERCENT_START_CHANGE_WIDTH) / (1 - PERCENT_START_CHANGE_WIDTH)).toInt()
//        }
//
//
//        toolbar.resize(-1, height)
//        frameFirst.resize(-1, height)
//        appbarLayout.resize(-1, height)
//    }
//
//    private fun refreshLayoutFirstView(height: Int) {
//    }
//
//    private fun springAnimation(velocityY: Float, minValue: Int, maxValue: Int, startValue: Int, finalPosition: Int,
//                                onUpdate: (Float) -> Unit,
//                                onEnd: () -> Unit) {
//        val springX = SpringForce(finalPosition.toFloat())
//        springX.dampingRatio = 0.7f
//        springX.stiffness = 300f
//        val springAnimation = SpringAnimation(FloatValueHolder())
//        springAnimation.setStartVelocity(velocityY)
//                .setMinValue(minValue.toFloat())
//                .setMaxValue(maxValue.toFloat())
//                .setStartValue(startValue.toFloat())
//                .setSpring(springX)
//                .setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS)
//                .addUpdateListener { _: DynamicAnimation<*>?, value: Float, _: Float ->
//                    onUpdate(value)
//                }
//                .addEndListener { _: DynamicAnimation<*>?, _: Boolean, _: Float, _: Float ->
//                    onEnd()
//                }
//                .start()
//    }
}