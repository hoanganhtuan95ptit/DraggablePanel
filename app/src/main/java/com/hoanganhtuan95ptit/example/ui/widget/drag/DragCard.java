//package com.hoanganhtuan95ptit.example.ui.widget.drag;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//
///**
// * Created by hoanganhtuan95ptit on 2/22/2019.
// */
//public class DragCard extends CardView {
//
//    @Nullable
//    private OnTouchListener onTouchListener;
//
//    public DragCard(Context context) {
//        super(context);
//    }
//
//    public DragCard(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public DragCard(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public void setOnTouchListener(@Nullable OnTouchListener onTouchListener) {
//        this.onTouchListener = onTouchListener;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (onTouchListener != null) return onTouchListener.onInterceptTouchEvent(ev);
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (onTouchListener != null) return onTouchListener.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
//
//    public interface OnTouchListener {
//        default boolean onInterceptTouchEvent(MotionEvent ev) {
//            return false;
//        }
//
//        default boolean onTouchEvent(MotionEvent ev) {
//            return false;
//        }
//    }
//}
