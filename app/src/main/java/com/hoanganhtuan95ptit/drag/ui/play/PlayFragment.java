package com.hoanganhtuan95ptit.drag.ui.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.drag.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayFragment extends Fragment {

    public static PlayFragment newInstance() {

        Bundle args = new Bundle();

        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        return view;
    }
}
