package com.hoanganhtuan95ptit.widget

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.hoanganhtuan95ptit.utils.resize

class DragBehavior(private val frameSecond: View) : CoordinatorLayout.Behavior<View>() {

    override fun layoutDependsOn(parent: CoordinatorLayout, fab: View, dependency: View): Boolean {
        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.resize(-1, frameSecond.y.toInt())
        return true
    }

}