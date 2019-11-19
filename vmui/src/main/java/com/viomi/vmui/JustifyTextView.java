package com.viomi.vmui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

public class JustifyTextView extends AppCompatTextView {

    private int mLineY;
    private int mViewWidth;
    public static final String TWO_CHINESE_BLANK = "  ";

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();

        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        String text = getText().toString();
        mLineY = 0;
        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
//        int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
//        if (gravity == Gravity.CENTER_VERTICAL)
            mLineY = (int) ((getMeasuredHeight() - textHeight * getLineCount() - (getLineSpacingExtra() * getLineCount() - 1)) / 2);
        mLineY += getTextSize();

        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout.getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            boolean overWith = width > mViewWidth - getPaddingLeft() - getPaddingRight();
            boolean overWith1 = overWith;
            while (overWith) {
                lineEnd--;
                width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
                overWith = width > mViewWidth - getPaddingLeft() - getPaddingRight();
            }
            String line = text.substring(lineStart, overWith1 ? lineEnd - 1 : lineEnd);
            if (needScale(line) && i < layout.getLineCount() - 1) {
                drawScaledText(canvas, lineStart, line, width);
            } else {
//                int gravity1 = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
                int leftInCenterHorizontal = (int) ((mViewWidth - getPaddingLeft() - getPaddingRight() - width) / 2 + getPaddingLeft());
//                int center = (int) ((mViewWidth - getPaddingLeft() - getPaddingRight() ) / 2 );
//                int left = gravity1 == Gravity.CENTER_HORIZONTAL ? leftInCenterHorizontal : getPaddingLeft();
                canvas.drawText(overWith1 ? line + "..." : line
                        , leftInCenterHorizontal
                        , mLineY
                        , paint);
            }
            mLineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288 && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }

}