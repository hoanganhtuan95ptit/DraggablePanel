package com.hoanganhtuan95ptit.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoanganhtuan95ptit.draggable.DraggablePanel
import com.hoanganhtuan95ptit.draggable.utils.toPx
import com.hoanganhtuan95ptit.example.fragment.BottomFragment
import com.hoanganhtuan95ptit.example.fragment.TopFragment
import kotlinx.android.synthetic.main.activity_custom.*
import kotlinx.android.synthetic.main.layout_bottom.*
import kotlin.math.max
import kotlin.math.min

class CustomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        draggablePanel.setDraggableListener(object : DraggablePanel.DraggableListener {
            override fun onChangeState(state: DraggablePanel.State) {
            }

            override fun onChangePercent(percent: Float) {
                alpha.alpha = 1 - percent
                shadow.alpha = percent
            }

        })

        supportFragmentManager.beginTransaction().add(R.id.frameTop, TopFragment()).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameBottom, BottomFragment()).commit()

        btnMax.setOnClickListener { draggablePanel.maximize() }
        btnMin.setOnClickListener { draggablePanel.minimize() }
        btnClose.setOnClickListener { draggablePanel.close() }
        btnSetHeightMax.setOnClickListener {
            var heightMax = 0
            if (etHeightMax.text.isNotEmpty()) {
                heightMax = etHeightMax.text.toString().toInt()
            }
            heightMax = max(heightMax, 200)
            heightMax = min(heightMax, 400)

            draggablePanel.setHeightMax(heightMax.toPx())
        }

    }
}
