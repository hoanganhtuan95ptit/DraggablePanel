package com.hoanganhtuan95ptit.widget

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.hoanganhtuan95ptit.R
import com.hoanganhtuan95ptit.utils.resize

class DragBehavior : CoordinatorLayout.Behavior<View>() {

    override fun layoutDependsOn(parent: CoordinatorLayout, fab: View, dependency: View): Boolean {
        return true
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.resize(-1, parent.findViewById<View>(R.id.frameSecond).y.toInt())
//        val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
//        layoutParams.topMargin = 0
//        layoutParams.bottomMargin =  parent.findViewById<View>(R.id.frameSecond).height
        return super.onDependentViewChanged(parent, child, dependency)
    }

}