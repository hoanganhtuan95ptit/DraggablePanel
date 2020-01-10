package com.hoanganhtuan95ptit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoanganhtuan95ptit.utils.toPx
import kotlinx.android.synthetic.main.activity_test.*

class DraggableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        draggablePanel.setHeightMax(400.toPx())

        btnMax.setOnClickListener { draggablePanel.maximize() }
        btnMin.setOnClickListener { draggablePanel.minimize() }
        btnClose.setOnClickListener { draggablePanel.close() }

        btnSetHeight.setOnClickListener { draggablePanel.setHeightMax(300.toPx()) }
    }
}