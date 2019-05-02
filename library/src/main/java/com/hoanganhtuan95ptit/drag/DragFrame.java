package com.hoanganhtuan95ptit.drag;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * Created by hoanganhtuan95ptit on 2/22/2019.
 */
public class DragFrame extends FrameLayout {

    private static final String TAG = "DragLayout";

    private static final float PERCENT_START_CHANGE_WIDTH = 0.9f;
    private static final float PERCENT_END_CHANGE_ALPHA = 0.7f;

    DragCard cardView;
    CoordinatorLayout coordinatorLayout;

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    RelativeLayout secondView;
    FrameLayout contentSecondView;
    View vAlpha;

    FrameLayout frameControl;
    FrameLayout frameVideo;
    TextView tvTitle;
    ImageView ivPlay;
    ImageView ivPause;
    ImageView ivClose;

    private ControlViewBehavior controlViewBehavior;
    private OnDragListener onDragListener;
    private State currentState;

    private boolean expand;

    private float velocityY;

    private int heightMinDefault;
    private int heightMaxDefault;

    private int heightMedNormal;
    private int heightWaiting;
    private int topNew;

    public DragFrame(@NonNull Context context) {
        this(context, null);
    }

    public DragFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFrame(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_drag, this);

        cardView = findViewById(R.id.card_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        secondView = findViewById(R.id.second_view);
        contentSecondView = findViewById(R.id.content_second_view);
        vAlpha = findViewById(R.id.v_alpha);
        frameControl = findViewById(R.id.frame_control);
        frameVideo = findViewById(R.id.frame_video);
        tvTitle = findViewById(R.id.tv_title);
        ivPlay = findViewById(R.id.iv_play);
        ivPause = findViewById(R.id.iv_pause);
        ivClose = findViewById(R.id.iv_close);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragFrame);
            radius = typedArray.getDimensionPixelSize(R.styleable.DragFrame_df_ratio, (int) dp2px(context, 8));
            edge = typedArray.getDimensionPixelSize(R.styleable.DragFrame_df_paddingEdge, (int) dp2px(context, 8));
            bottom = typedArray.getDimensionPixelSize(R.styleable.DragFrame_df_paddingBottom, (int) dp2px(context, 64));
            typedArray.recycle();
        } else {
            radius = (int) dp2px(context, 8);
            edge = (int) dp2px(context, 8);
            bottom = (int) dp2px(context, 64);
        }

        heightMinDefault = (int) dp2px(context, 80);
        heightMaxDefault = (int) (getScreenWidth(context) * 9f / 16);

        setVisibility(GONE);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) frameControl.getLayoutParams();
        params.setBehavior(controlViewBehavior = new ControlViewBehavior());
        frameControl.requestLayout();

        appBarLayout.addOnOffsetChangedListener(provideOnOffsetChangedListener());
        cardView.setOnTouchListener(getOnTouchListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int width = getScreenWidth(getContext());
        int height = getScreenHeight(getContext()) - getStatusBarHeight(getContext());

        widthMax = width;
        heightMax = heightMaxDefault;
        heightWaiting = heightMax;

        heightMin = heightMinDefault;
        widthMin = heightMin * 22 / 9;

        topMax = 0;
        topNew = topMax;
        topMin = height - heightMin - bottom;

        heightMed = (int) (height - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax);
        widthMed = (int) (width - PERCENT_START_CHANGE_WIDTH * edge);

        heightMedNormal = heightMed;

        refreshLayoutFirstView(heightMax);
        refreshLayoutContentFirstView(heightMax);

        refreshLayoutToolBar();
        refreshLayoutSecondView(height - heightMaxDefault, width);

        if (currentState == State.MAXIMIZE) {
            topNew = topMax;
            currentState = State.MAXIMIZE;
        } else if (currentState == State.MINIMIZE) {
            topNew = topMin;
            currentState = State.MINIMIZE;
        } else {
            topNew = topMin;
            currentState = State.CLOSE;
            startTranslationAnimationHide();
        }
        refresh();
        setVisibility(VISIBLE);
    }

    private VelocityTracker velocityTracker;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
            case MotionEvent.ACTION_CANCEL:
                velocityY = velocityTracker.getYVelocity();
                velocityTracker.recycle();
                velocityTracker = null;
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(ev);
                velocityTracker.computeCurrentVelocity(1000);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * funtion public
     */
    public void setTopFragment(FragmentManager supportFragmentManager, Fragment fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_video, fragment)
                .commitAllowingStateLoss();
    }

    public void setBottomFragment(FragmentManager supportFragmentManager, Fragment fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content_second_view, fragment)
                .commitAllowingStateLoss();
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public FrameLayout getFrameVideo() {
        return frameVideo;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public ImageView getIvPlay() {
        return ivPlay;
    }

    public ImageView getIvPause() {
        return ivPause;
    }

    public ImageView getIvClose() {
        return ivClose;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public FrameLayout getContentSecondView() {
        return contentSecondView;
    }

    public View getvAlpha() {
        return vAlpha;
    }

    public RelativeLayout getSecondView() {
        return secondView;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public DragCard getCardView() {
        return cardView;
    }

    public FrameLayout getFrameControl() {
        return frameControl;
    }

    public float getPercent() {
        return currentPercent;
    }

    public boolean isMinimized() {
        return getTranslationY() == 0 && currentState == State.MINIMIZE && getVisibility() == VISIBLE;
    }

    public boolean isMaximized() {
        return getTranslationY() == 0 && currentState == State.MAXIMIZE && getVisibility() == VISIBLE;
    }

    public boolean isClosed() {
        return currentState == State.CLOSE || getVisibility() != VISIBLE || getTranslationY() != 0;
    }

    public void setHeightWaiting(int heightWaiting) {
        this.expand = true;
        this.heightWaiting = heightWaiting;
    }

    public void setHeight(int height) {
        setHeightWaiting(height);
        startSpringAnimation(topMax);
    }

    public void maximize() {
        post(() -> {
            if (isMaximized()) return;

            if (getHeight() == 0) {
                currentState = State.MAXIMIZE;
                return;
            }

            startTranslationAnimationShow();
            setHeightWaiting(heightMax);
            startSpringAnimation(topMax);
        });
    }

    public void minimize() {
        post(() -> {
            if (isMinimized()) return;

            if (getHeight() == 0) {
                currentState = State.MINIMIZE;
                return;
            }

            startTranslationAnimationShow();
            startSpringAnimation(topMin);
        });
    }

    public void close() {
        post(() -> {
            if (isClosed()) return;

            if (getHeight() == 0) {
                currentState = State.CLOSE;
                return;
            }

            startSpringAnimation(topMin);
            startTranslationAnimationHide();
        });
    }

    /**
     * handler
     */
    private void onEndResizeAnimation() {
        onEndDrag();
        if (expand) {
            expand = false;
            appBarLayout.setExpanded(true, true);
        }
    }

    private void onEndDrag() {
        State state;
        if (getTranslationY() != 0) {
            state = State.CLOSE;
        } else if (currentPercent >= 0.5f) {
            state = State.MINIMIZE;
        } else {
            state = State.MAXIMIZE;
        }

        if (state == currentState) return;
        currentState = state;

        if (onDragListener == null || getVisibility() != VISIBLE) return;
        if (currentState == State.CLOSE) {
            onDragListener.onClosed();
        } else if (currentState == State.MINIMIZE) {
            onDragListener.onMinimized();
        } else {
            onDragListener.onMaximized();
        }
    }

    private void onRefresh() {
        if (onDragListener == null) return;
        onDragListener.onDragProcess(currentPercent);
    }

    /**
     * animaton
     */
    private void startResizeAnimation() {
        OnAnimationListener onAnimationListener = new OnAnimationListener() {
            @Override
            public void onUpdate(int value) {
                refreshLayoutFirstView(value);
                refreshLayoutContentFirstView(value);
                refreshLayoutSecondView(getHeight() - value);
            }

            @Override
            public void onEnd() {
                onEndResizeAnimation();
            }
        };
        if (appBarLayout.getHeight() == heightMax) {
            onAnimationListener.onEnd();
        } else {
            resizeAnimation(appBarLayout.getHeight(), heightMax, onAnimationListener);
        }
    }

    protected void startTranslationAnimationHide() {
        startTranslationAnimation((int) (heightMinDefault + bottom), null);
    }

    protected void startTranslationAnimationShow() {
        startTranslationAnimation(0, new OnAnimationListener() {
            @Override
            public void onEnd() {
                if (getVisibility() != VISIBLE) {
                    setVisibility(VISIBLE);
                }
            }
        });
    }

    protected void startTranslationAnimation(int translation, @Nullable OnAnimationListener onAnimationListener) {
        translationAnimation(this, translation, new OnAnimationListener() {
            @Override
            public void onEnd() {
                if (onAnimationListener != null) {
                    onAnimationListener.onEnd();
                }
                onEndDrag();
            }
        });
    }

    protected void startSpringAnimation() {
        if (cardViewParams == null) {
            cardViewParams = (LayoutParams) cardView.getLayoutParams();
        }
        topNew = cardViewParams.topMargin;

        if (topNew < topMax) topNew = topMax;
        else if (topNew > topMin) topNew = topMin;

        int finalPosition;
        if (Math.abs(velocityY) < 200) {
            finalPosition = topNew - topMax > topMin - topNew ? topMin : topMax;
        } else {
            finalPosition = velocityY < 0 ? topMax : topMin;
        }

        startSpringAnimation(finalPosition);
    }

    private void startSpringAnimation(int finalPosition) {
        OnAnimationListener onAnimationListener = new OnAnimationListener() {
            @Override
            public void onUpdate(int value) {
                topNew = value;
                refresh();
            }

            @Override
            public void onEnd() {
                if (currentPercent == 0 && (heightWaiting != heightMax || heightMax != appBarLayout.getHeight())) {
                    heightMax = heightWaiting;
                    startResizeAnimation();
                } else {
                    onEndResizeAnimation();
                }
            }
        };
        if (finalPosition == topNew) {
            onAnimationListener.onEnd();
        } else {
            springAnimation(velocityY, topMax, topMin, topNew, finalPosition, onAnimationListener);
        }
    }

    /**
     * listener
     */
    private DragCard.OnTouchListener getOnTouchListener() {
        return new DragCard.OnTouchListener() {

            private float downY;

            private boolean scrolling;
            private boolean firstViewDown;

            private int deltaY;

            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = ev.getRawY();

                        onTouchEvent(ev);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        scrolling = false;
                        firstViewDown = false;
//
//                        if (currentPercent == 1
//                                && calculateDistance(ev, downY) < getScaledTouchSlop()
//                                && isViewUnder(appBarLayout, ev)
//                                && System.currentTimeMillis() - timeDown < 1000
//                                && !isViewUnder(ivClose, ev)
//                                && !isViewUnder(ivPlay, ev)
//                                && !isViewUnder(ivPause, ev)) {
//                            maximize();
//                        }
//
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (scrolling) {
                            break;
                        }
                        int calculateDiff = calculateDistance(ev, downY);
                        int scaledTouchSlop = getScaledTouchSlop(getContext());
                        if (calculateDiff > scaledTouchSlop && firstViewDown) {
                            scrolling = true;
                            break;
                        }
                }
                return scrolling;
            }

//            private long timeDown;

            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                int motionY = (int) ev.getRawY();
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        timeDown = System.currentTimeMillis();
                        firstViewDown = isViewUnder(appBarLayout, ev);
                        if (cardViewParams == null) {
                            cardViewParams = (LayoutParams) cardView.getLayoutParams();
                        }
                        deltaY = motionY - cardViewParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        scrolling = false;
                        firstViewDown = false;
                        startSpringAnimation();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        topNew = motionY - deltaY;
                        refresh();
                        break;
                }
                return firstViewDown;
            }
        };
    }

    protected int topMax;
    protected int topMin;

    protected float currentPercent = -1;

    protected int edge;
    protected int bottom;
    protected int radius;

    protected int widthMax;
    protected int heightMax;

    protected int widthMin;
    protected int heightMin;

    protected int widthMed;
    protected int heightMed;

    private AppBarLayout.BaseOnOffsetChangedListener provideOnOffsetChangedListener() {
        return (AppBarLayout.OnOffsetChangedListener) (appBarLayout, i) -> {
            i = Math.abs(i);
            if (currentPercent == 0 && i > 0) {
                heightMin = heightMinDefault + i;
                heightMed = (int) (getMeasuredHeight() - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax) + i;
            } else if (currentPercent == 0) {
                heightMin = heightMinDefault;
                heightMed = (int) (getMeasuredHeight() - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax);
            }
        };
    }

    /**
     * refresh ui
     */

    private void refresh() {
        if (topNew < topMax) topNew = topMax;
        else if (topNew > topMin) topNew = topMin;

        float percent = (topNew - topMax) * 1f / (topMin - topMax);
        percent = Math.abs(percent);

        if (currentPercent == percent || percent > 1) return;
        currentPercent = percent;

        onRefresh();

        controlViewBehavior.behave = currentPercent == 0;

        vAlpha.setAlpha(currentPercent / PERCENT_END_CHANGE_ALPHA);

        refreshLayoutCard();
        refreshLayoutToolBar();
        refreshLayoutFirstView();
        refreshLayoutFrameVideo();
    }

    private LayoutParams cardViewParams;

    private void refreshLayoutCard() {
        int currentEdge = (int) (edge * currentPercent);
        int currentBottom = (int) (bottom * currentPercent);
        if (cardViewParams == null) {
            cardViewParams = (LayoutParams) cardView.getLayoutParams();
        }
        cardViewParams.topMargin = topNew;
        cardViewParams.rightMargin = currentEdge;
        cardViewParams.leftMargin = currentEdge;
        cardViewParams.bottomMargin = currentBottom;
        cardView.setRadius(radius * currentPercent);
        cardView.setLayoutParams(cardViewParams);
    }

    private ViewGroup.LayoutParams frameVideoParams;

    private void refreshLayoutFrameVideo() {
        float heightMax = controlViewBehavior.Y;
        float heightMed = heightMedNormal/*(int) (getHeight() - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax)*/;

        float height;
        if (currentPercent < PERCENT_START_CHANGE_WIDTH) {
            height = heightMax - (heightMax - heightMed) * currentPercent / PERCENT_START_CHANGE_WIDTH;
        } else {
            height = heightMed - (heightMed - heightMinDefault) * (currentPercent - PERCENT_START_CHANGE_WIDTH) / (1 - PERCENT_START_CHANGE_WIDTH);
        }
        controlViewBehavior.refreshUiControl(frameControl, height);

        int width;
        if (currentPercent < PERCENT_START_CHANGE_WIDTH) {
            width = (int) (widthMax - (widthMax - widthMed) * currentPercent);
        } else {
            width = (int) (widthMed - (widthMed - widthMin) * (currentPercent - PERCENT_START_CHANGE_WIDTH) / (1 - PERCENT_START_CHANGE_WIDTH));
        }

        if (frameVideoParams == null) {
            frameVideoParams = frameVideo.getLayoutParams();
        }
        if (frameVideoParams.width != width) {
            frameVideoParams.width = width;
            frameVideo.setLayoutParams(frameVideoParams);
        }
    }

    private ViewGroup.LayoutParams appBarLayoutParams;

    private void refreshLayoutFirstView() {
        int height;
        if (currentPercent < PERCENT_START_CHANGE_WIDTH) {
            height = (int) (heightMax - (heightMax - heightMed) * currentPercent / PERCENT_START_CHANGE_WIDTH);
        } else {
            height = (int) (heightMed - (heightMed - heightMin) * (currentPercent - PERCENT_START_CHANGE_WIDTH) / (1 - PERCENT_START_CHANGE_WIDTH));
        }
        refreshLayoutFirstView(height);
    }

    private void refreshLayoutFirstView(int height) {
        if (appBarLayoutParams == null) {
            appBarLayoutParams = appBarLayout.getLayoutParams();
        }
        if (appBarLayoutParams.height != height) {
            appBarLayoutParams.height = height;
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
    }

    private ViewGroup.LayoutParams collapsingToolbarLayoutParams;

    private void refreshLayoutContentFirstView(int height) {
        if (collapsingToolbarLayoutParams == null) {
            collapsingToolbarLayoutParams = collapsingToolbarLayout.getLayoutParams();
        }
        if (collapsingToolbarLayoutParams.height != height) {
            collapsingToolbarLayoutParams.height = height;
            collapsingToolbarLayout.setLayoutParams(collapsingToolbarLayoutParams);
        }
    }

    private ViewGroup.LayoutParams toolbarParams;

    private void refreshLayoutToolBar() {
        int heightMax = heightMaxDefault;
        int heightMed = heightMedNormal/*(int) (getHeight() - PERCENT_START_CHANGE_WIDTH * bottom - PERCENT_START_CHANGE_WIDTH * (topMin - topMax) - topMax)*/;

        int height;
        if (currentPercent < PERCENT_START_CHANGE_WIDTH) {
            height = (int) (heightMax - (heightMax - heightMed) * currentPercent / PERCENT_START_CHANGE_WIDTH);
        } else {
            height = (int) (heightMed - (heightMed - heightMinDefault) * (currentPercent - PERCENT_START_CHANGE_WIDTH) / (1 - PERCENT_START_CHANGE_WIDTH));
        }
        refreshLayoutToolBar(height);
    }

    private void refreshLayoutToolBar(int height) {
        if (toolbarParams == null) {
            toolbarParams = toolbar.getLayoutParams();
        }
        if (toolbarParams.height != height) {
            toolbarParams.height = height;
            toolbar.setLayoutParams(toolbarParams);
        }
    }

    private ViewGroup.LayoutParams secondViewParams;

    private void refreshLayoutSecondView() {
        refreshLayoutSecondView(getHeight() - heightMaxDefault);
    }

    private void refreshLayoutSecondView(int height) {
        refreshLayoutSecondView(Math.max(height, getHeight() - heightMaxDefault), getWidth());
    }

    private void refreshLayoutSecondView(int height, int width) {
        if (secondViewParams == null) {
            secondViewParams = secondView.getLayoutParams();
        }
        if (secondViewParams.height != height || secondViewParams.width != width) {
            secondViewParams.height = height;
            secondViewParams.width = width;
            secondView.setLayoutParams(secondViewParams);
        }
    }

    /**
     * funtion static
     */

    private static void springAnimation(float velocityY, int minValue, int maxValue, int startValue, int finalPosition, @Nullable OnAnimationListener onAnimationListener) {
        SpringForce springX = new SpringForce(finalPosition);
        springX.setDampingRatio(0.7f);
        springX.setStiffness(300f);

        SpringAnimation springAnimation = new SpringAnimation(new FloatValueHolder());
        springAnimation.setStartVelocity(velocityY)
                .setMinValue(minValue)
                .setMaxValue(maxValue)
                .setStartValue(startValue)
                .setSpring(springX)
                .setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_PIXELS)
                .addUpdateListener((dynamicAnimation, value, velocity) -> {
                    if (onAnimationListener != null) {
                        onAnimationListener.onUpdate((int) value);
                    }
                })
                .addEndListener((dynamicAnimation, b, value, velocity) -> {
                    if (onAnimationListener != null) {
                        onAnimationListener.onEnd();
                    }
                })
                .start();
    }

    private static void translationAnimation(View view, int translation, @Nullable OnAnimationListener onAnimationListener) {
        view.animate().translationY(translation).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationListener != null) {
                    onAnimationListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private static void resizeAnimation(int from, int to, @Nullable OnAnimationListener onAnimationListener) {
        if (from == to) {
            if (onAnimationListener != null) {
                onAnimationListener.onEnd();
            }
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.addUpdateListener(animation -> {
            if (onAnimationListener != null) {
                onAnimationListener.onUpdate((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationListener != null) {
                    onAnimationListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration((long) 300);
        valueAnimator.start();
    }

    private static int scaledTouchSlop;

    private static int getScaledTouchSlop(Context context) {
        if (scaledTouchSlop == 0) {
            scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }
        return scaledTouchSlop;
    }

    private static boolean isViewUnder(View view, MotionEvent ev) {
        if (view == null) {
            return false;
        } else {
            return ev.getX() >= view.getLeft() && ev.getX() < view.getRight() && ev.getY() >= view.getTop() && ev.getY() < view.getBottom();
        }
    }

    private static int calculateDistance(MotionEvent event, float downY) {
        return (int) Math.abs(event.getRawY() - downY);
    }

    private static class ControlViewBehavior extends CoordinatorLayout.Behavior<View> {

        private boolean behave = true;
        private float Y;

        private View secondView;

        ControlViewBehavior() {
        }

        @Override
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View fab, @NonNull View dependency) {
            return behave;
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
            if (!behave) return false;
            if (secondView == null) {
                secondView = parent.findViewById(R.id.second_view);
            }
            Y = Math.abs(secondView.getY());
            refreshUiControl(child, Y);
            return false;
        }


        private ViewGroup.LayoutParams childParams;

        private void refreshUiControl(View child, float y) {
            int height = (int) (y);

            if (childParams == null) {
                childParams = child.getLayoutParams();
            }
            if (childParams.height != height) {
                childParams.height = height;
                child.setLayoutParams(childParams);
            }
        }

    }

    public interface OnDragListener {

        default void onDragProcess(float percent) {

        }

        default void onMaximized() {

        }

        default void onMinimized() {

        }

        default void onClosed() {

        }

    }

    private interface OnAnimationListener {
        default void onUpdate(int value) {

        }

        default void onEnd() {

        }
    }

    private enum State {
        MAXIMIZE, MINIMIZE, CLOSE
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightId > 0) {
            return resources.getDimensionPixelSize(statusBarHeightId);
        } else {
            return 0;
        }
    }

    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }


}
