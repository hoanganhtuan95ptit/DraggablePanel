package com.hoanganhtuan95ptit.example;

import android.widget.Toast;

public interface Constants extends Config {


    default void showToast(String message) {
        Toast.makeText(App.self(), message, Toast.LENGTH_LONG).show();
    }

    default void showToast(int message) {
        Toast.makeText(App.self(), message, Toast.LENGTH_LONG).show();
    }

}
