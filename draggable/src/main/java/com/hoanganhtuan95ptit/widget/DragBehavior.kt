package com.hoanganhtuan95ptit.widget

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.hoanganhtuan95ptit.R
import kotlin.math.abs

class DragBehavior : CoordinatorLayout.Behavior<View>() {

    var behave = true

    var Y = 0f

    var frameSecond: View? = null

    fun ControlViewBehavior() {}

    override fun layoutDependsOn(parent: CoordinatorLayout, fab: View, dependency: View): Boolean {
        return behave
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (!behave) return false
        if (frameSecond == null) {
//            frameSecond = parent.findViewById(R.id.frameSecond)
        }
        Y = abs(frameSecond!!.y)
        refreshUiControl(child, Y)
        return false
    }


    var childParams: ViewGroup.LayoutParams? = null

    fun refreshUiControl(child: View, y: Float) {
        val height = y.toInt()
        if (childParams == null) {
            childParams = child.layoutParams
        }
        if (childParams!!.height != height) {
            childParams!!.height = height
            child.layoutParams = childParams
        }
    }

}