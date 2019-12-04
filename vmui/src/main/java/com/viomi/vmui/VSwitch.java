package com.viomi.vmui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


public class VSwitch extends View {
    private static final long duration = 300;
    private ValueAnimator valueAnimator;
    float curRate;
    boolean check;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent event) {
            if (!isEnabled()) return false;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent event) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            setCheck(!isCheck());
            if (onSwitchStateChangeListener != null)
                onSwitchStateChangeListener.onSwitchStateChange(check);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e2.getX() > (with / 2)) {
                setCheck(true);
            } else {
                setCheck(false);
            }
            if (onSwitchStateChangeListener != null)
                onSwitchStateChangeListener.onSwitchStateChange(check);
            return true;
        }
    };

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        if (check) {
            valueAnimator.setFloatValues(curRate, 1F);
        } else {
            valueAnimator.setFloatValues(curRate, 0F);
        }
        valueAnimator.start();
        this.check = check;
    }


    private int tintColor, initTintColor;
    private int borderColor, initBorderColor, borderWith;

    GestureDetector gestureDetector;

    public interface OnSwitchStateChangeListener {
        void onSwitchStateChange(boolean isOn);
    }

    private OnSwitchStateChangeListener onSwitchStateChangeListener;

    public VSwitch(Context context) {
        this(context, null);
    }

    public VSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VSwitch);
        initTintColor = ta.getColor(R.styleable.VSwitch_tintColor, getResources().getColor(R.color.text_green));
        check = ta.getBoolean(R.styleable.VSwitch_check, false);
        ta.recycle();
        tintColor = Color.WHITE;
        initBorderColor = Color.parseColor("#E5E5E5");
        borderColor = initBorderColor;
        borderWith = dipToPx(2);
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        initAnimators();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;
        return gestureDetector.onTouchEvent(event);
    }

    private void initAnimators() {
        valueAnimator = ValueAnimator.ofFloat(curRate, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isAttachedToWindow)
                    return;
                float value = (float) animation.getAnimatedValue();
                curRate = value;
                if (borderRect != null)
                    cx = (int) (borderRect.left + radius + borderWith / 2 + (borderRect.right - borderRect.left - radius * 2 - borderWith) * value);
                tintColor = RGBColorTransform(value, Color.WHITE, initTintColor);
                borderColor = RGBColorTransform(value, initBorderColor, initTintColor);
                invalidate();
            }
        });

    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        this.onSwitchStateChangeListener = onSwitchStateChangeListener;
    }

    public OnSwitchStateChangeListener getOnSwitchStateChangeListener() {
        return this.onSwitchStateChangeListener;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        setCheck(check);
        if (isEnabled()) {
            setAlpha(1f);
        } else {
            setAlpha(0.5f);
        }
    }

    boolean isAttachedToWindow;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;

    }

    int with, height;
    RectF rect;
    RectF borderRect;
    Paint paint;
    int radius;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        with = w;
        height = h;
        rect = new RectF(0, 0, w, h);
        borderRect = new RectF(borderWith, borderWith / 2, w - borderWith, h - borderWith / 2);
        radius = height / 2 - borderWith;
        cx = (int) (borderRect.left + radius + borderWith / 2);
    }

    public void setTintColor(int tintColor) {
        this.initTintColor = tintColor;
        this.tintColor = tintColor;
    }

    public int getTintColor() {
        return this.initTintColor;
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setAlpha(1f);
        } else {
            setAlpha(0.5f);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawCircle(canvas);
    }

    int cx;

    private void drawCircle(Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);
        canvas.drawCircle(cx, height / 2, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#08000000"));
//        paint.setColor(Color.BLACK);
        paint.setShadowLayer(borderWith, 0, borderWith / 2, 0x20000000);
        canvas.drawCircle(cx, height / 2, radius, paint);
    }

    void drawBg(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tintColor);
        paint.setStrokeWidth(0);
        paint.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        canvas.drawRoundRect(rect, height / 2, height / 2, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWith);
        paint.setColor(borderColor);
        canvas.drawRoundRect(borderRect, height / 2, height / 2, paint);
    }

    int dipToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int RGBColorTransform(float progress, int fromColor, int toColor) {
        int or = (fromColor >> 16) & 0xFF;
        int og = (fromColor >> 8) & 0xFF;
        int ob = fromColor & 0xFF;

        int nr = (toColor >> 16) & 0xFF;
        int ng = (toColor >> 8) & 0xFF;
        int nb = toColor & 0xFF;

        int rGap = (int) ((float) (nr - or) * progress);
        int gGap = (int) ((float) (ng - og) * progress);
        int bGap = (int) ((float) (nb - ob) * progress);

        return 0xFF000000 | ((or + rGap) << 16) | ((og + gGap) << 8) | (ob + bGap);
    }
}
