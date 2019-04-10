package com.hoanganhtuan95ptit.drag.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hoanganhtuan95ptit.drag.R;
import com.hoanganhtuan95ptit.drag.ui.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailFragment extends Fragment {

    @BindView(R.id.holder_detail_video)
    LinearLayout holderDetailVideo;
    @BindView(R.id.holder_detail_video1)
    LinearLayout holderDetailVideo1;
    @BindView(R.id.holder_detail_video2)
    LinearLayout holderDetailVideo2;
    @BindView(R.id.holder_detail_video3)
    LinearLayout holderDetailVideo3;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.holder_detail_video, R.id.holder_detail_video1, R.id.holder_detail_video2, R.id.holder_detail_video3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.holder_detail_video:
                ((MainActivity) getActivity()).play(1f);
                break;
            case R.id.holder_detail_video1:
                ((MainActivity) getActivity()).play(0.9f);
                break;
            case R.id.holder_detail_video2:
                ((MainActivity) getActivity()).play(0.6f);
                break;
            case R.id.holder_detail_video3:
                ((MainActivity) getActivity()).play(0.7f);
                break;
        }
    }
}
