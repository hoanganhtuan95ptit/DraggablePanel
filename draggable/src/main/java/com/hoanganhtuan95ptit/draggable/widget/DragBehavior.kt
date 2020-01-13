package com.hoanganhtuan95ptit.draggable.widget

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.hoanganhtuan95ptit.draggable.utils.resize

class DragBehavior(private val frameSecond: View) : CoordinatorLayout.Behavior<View>() {

    override fun layoutDependsOn(parent: CoordinatorLayout, fab: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if(dependency is AppBarLayout) {
            child.resize(-1, frameSecond.y.toInt())
        }
        return true
    }

}