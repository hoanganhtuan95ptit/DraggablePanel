package com.hoanganhtuan95ptit.drag.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hoanganhtuan95ptit.drag.R;
import com.hoanganhtuan95ptit.drag.ui.fragment.DetailFragment;
import com.hoanganhtuan95ptit.drag.ui.play.PlayFragment;
import com.hoanganhtuan95ptit.drag.utils.Utils;
import com.hoanganhtuan95ptit.drag.view.DragFrame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DragFrame.OnDragListener {

    @BindView(R.id.v_background)
    View vBackground;
    @BindView(R.id.drag_frame)
    DragFrame dragFrame;

    @BindView(R.id.root_holder_video_home)
    CardView rootHolderVideoHome;
    @BindView(R.id.root_holder_video_normal)
    LinearLayout rootHolderVideoNormal;

    private float radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dragFrame.setBottomFragment(getSupportFragmentManager(), DetailFragment.newInstance());
        dragFrame.setTopFragment(getSupportFragmentManager(), PlayFragment.newInstance());
        dragFrame.setOnDragListener(this);
        dragFrame.close();
    }

    @Override
    public void onDragProcess(float percent) {
        vBackground.setAlpha(1 - percent);
    }

    @Override
    public void onMaximized() {
        play();
    }

    @Override
    public void onMinimized() {
    }

    @Override
    public void onClosed() {
    }

    @OnClick({R.id.root_holder_video_home, R.id.root_holder_video_normal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.root_holder_video_home:
                play(16f / 9);
                break;
            case R.id.root_holder_video_normal:
                play(1f);
                break;
            case R.id.root_holder_video_normal2:
                play(0.8f);
                break;
            case R.id.root_holder_video_normal3:
                play(0.8f);
                break;
        }
    }

    public void play(float r) {
        radio = r;
        if (dragFrame.isMaximized()) {
            play();
        } else {
            dragFrame.maximize();
        }
    }

    private void play() {
        dragFrame.postDelayed(delayRunnable, 200L);
    }

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            if (dragFrame == null) return;
            int heightNew = (int) Math.min(Utils.getScreenWidth() / radio, Utils.getScreenHeight() - Utils.getScreenHeight() / 3f);
            if (dragFrame.isMaximized()) {
                dragFrame.setHeight(heightNew);
            } else {
                dragFrame.setHeightWaiting(heightNew);
            }
        }
    };
}
