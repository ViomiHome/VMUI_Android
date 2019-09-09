package com.viomi.vmui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/***
 * An indicator of progress, similar to Android's ProgressBar. Can be used in
 * 'spin mode' or 'increment mode'
 */
public class VCircleProgress extends View {

    public final static int CHANGE_TYPE_INCREASE = 0;//����
    public final static int CHANGE_TYPE_DECREASE = 1;//�ݼ�
    // Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int center_x = 0;
    private int center_y = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private int unitTextSize = 20;

    // Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    // Colors (with defaults)
    private int barColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    // Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint unitTextPaint = new Paint();

    // Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();

    // Animation
    // The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    // The number of milliseconds to wait inbetween each draw
    private int delayMillis = 0;

    int progress = 0;
    boolean isSpinning = false;
    private String text = "";


    private String uint = "%";//��λ


    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable and so
         * spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                progress += spinSpeed;
                if (progress > 360) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
            // super.handleMessage(msg);
        }
    };

    /**
     * The constructor for the VCircleProgress
     *
     * @param context
     * @param attrs
     */
    public VCircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.VCircleProgress));
    }

    // ----------------------------------
    // Setting up stuff
    // ----------------------------------

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of
     * the view, because this method is called after measuring the dimensions of
     * MATCH_PARENT & WRAP_CONTENT. Use this dimensions to setup the bounds and
     * paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;
        center_x = layout_width / 2;
        center_y = layout_height / 2;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
//        textPaint.setTextAlign(Paint.Align.CENTER);

        unitTextPaint.setColor(textColor);
        unitTextPaint.setStyle(Style.FILL);
        unitTextPaint.setAntiAlias(true);
        unitTextPaint.setTextSize(unitTextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to steup the circle
        int minValue = Math.min(layout_width, layout_height);

        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        rectBounds = new RectF(paddingLeft, paddingTop,
                this.getLayoutParams().width - paddingRight, this.getLayoutParams().height
                - paddingBottom);

        circleBounds = new RectF(paddingLeft + barWidth, paddingTop + barWidth,
                this.getLayoutParams().width - paddingRight - barWidth,
                this.getLayoutParams().height - paddingBottom - barWidth);

        fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        if (a == null)
            return;
        barWidth = (int) a.getDimension(R.styleable.VCircleProgress_barWidth, barWidth);

        rimWidth = (int) a.getDimension(R.styleable.VCircleProgress_rimWidth, rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.VCircleProgress_spinSpeed, spinSpeed);

        delayMillis = (int) a.getInteger(R.styleable.VCircleProgress_delayMillis, delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.VCircleProgress_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.VCircleProgress_barLength, barLength);

        textSize = (int) a.getDimension(R.styleable.VCircleProgress_textSize, textSize);
        unitTextSize = (int) a.getDimension(R.styleable.VCircleProgress_unitTextSize, unitTextSize);
        textColor = (int) a.getColor(R.styleable.VCircleProgress_textColor, textColor);

        // if the text is empty , so ignore it
        if (a.hasValue(R.styleable.VCircleProgress_text)) {
            setText(a.getString(R.styleable.VCircleProgress_text));
        }

        rimColor = (int) a.getColor(R.styleable.VCircleProgress_rimColor, rimColor);

        circleColor = (int) a.getColor(R.styleable.VCircleProgress_circleColor, circleColor);

        // Recycle
        a.recycle();
    }

    // ----------------------------------
    // Animation stuff
    // ----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the rim
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        // Draw the bar
        if (isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false, barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }
        // Draw the inner circle
        canvas.drawCircle((circleBounds.width() / 2) + rimWidth + paddingLeft,
                (circleBounds.height() / 2) + rimWidth + paddingTop, circleRadius, circlePaint);

        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();
        float horizontalTextOffset = textPaint.measureText(text) / 2;
        float x = center_x - horizontalTextOffset;
        float y = center_y + verticalTextOffset;
        canvas.drawText(text, x, y, textPaint);

        canvas.drawText(uint
                , center_x + horizontalTextOffset
                        + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())
                , y
                , unitTextPaint);

//        // Draw the text
//        float offset = unitTextPaint.measureText(uint) / 2;
//        canvas.drawText(text, this.getWidth() / 2 - offset, this.getHeight() / 2 + (textSize / 2), textPaint);
//        offset = textPaint.measureText(text) / 2;
//        canvas.drawText(uint, this.getWidth() / 2 + offset, this.getHeight() / 2 + (textSize / 2), unitTextPaint);

    }

    /**
     * Reset the count (in increment mode)
     */
    public void resetCount() {
        progress = 0;
        setText("0");
        invalidate();
    }

    /**
     * Turn off spin mode
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }

    /**
     * Puts the view on spin mode
     */
    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Increment the progress by 1 (of 360)
     */
    public void incrementProgress() {
        isSpinning = false;
        progress++;
        setText(Math.round(((float) progress / 360) * 100) + "");
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Set the progress to a specific value
     *
     * @param type ������ݼ� {@link #CHANGE_TYPE_INCREASE,#CHANGE_TYPE_DECREASE}
     */
    public void setProgress(int i, int type) {
        if (type == CHANGE_TYPE_INCREASE) {
            setText("" + i);
        } else {
            setText("" + (100 - i));
        }
        isSpinning = false;
        progress = (int) ((((float) i) * 360) / 100);
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Set the progress to a specific value
     */
    public void setProgress(int i) {

        setText("" + i);
        isSpinning = false;
        progress = (int) ((((float) i) * 360) / 100);
        spinHandler.sendEmptyMessage(0);
    }

    public void setProgressColor(int color) {
        rimPaint.setColor(color);
        textPaint.setColor(color);
        unitTextPaint.setColor(color);
        barPaint.setColor(barColor);
        invalidate();
    }

    public void setFlueColor(int color) {
        rimPaint.setColor(color);
        textPaint.setColor(color);
        unitTextPaint.setColor(color);
        barPaint.setColor(color);
        invalidate();
    }

    // ----------------------------------
    // Getters + setters
    // ----------------------------------

    /**
     * Set the text in the progress bar Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }

    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }
}
