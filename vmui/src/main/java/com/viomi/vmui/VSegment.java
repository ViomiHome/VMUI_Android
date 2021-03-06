
package com.viomi.vmui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viomi.vmui.adapter.VItemViewsAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class VSegment extends HorizontalScrollView {

    private static final String TAG = "VSegment";

    // mode: 自适应宽度+滚动 / 均分
    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;
    // icon position
    public static final int ICON_POSITION_LEFT = 0;
    public static final int ICON_POSITION_TOP = 1;
    public static final int ICON_POSITION_RIGHT = 2;
    public static final int ICON_POSITION_BOTTOM = 3;

    private static final int NO_POSITION = -1;
    /**
     * listener
     */
    private final ArrayList<OnTabSelectedListener> mSelectedListeners = new ArrayList<>();
    private Container mContentLayout;

    private int mCurrentSelectedIndex = NO_POSITION;
    private int mPendingSelectedIndex = NO_POSITION;

    /**
     * item的默认字体大小
     */
    private int mTabTextSize;
    /**
     * item的选中字体大小
     */
    private int mTabSelectedTextSize;
    /**
     * 是否有Indicator
     */
    private boolean mHasIndicator = true;
    /**
     * Indicator宽度
     */
    private int mIndicatorWidth;
    /**
     * Indicator高度
     */
    private int mIndicatorHeight;
    /**
     * indicator采用drawable
     */
    private Drawable mIndicatorDrawable;

    private int mIndicatorColor;

    /**
     * indicator rect, draw directly
     */
    private Rect mIndicatorRect = null;

    /**
     * indicator paint, draw directly
     */
    private Paint mIndicatorPaint = null;

    /**
     * item normal color
     */
    private int mDefaultNormalColor;
    /**
     * item selected color
     */
    private int mDefaultSelectedColor;
    /**
     * item icon的默认位置
     */
    @IconPosition
    private int mDefaultTabIconPosition;
    /**
     * ScrollMode下item的间隙
     */
    private int mItemSpaceInScrollMode;
    /**
     * typeface
     */
    private TypefaceProvider mTypefaceProvider;

    /**
     * 记录 ViewPager 的 scrollState
     */
    private int mViewPagerScrollState = ViewPager.SCROLL_STATE_IDLE;

    private Animator mSelectAnimator;
    private OnTabClickListener mOnTabClickListener;
    protected OnClickListener mTabOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSelectAnimator != null || mViewPagerScrollState != ViewPager.SCROLL_STATE_IDLE) {
                return;
            }
            int index = (int) v.getTag();
            Tab model = getAdapter().getItem(index);
            if (model != null) {
                selectTab(index, !mHasIndicator && !model.isDynamicChangeIconColor(), true);
            }
            if (mOnTabClickListener != null) {
                mOnTabClickListener.onTabClick(index);
            }
        }
    };
    /**
     * 与ViewPager的协同工作
     */
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnTabSelectedListener mViewPagerSelectedListener;
    private AdapterChangeListener mAdapterChangeListener;
    private boolean mIsInSelectTab = false;


    public VSegment(Context context) {
        this(context, null);
    }


    public VSegment(Context context, boolean hasIndicator) {
        this(context, null);
        mHasIndicator = hasIndicator;
    }

    public VSegment(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VSegment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        setClipToPadding(false);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VTabSegment, defStyleAttr, 0);
        mDefaultSelectedColor = array.getColor(R.styleable.VTabSegment_seg_selected_color, getResources().getColor(R.color.title_gray));
        mDefaultNormalColor = array.getColor(R.styleable.VTabSegment_seg_normal_color, getResources().getColor(R.color.tips_gray));
        mHasIndicator = array.getBoolean(R.styleable.VTabSegment_seg_has_indicator, true);
        mIndicatorWidth = array.getDimensionPixelSize(R.styleable.VTabSegment_seg_indicator_width,
                getResources().getDimensionPixelSize(R.dimen.segment_indicator_with));
        mIndicatorHeight = array.getDimensionPixelSize(R.styleable.VTabSegment_seg_indicator_height,
                getResources().getDimensionPixelSize(R.dimen.segment_indicator_height));
        mTabTextSize = array.getDimensionPixelSize(R.styleable.VTabSegment_seg_normal_textsize,
                getResources().getDimensionPixelSize(R.dimen.segment_normal_textsize));
        mTabSelectedTextSize = array.getDimensionPixelSize(R.styleable.VTabSegment_seg_selected_textsize,
                getResources().getDimensionPixelSize(R.dimen.segment_selected_textsize));
        mDefaultTabIconPosition = array.getInt(R.styleable.VTabSegment_seg_tab_icon_position, ICON_POSITION_LEFT);
        mItemSpaceInScrollMode = array.getDimensionPixelSize(R.styleable.VTabSegment_seg_itemspace_inscroll,
                getResources().getDimensionPixelSize(R.dimen.segment_itemspace));
        array.recycle();

        mContentLayout = new Container(context);
        addView(mContentLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }


    private String getFullClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        return className;
    }

    public void setTypefaceProvider(TypefaceProvider typefaceProvider) {
        mTypefaceProvider = typefaceProvider;
    }

    public VSegment addTab(Tab item) {
        mContentLayout.getTabAdapter().addItem(item);
        notifyDataChanged();
        return this;
    }

    public TabAdapter getAdapter() {
        return mContentLayout.getTabAdapter();
    }

    public void setTabTextSize(int tabTextSize) {
        mTabTextSize = tabTextSize;
    }

    /**
     * 清空已经存在的 Tab。
     * 一般先调用本方法清空已加上的 Tab, 然后重新 {@link #addTab(Tab)} 添加新的 Tab, 然后通过 {@link #notifyDataChanged()} 通知变动
     */
    public void reset() {
        mContentLayout.getTabAdapter().clear();
        mCurrentSelectedIndex = NO_POSITION;
        if (mSelectAnimator != null) {
            mSelectAnimator.cancel();
            mSelectAnimator = null;
        }
    }

    /**
     * 通知 TabSegment 数据变动。
     * 一般先调用 {@link #reset()} 清空已加上的 Tab, 然后重新 {@link #addTab(Tab)} 添加新的 Tab, 然后通过本方法通知变动
     */
    public void notifyDataChanged() {
        getAdapter().setup();
        populateFromPagerAdapter(false);
    }

    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener);
        }
    }

    public void setItemSpaceInScrollMode(int itemSpaceInScrollMode) {
        mItemSpaceInScrollMode = itemSpaceInScrollMode;
    }

    /**
     * 设置 indicator 为自定义的 Drawable(默认跟随 Tab 的 selectedColor)
     */
    public void setIndicatorDrawable(Drawable indicatorDrawable) {
        mIndicatorDrawable = indicatorDrawable;
        if (indicatorDrawable != null) {
            mIndicatorHeight = indicatorDrawable.getIntrinsicHeight();
        }
        mContentLayout.invalidate();
    }

    public void setIndicatorSelectedColor(int color) {
        mIndicatorColor = color;
        for (TabItemView view : mContentLayout.getTabAdapter().getViews()) {
            view.getTextView().setTextChangeColor(mIndicatorColor);
        }
        mContentLayout.invalidate();
    }


    public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        mSelectedListeners.remove(listener);
    }

    public void clearOnTabSelectedListeners() {
        mSelectedListeners.clear();
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, boolean useAdapterTitle) {
        setupWithViewPager(viewPager, useAdapterTitle, true);
    }

    /**
     * @param viewPager       需要关联的 ViewPager。
     * @param useAdapterTitle 自动根据ViewPager的adapter.getTitle取值。
     * @param autoRefresh     adapter有更改时，刷新TabSegment。
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean useAdapterTitle, boolean autoRefresh) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mOnPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
            }

            if (mAdapterChangeListener != null) {
                mViewPager.removeOnAdapterChangeListener(mAdapterChangeListener);
            }
        }

        if (mViewPagerSelectedListener != null) {
            // If we already have a tab selected listener for the ViewPager, remove it
            removeOnTabSelectedListener(mViewPagerSelectedListener);
            mViewPagerSelectedListener = null;
        }

        if (viewPager != null) {
            mViewPager = viewPager;

            // Add our custom OnPageChangeListener to the ViewPager
            if (mOnPageChangeListener == null) {
                mOnPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            viewPager.addOnPageChangeListener(mOnPageChangeListener);

            // Now we'll add a tab selected listener to set ViewPager's current item
            mViewPagerSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            addOnTabSelectedListener(mViewPagerSelectedListener);

            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                // Now we'll populate ourselves from the pager adapter, adding an observer if
                // autoRefresh is enabled
                setPagerAdapter(adapter, useAdapterTitle, autoRefresh);
            }

            // Add a listener so that we're notified of any adapter changes
            if (mAdapterChangeListener == null) {
                mAdapterChangeListener = new AdapterChangeListener(useAdapterTitle);
            }
            mAdapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager.addOnAdapterChangeListener(mAdapterChangeListener);
        } else {
            // We've been given a null ViewPager so we need to clear out the internal state,
            // listeners and observers
            mViewPager = null;
            setPagerAdapter(null, false, false);
        }
    }

    private void dispatchTabSelected(int index) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabSelected(index);
        }
    }

    private void dispatchTabUnselected(int index) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabUnselected(index);
        }
    }

    private void dispatchTabReselected(int index) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabReselected(index);
        }
    }

    private void dispatchTabDoubleTap(int index) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onDoubleTap(index);
        }
    }

    /**
     * 设置 Tab 正常状态下的颜色
     */
    public void setDefaultNormalColor(@ColorInt int defaultNormalColor) {
        mDefaultNormalColor = defaultNormalColor;
        for (TabItemView view : mContentLayout.mTabAdapter.getViews()) {
            view.getTextView().setTextOriginColor(defaultNormalColor);
        }
    }

    /**
     * 设置 Tab 选中状态下的颜色
     */
    public void setDefaultSelectedColor(@ColorInt int defaultSelectedColor) {
        mDefaultSelectedColor = defaultSelectedColor;
    }

    /**
     * @param defaultTabIconPosition
     */
    public void setDefaultTabIconPosition(@IconPosition int defaultTabIconPosition) {
        mDefaultTabIconPosition = defaultTabIconPosition;
    }


    private void setViewPagerScrollState(int state) {
        mViewPagerScrollState = state;
        if (mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE) {
            if (mPendingSelectedIndex != NO_POSITION && mSelectAnimator == null) {
                selectTab(mPendingSelectedIndex, true, false);
                mPendingSelectedIndex = NO_POSITION;
            }
        }
    }

    public void selectTab(int index) {
        selectTab(index, false, false);
    }

    public void selectTab(final int index, boolean noAnimation, boolean fromTabClick) {
        if (mIsInSelectTab) {
            return;
        }
        mIsInSelectTab = true;
        TabAdapter tabAdapter = getAdapter();
        List<TabItemView> listViews = tabAdapter.getViews();

        if (listViews.size() != tabAdapter.getSize()) {
            tabAdapter.setup();
            listViews = tabAdapter.getViews();
        }

        if (listViews.size() == 0 || listViews.size() <= index) {
            mIsInSelectTab = false;
            return;
        }

        if (mSelectAnimator != null || mViewPagerScrollState != ViewPager.SCROLL_STATE_IDLE) {
            mPendingSelectedIndex = index;
            mIsInSelectTab = false;
            return;
        }

        if (mCurrentSelectedIndex == index) {
            if (fromTabClick) {
                // dispatch re select only when click tab
                dispatchTabReselected(index);
            }
            mIsInSelectTab = false;
            // invalidate mContentLayout to sure indicator is drawn if needed
            mContentLayout.invalidate();
            return;
        }


        if (mCurrentSelectedIndex > listViews.size()) {
            Log.i(TAG, "selectTab: current selected index is bigger than views size.");
            mCurrentSelectedIndex = NO_POSITION;
        }

        // first time to select
        if (mCurrentSelectedIndex == NO_POSITION) {
            Tab model = tabAdapter.getItem(index);
            layoutIndicator(model, listViews.get(index), true);
            VColorTextView selectedTv = listViews.get(index).getTextView();
            setTextViewTypeface(selectedTv, true);
            listViews.get(index).updateDecoration(model, true);
            dispatchTabSelected(index);
            mCurrentSelectedIndex = index;
            mIsInSelectTab = false;
            return;
        }

        final int prev = mCurrentSelectedIndex;
        final Tab prevModel = tabAdapter.getItem(prev);
        final TabItemView prevView = listViews.get(prev);
        final Tab nowModel = tabAdapter.getItem(index);
        final TabItemView nowView = listViews.get(index);

        if (noAnimation) {
            dispatchTabUnselected(prev);
            dispatchTabSelected(index);
            setTextViewTypeface(prevView.getTextView(), false);
            setTextViewTypeface(nowView.getTextView(), true);
            prevView.updateDecoration(prevModel, false);
            nowView.updateDecoration(nowModel, true);
            if (getScrollX() > nowView.getLeft()) {
                smoothScrollTo(nowView.getLeft(), 0);
            } else {
                int realWidth = getWidth() - getPaddingRight() - getPaddingLeft();
                if (getScrollX() + realWidth < nowView.getRight()) {
                    smoothScrollBy(nowView.getRight() - realWidth - getScrollX(), 0);
                }
            }
            mCurrentSelectedIndex = index;
            mIsInSelectTab = false;
            layoutIndicator(nowModel, nowView, true);
            return;
        }

        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animValue = (float) animation.getAnimatedValue();
                int preColor = computeColor(getTabSelectedColor(prevModel), getTabNormalColor(prevModel), animValue);
                int nowColor = computeColor(getTabNormalColor(nowModel), getTabSelectedColor(nowModel), animValue);
                prevView.setColorInTransition(prevModel, preColor);
                nowView.setColorInTransition(nowModel, nowColor);
                layoutIndicatorInTransition(prevModel, nowModel, animValue);
                int left = mIndicatorRect.left - prevModel.contentLeft;
                int right = mIndicatorRect.right - prevModel.contentLeft;
                prevView.setSelectedRect(new Rect(left, 0, right, 0));

                int leftT = mIndicatorRect.left - nowModel.contentLeft;
                int rightT = mIndicatorRect.right - nowModel.contentLeft;
                nowView.setSelectedRect(new Rect(leftT, 0, rightT, 0));
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSelectAnimator = animation;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSelectAnimator = null;
                prevView.updateDecoration(prevModel, false);
                nowView.updateDecoration(nowModel, true);
                dispatchTabSelected(index);
                dispatchTabUnselected(prev);
                setTextViewTypeface(prevView.getTextView(), false);
                setTextViewTypeface(nowView.getTextView(), true);
                mCurrentSelectedIndex = index;
                mIsInSelectTab = false;
                if (mPendingSelectedIndex != NO_POSITION && mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    selectTab(mPendingSelectedIndex, true, false);
                    mPendingSelectedIndex = NO_POSITION;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mSelectAnimator = null;
                prevView.updateDecoration(prevModel, true);
                nowView.updateDecoration(nowModel, false);
                layoutIndicator(prevModel, prevView, true);
                mIsInSelectTab = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public int computeColor(@ColorInt int fromColor, @ColorInt int toColor, float fraction) {
        fraction = Math.max(Math.min(fraction, 1), 0);

        int minColorA = Color.alpha(fromColor);
        int maxColorA = Color.alpha(toColor);
        int resultA = (int) ((maxColorA - minColorA) * fraction) + minColorA;

        int minColorR = Color.red(fromColor);
        int maxColorR = Color.red(toColor);
        int resultR = (int) ((maxColorR - minColorR) * fraction) + minColorR;

        int minColorG = Color.green(fromColor);
        int maxColorG = Color.green(toColor);
        int resultG = (int) ((maxColorG - minColorG) * fraction) + minColorG;

        int minColorB = Color.blue(fromColor);
        int maxColorB = Color.blue(toColor);
        int resultB = (int) ((maxColorB - minColorB) * fraction) + minColorB;

        return Color.argb(resultA, resultR, resultG, resultB);
    }

    private void layoutIndicator(Tab model, TabItemView view, boolean invalidate) {
        if (model == null) {
            return;
        }


        if (mIndicatorRect == null) {
            mIndicatorRect = new Rect(model.contentLeft - model.leftAddonMargin, 0,
                    model.contentLeft + model.contentWidth + model.rightAddonMargin, 0);
        } else {
            mIndicatorRect.left = model.contentLeft - model.leftAddonMargin;
            mIndicatorRect.right = model.contentLeft + model.contentWidth + model.rightAddonMargin;
        }
        if (mIndicatorPaint == null) {
            mIndicatorPaint = new Paint();
            mIndicatorPaint.setStyle(Paint.Style.FILL);
        }
        mIndicatorPaint.setColor(getTabSelectedColor(model));

        int left = mIndicatorRect.left - model.contentLeft;
        int right = mIndicatorRect.right - model.contentLeft;
        view.setSelectedRect(new Rect(left, 0, right, 0));
        if (invalidate) {
            mContentLayout.invalidate();
        }

    }

    private void layoutIndicatorInTransition(Tab preModel, Tab targetModel, float offsetPercent) {
        int leftDistance = targetModel.getContentLeft() - preModel.getContentLeft();
        int widthDistance = targetModel.getContentWidth() - preModel.getContentWidth();
        int targetLeft = (int) (preModel.getContentLeft() + leftDistance * offsetPercent);
        int targetWidth = (int) (preModel.getContentWidth() + widthDistance * offsetPercent);
        if (mIndicatorRect == null) {
            mIndicatorRect = new Rect(targetLeft, 0, targetLeft + targetWidth, 0);
        } else {
            mIndicatorRect.left = targetLeft;
            mIndicatorRect.right = targetLeft + targetWidth;
        }

        if (mIndicatorPaint == null) {
            mIndicatorPaint = new Paint();
            mIndicatorPaint.setStyle(Paint.Style.FILL);
        }
        int indicatorColor = computeColor(
                getTabSelectedColor(preModel), getTabSelectedColor(targetModel), offsetPercent);
        mIndicatorPaint.setColor(indicatorColor);
        mContentLayout.invalidate();
    }


    private void setTextViewTypeface(VColorTextView tv, boolean selected) {
        if (mTypefaceProvider == null || tv == null) {
            return;
        }
        boolean isBold = selected ? mTypefaceProvider.isSelectedTabBold() : mTypefaceProvider.isNormalTabBold();
        tv.setTypeface(mTypefaceProvider.getTypeface());
    }

    public void updateIndicatorPosition(final int index, float offsetPercent) {
        if (mSelectAnimator != null || mIsInSelectTab || offsetPercent == 0) {
            return;
        }

        int targetIndex;
        if (offsetPercent < 0) {
            targetIndex = index - 1;
            offsetPercent = -offsetPercent;
        } else {
            targetIndex = index + 1;
        }

        TabAdapter tabAdapter = getAdapter();
        final List<TabItemView> listViews = tabAdapter.getViews();
        if (listViews.size() <= index || listViews.size() <= targetIndex) {
            return;
        }
        Tab preModel = tabAdapter.getItem(index);
        Tab targetModel = tabAdapter.getItem(targetIndex);
        TabItemView preView = listViews.get(index);
        TabItemView targetView = listViews.get(targetIndex);
        int preColor = computeColor(getTabSelectedColor(preModel), getTabNormalColor(preModel), offsetPercent);
        int targetColor = computeColor(getTabNormalColor(targetModel), getTabSelectedColor(targetModel), offsetPercent);
        preView.setColorInTransition(preModel, preColor);
        targetView.setColorInTransition(targetModel, targetColor);
        layoutIndicatorInTransition(preModel, targetModel, offsetPercent);

        int left = mIndicatorRect.left - preModel.contentLeft - preView.getTextView().getLeft();
        int right = mIndicatorRect.right - preModel.contentLeft - preView.getTextView().getLeft();
        preView.setSelectedRect(new Rect(left, 0, right, 0));
        int leftT = mIndicatorRect.left - targetModel.contentLeft - targetView.getTextView().getLeft();
        int rightT = mIndicatorRect.right - targetModel.contentLeft - targetView.getTextView().getLeft();
        targetView.setSelectedRect(new Rect(leftT, 0, rightT, 0));
        Log.d(TAG, "updateIndicatorPosition   " + "pre:" + index + "  target:" + targetIndex);
    }

    /**
     * 改变 Tab 的文案
     *
     * @param index Tab 的 index
     * @param text  新文案
     */
    public void updateTabText(int index, String text) {
        Tab model = getAdapter().getItem(index);
        if (model == null) {
            return;
        }
        model.setText(text);
        notifyDataChanged();
    }

    /**
     * 整个 Tab 替换
     *
     * @param index 需要被替换的 Tab 的 index
     * @param model 新的 Tab
     */
    public void replaceTab(int index, Tab model) {
        try {
            getAdapter().replaceItem(index, model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    private void setDrawable(TextView tv, Drawable drawable, int iconPosition) {
        tv.setCompoundDrawables(
                iconPosition == ICON_POSITION_LEFT ? drawable : null,
                iconPosition == ICON_POSITION_TOP ? drawable : null,
                iconPosition == ICON_POSITION_RIGHT ? drawable : null,
                iconPosition == ICON_POSITION_BOTTOM ? drawable : null);
    }

    private int getTabNormalColor(Tab item) {
        int color = item.getNormalColor();
        if (color == Tab.USE_TAB_SEGMENT) {
            color = mDefaultNormalColor;
        }
        return color;
    }

    private int getTabIconPosition(Tab item) {
        int iconPosition = item.getIconPosition();
        if (iconPosition == Tab.USE_TAB_SEGMENT) {
            iconPosition = mDefaultTabIconPosition;
        }
        return iconPosition;
    }

    private int getTabTextSize(Tab item) {
        int textSize = item.getTextSize();
        if (textSize == Tab.USE_TAB_SEGMENT) {
            textSize = mTabTextSize;
        }
        return textSize;
    }

    private int getTabSelectedColor(Tab item) {
        int color = item.getSelectedColor();
        if (color == Tab.USE_TAB_SEGMENT) {
            color = mDefaultSelectedColor;
        }
        return color;
    }

    void populateFromPagerAdapter(boolean useAdapterTitle) {
        if (mPagerAdapter == null) {
            if (useAdapterTitle) {
                reset();
            }
            return;
        }
        final int adapterCount = mPagerAdapter.getCount();
        if (useAdapterTitle) {
            reset();
            for (int i = 0; i < adapterCount; i++) {
                addTab(new Tab(mPagerAdapter.getPageTitle(i)));
            }
            notifyDataChanged();
        }

        if (mViewPager != null && adapterCount > 0) {
            final int curItem = mViewPager.getCurrentItem();
            selectTab(curItem, true, false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            int paddingHor = getPaddingLeft() + getPaddingRight();
            child.measure(MeasureSpec.makeMeasureSpec(widthSize - paddingHor, MeasureSpec.EXACTLY), heightMeasureSpec);
            if (widthMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(Math.min(widthSize, child.getMeasuredWidth() + paddingHor), heightMeasureSpec);
                return;
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    void setPagerAdapter(@Nullable final PagerAdapter adapter, boolean useAdapterTitle, final boolean addObserver) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
        }

        mPagerAdapter = adapter;

        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = new PagerAdapterObserver(useAdapterTitle);
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver);
        }

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter(useAdapterTitle);
    }

    public int getSelectedIndex() {
        return mCurrentSelectedIndex;
    }

    private int getTabCount() {
        return getAdapter().getSize();
    }

    /**
     * 根据 index 获取对应下标的 {@link Tab} 对象
     *
     * @return index 下标对应的 {@link Tab} 对象
     */
    public Tab getTab(int index) {
        return getAdapter().getItem(index);
    }

    /**
     * 根据 index 在对应的 Tab 上显示未读数或红点
     *
     * @param index 要显示未读数或红点的 Tab 的下标
     * @param count 不为0时红点会显示该数字作为未读数,为0时只会显示一个小红点
     */
    public void showSignCountView(Context context, int index, int count) {
        Tab tab = getAdapter().getItem(index);
        tab.showSignCountView(context, count);
        notifyDataChanged();
    }

    /**
     * 根据 index 在对应的 Tab 上隐藏红点
     */
    public void hideSignCountView(int index) {
        Tab tab = getAdapter().getItem(index);
        tab.hideSignCountView();
    }

    /**
     * 获取当前的红点数值，如果没有红点则返回 0
     */
    public int getSignCount(int index) {
        Tab tab = getAdapter().getItem(index);
        return tab.getSignCount();
    }


    @IntDef(value = {MODE_SCROLLABLE, MODE_FIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    @IntDef(value = {ICON_POSITION_LEFT, ICON_POSITION_TOP, ICON_POSITION_RIGHT, ICON_POSITION_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IconPosition {
    }

    public interface OnTabClickListener {
        /**
         * 当某个 Tab 被点击时会触发
         *
         * @param index 被点击的 Tab 下标
         */
        void onTabClick(int index);
    }

    public interface OnTabSelectedListener {
        /**
         * 当某个 Tab 被选中时会触发
         *
         * @param index 被选中的 Tab 下标
         */
        void onTabSelected(int index);

        /**
         * 当某个 Tab 被取消选中时会触发
         *
         * @param index 被取消选中的 Tab 下标
         */
        void onTabUnselected(int index);

        /**
         * 当某个 Tab 处于被选中状态下再次被点击时会触发
         *
         * @param index 被再次点击的 Tab 下标
         */
        void onTabReselected(int index);

        /**
         * 当某个 Tab 被双击时会触发
         *
         * @param index 被双击的 Tab 下标
         */
        void onDoubleTap(int index);
    }

    public interface TypefaceProvider {

        boolean isNormalTabBold();

        boolean isSelectedTabBold();

        @Nullable
        Typeface getTypeface();
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<VSegment> mTabSegmentRef;

        public TabLayoutOnPageChangeListener(VSegment tabSegment) {
            mTabSegmentRef = new WeakReference<>(tabSegment);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            final VSegment tabSegment = mTabSegmentRef.get();
            if (tabSegment != null) {
                tabSegment.setViewPagerScrollState(state);
            }

        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
            Log.d(TAG, "onPageScrolled   " + "position:" + position + "  positionOffset:" + positionOffset);
            final VSegment tabSegment = mTabSegmentRef.get();
            if (tabSegment != null) {
                tabSegment.updateIndicatorPosition(position, positionOffset);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            final VSegment tabSegment = mTabSegmentRef.get();
            if (tabSegment != null && tabSegment.mPendingSelectedIndex != NO_POSITION) {
                tabSegment.mPendingSelectedIndex = position;
                return;
            }
            if (tabSegment != null && tabSegment.getSelectedIndex() != position
                    && position < tabSegment.getTabCount()) {
                tabSegment.selectTab(position, true, false);
            }
        }
    }

    private static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(int index) {
            mViewPager.setCurrentItem(index, false);
        }

        @Override
        public void onTabUnselected(int index) {
        }

        @Override
        public void onTabReselected(int index) {
        }

        @Override
        public void onDoubleTap(int index) {

        }
    }

    public static class Tab {
        public static final int USE_TAB_SEGMENT = Integer.MIN_VALUE;
        private int textSize = USE_TAB_SEGMENT;
        private int normalColor = USE_TAB_SEGMENT;
        private int selectedColor = USE_TAB_SEGMENT;
        private int contentWidth = 0;
        private int contentLeft = 0;
        private int iconPosition = USE_TAB_SEGMENT;
        private int gravity = Gravity.CENTER;
        private CharSequence text;
        private List<View> mCustomViews;
        private int mSignCountDigits = 2;
        private View mSignCountTextView;
        private int mSignCountMarginLeft = 0;
        private int mSignCountMarginTop = 0;
        /**
         * 是否动态更改icon颜色，如果为true, selectedIcon将失效
         */
        private boolean dynamicChangeIconColor = true;

        private float rightSpaceWeight = 0f;
        private float leftSpaceWeight = 0f;
        private int leftAddonMargin = 0;
        private int rightAddonMargin = 0;

        public Tab(CharSequence text) {
            this.text = text;
        }


        public Tab(CharSequence text, boolean dynamicChangeIconColor) {
            this(text, dynamicChangeIconColor, true);
        }

        /**
         * 如果你的 icon 显示大小和实际大小不吻合:
         * 1. 设置icon 的 bounds
         * 2. 使用此构造器
         * 3. 最后一个参数（setIntrinsicSize）设置为false
         *
         * @param text                   文字
         * @param dynamicChangeIconColor 是否动态改变 icon 颜色
         * @param setIntrinsicSize       是否设置 icon 的大小为 intrinsic width 和 intrinsic height。
         */
        public Tab(CharSequence text, boolean dynamicChangeIconColor, boolean setIntrinsicSize) {
            this.text = text;
            this.dynamicChangeIconColor = dynamicChangeIconColor;
        }

        /**
         * 设置红点中数字显示的最大位数，默认值为 2，超过这个位数以 99+ 这种形式显示。如：110 -> 99+，98 -> 98
         *
         * @param digit 数字显示的最大位数
         */
        public void setmSignCountDigits(int digit) {
            mSignCountDigits = digit;
        }

        public void setTextColor(@ColorInt int normalColor, @ColorInt int selectedColor) {
            this.normalColor = normalColor;
            this.selectedColor = selectedColor;
        }

        public void setSpaceWeight(float leftWeight, float rightWeight) {
            leftSpaceWeight = leftWeight;
            rightSpaceWeight = rightWeight;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public CharSequence getText() {
            return text;
        }

        public void setText(CharSequence text) {
            this.text = text;
        }

        public int getContentLeft() {
            return contentLeft;
        }

        public void setContentLeft(int contentLeft) {
            this.contentLeft = contentLeft;
        }

        public int getContentWidth() {
            return contentWidth;
        }

        public void setContentWidth(int contentWidth) {
            this.contentWidth = contentWidth;
        }

        public int getIconPosition() {
            return iconPosition;
        }

        public void setIconPosition(int iconPosition) {
            this.iconPosition = iconPosition;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public int getNormalColor() {
            return normalColor;
        }


        public int getSelectedColor() {
            return selectedColor;
        }


        public boolean isDynamicChangeIconColor() {
            return dynamicChangeIconColor;
        }

        public void addCustomView(@NonNull View view) {
            if (mCustomViews == null) {
                mCustomViews = new ArrayList<>();
            }
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(getDefaultCustomLayoutParam());
            }
            mCustomViews.add(view);
        }

        public List<View> getCustomViews() {
            return mCustomViews;
        }

        /**
         * 设置红点的位置, 注意红点的默认位置是在内容的右侧并顶对齐
         *
         * @param marginLeft 在红点默认位置的基础上添加的 marginLeft
         * @param marginTop  在红点默认位置的基础上添加的 marginTop
         */
        public void setSignCountMargin(int marginLeft, int marginTop) {
            mSignCountMarginLeft = marginLeft;
            mSignCountMarginTop = marginTop;
            if (mSignCountTextView != null && mSignCountTextView.getLayoutParams() != null) {
                ((MarginLayoutParams) mSignCountTextView.getLayoutParams()).leftMargin = marginLeft;
                ((MarginLayoutParams) mSignCountTextView.getLayoutParams()).topMargin = marginTop;
            }
        }

        private View ensureSignCountView(Context context, boolean align_parent) {
            if (mSignCountTextView == null) {

                mSignCountTextView = View.inflate(context, R.layout.tab_cont_textview, null);
                RelativeLayout.LayoutParams signCountLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                if (align_parent) {
                    signCountLp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    signCountLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                } else {
                    signCountLp.addRule(RelativeLayout.ALIGN_TOP, R.id.tab_segment_item_id);
                    signCountLp.addRule(RelativeLayout.RIGHT_OF, R.id.tab_segment_item_id);
                }

                mSignCountTextView.setLayoutParams(signCountLp);
                addCustomView(mSignCountTextView);
            }
            // 确保在先 setMargin 后 create 的情况下 margin 会生效
            setSignCountMargin(mSignCountMarginLeft, mSignCountMarginTop);
            return mSignCountTextView;
        }

        public void showSignCountView(Context context, int count) {
            showSignCountView(context, count, false);
        }

        /**
         * 显示 Tab 上的未读数或红点
         *
         * @param count 不为0时红点会显示该数字作为未读数,为0时只会显示一个小红点
         */
        public void showSignCountView(Context context, int count, boolean align_parent) {
            ensureSignCountView(context, align_parent);
            TextView textView = ((TextView) mSignCountTextView.findViewById(R.id.tv_reddot));
            textView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams signCountLp = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if (count != 0) {
                // 显示未读数
                String text = getNumberDigitsFormattingValue(count);
                int textWidth = (int) textView.getPaint().measureText(text);
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());

                signCountLp.width = textWidth + padding * 2;
                Log.d(TAG, "showSignCountView: " + signCountLp.width);
                signCountLp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics());
                signCountLp.topMargin = 0;
                textView.setLayoutParams(signCountLp);
                textView.setMinWidth(textWidth + padding * 2);
                textView.setBackgroundDrawable(context.getDrawable(R.drawable.red_dot_with_number));
                Log.d(TAG, "showSignCountView: " + text);
                ((TextView) textView.findViewById(R.id.tv_reddot)).setText(text);
            } else {
                // 显示红点
                signCountLp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
                signCountLp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
                signCountLp.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
                textView.setLayoutParams(signCountLp);
                textView.setBackground(context.getDrawable(R.drawable.red_dot));
                ((TextView) textView.findViewById(R.id.tv_reddot)).setText(null);
            }
        }

        /**
         * 隐藏 Tab 上的未读数或红点
         */
        public void hideSignCountView() {
            if (mSignCountTextView != null) {
                mSignCountTextView.setVisibility(View.GONE);
            }
        }

        /**
         * 获取该 Tab 的未读数
         */
        public int getSignCount() {
            if (mSignCountTextView == null || mSignCountTextView.getVisibility() != VISIBLE) {
                return 0;
            }
            if (!TextUtils.isEmpty(((TextView) mSignCountTextView.findViewById(R.id.tv_reddot)).getText())) {
                return Integer.parseInt(((TextView) mSignCountTextView.findViewById(R.id.tv_reddot)).getText().toString());
            } else {
                return 0;
            }
        }

        private RelativeLayout.LayoutParams getDefaultCustomLayoutParam() {
            return new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }

        private String getNumberDigitsFormattingValue(int number) {
            if (getNumberDigits(number) > mSignCountDigits) {
                StringBuilder result = new StringBuilder();
                for (int digit = 1; digit <= mSignCountDigits; digit++) {
                    result.append("9");
                }
                result.append("+");
                return result.toString();
            } else {
                return String.valueOf(number);
            }
        }

        public int getNumberDigits(int number) {
            if (number <= 0) return 0;
            return (int) (Math.log10(number) + 1);
        }
    }


    public class TabAdapter extends VItemViewsAdapter<Tab, TabItemView> {
        public TabAdapter(ViewGroup parentView) {
            super(parentView);
        }

        @Override
        protected TabItemView createView(ViewGroup parentView) {
            return new TabItemView(getContext());
        }

        @Override
        protected void bind(Tab item, TabItemView view, int position) {
            VColorTextView tv = view.getTextView();
            setTextViewTypeface(tv, mCurrentSelectedIndex == position);
            // custom view
            List<View> mCustomViews = item.getCustomViews();
            if (mCustomViews != null && mCustomViews.size() > 0) {
                view.setTag(R.id.view_can_not_cache_tag, true);
                for (View v : mCustomViews) {
                    // 防止先 setCustomViews 然后再 updateTabText 时会重复添加 customView 导致 crash
                    if (v.getParent() == null) {
                        view.addView(v);
                    }
                }
            }
            // gravity
            int gravity = item.getGravity();
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, (gravity & Gravity.LEFT) == Gravity.LEFT ? RelativeLayout.TRUE : 0);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, (gravity & Gravity.CENTER) == Gravity.CENTER ? RelativeLayout.TRUE : 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, (gravity & Gravity.RIGHT) == Gravity.RIGHT ? RelativeLayout.TRUE : 0);
            tv.setLayoutParams(lp);

            tv.setText(item.getText().toString());
            tv.setTextSize(getTabTextSize(item));
            view.updateDecoration(item, mCurrentSelectedIndex == position);
            view.setTag(position);
            view.setOnClickListener(mTabOnClickListener);
        }
    }

    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean mAutoRefresh;
        private final boolean mUseAdapterTitle;

        AdapterChangeListener(boolean useAdapterTitle) {
            mUseAdapterTitle = useAdapterTitle;
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager,
                                     @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (mViewPager == viewPager) {
                setPagerAdapter(newAdapter, mUseAdapterTitle, mAutoRefresh);
            }
        }

        void setAutoRefresh(boolean autoRefresh) {
            mAutoRefresh = autoRefresh;
        }
    }

    public class TabItemView extends RelativeLayout {
        private VColorTextView mTextView;
        private GestureDetector mGestureDetector;

        public TabItemView(Context context) {
            super(context);
            mTextView = new VColorTextView(getContext());
            // 用于提供给customView布局用
            mTextView.setId(R.id.tab_segment_item_id);
            LayoutParams tvLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tvLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            addView(mTextView, tvLp);
            // 添加双击事件
            mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (mSelectedListeners.isEmpty()) {
                        return false;
                    }
                    int index = (int) TabItemView.this.getTag();
                    Tab model = getAdapter().getItem(index);
                    if (model != null) {
                        dispatchTabDoubleTap(index);
                        return true;
                    }
                    return false;
                }
            });
        }

        public VColorTextView getTextView() {
            return mTextView;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
        }

        public void setColorInTransition(Tab tab, int color) {
            mTextView.setTextColor(color);
        }

        public void setSelectedRect(Rect rect) {
            mTextView.setSelectedRect(rect);
        }


        public void updateDecoration(Tab tab, boolean isSelected) {
            int color = isSelected ? getTabSelectedColor(tab) : getTabNormalColor(tab);
            mTextView.setTextColor(color);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private class PagerAdapterObserver extends DataSetObserver {
        private final boolean mUseAdapterTitle;

        PagerAdapterObserver(boolean useAdapterTitle) {
            mUseAdapterTitle = useAdapterTitle;
        }

        @Override
        public void onChanged() {
            populateFromPagerAdapter(mUseAdapterTitle);
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter(mUseAdapterTitle);
        }
    }

    private final class Container extends ViewGroup {
        private TabAdapter mTabAdapter;

        public Container(Context context) {
            super(context);
            mTabAdapter = new TabAdapter(this);
        }

        public TabAdapter getTabAdapter() {
            return mTabAdapter;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            List<TabItemView> childViews = mTabAdapter.getViews();
            int size = childViews.size();
            int i;

            int visibleChild = 0;
            for (i = 0; i < size; i++) {
                View child = childViews.get(i);
                if (child.getVisibility() == VISIBLE) {
                    visibleChild++;
                }
            }
            if (size == 0 || visibleChild == 0) {
                setMeasuredDimension(widthSpecSize, heightSpecSize);
                return;
            }

            int childHeight = heightSpecSize - getPaddingTop() - getPaddingBottom();
            int childWidthMeasureSpec, childHeightMeasureSpec, resultWidthSize = 0;
            resultWidthSize = widthSpecSize;
            int modeFixItemWidth = widthSpecSize / visibleChild;
            for (i = 0; i < size; i++) {
                final View child = childViews.get(i);
                if (child.getVisibility() != VISIBLE) {
                    continue;
                }
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(i == size - 1 ? (widthSpecSize - modeFixItemWidth * (size - 1)) : modeFixItemWidth, MeasureSpec.EXACTLY);
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                // reset
                Tab tab = mTabAdapter.getItem(i);
                tab.leftAddonMargin = 0;
                tab.rightAddonMargin = 0;
            }

            setMeasuredDimension(resultWidthSize, heightSpecSize);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            List<TabItemView> childViews = mTabAdapter.getViews();
            int size = childViews.size();
            int i;
            int visibleChild = 0;
            for (i = 0; i < size; i++) {
                View child = childViews.get(i);
                if (child.getVisibility() == VISIBLE) {
                    visibleChild++;
                }
            }

            if (size == 0 || visibleChild == 0) {
                return;
            }

            int usedLeft = getPaddingLeft();
            for (i = 0; i < size; i++) {
                TabItemView childView = childViews.get(i);
                if (childView.getVisibility() != VISIBLE) {
                    continue;
                }
                Tab model = mTabAdapter.getItem(i);
                final int childMeasureWidth = childView.getMeasuredWidth();

                childView.layout(
                        usedLeft + model.leftAddonMargin,
                        getPaddingTop(),
                        usedLeft + model.leftAddonMargin + childMeasureWidth + model.rightAddonMargin,
                        b - t - getPaddingBottom());


                int oldLeft, oldWidth, newLeft, newWidth;
                oldLeft = model.getContentLeft();
                oldWidth = model.getContentWidth();
                newLeft = usedLeft + model.leftAddonMargin;
                newWidth = childMeasureWidth;
                if (oldLeft != newLeft || oldWidth != newWidth) {
                    model.setContentLeft(newLeft);
                    model.setContentWidth(newWidth);
                }
                usedLeft = usedLeft + childMeasureWidth
                        + model.leftAddonMargin + model.rightAddonMargin;
            }

            if (mCurrentSelectedIndex != NO_POSITION && mSelectAnimator == null
                    && mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE) {
                layoutIndicator(mTabAdapter.getItem(mCurrentSelectedIndex), mTabAdapter.getViews().get(mCurrentSelectedIndex), false);
            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (mHasIndicator && mIndicatorRect != null) {
                mIndicatorRect.top = 0;
                mIndicatorRect.bottom = getHeight();
                if (mIndicatorDrawable != null) {
                    mIndicatorDrawable.setBounds(mIndicatorRect);
                    mIndicatorDrawable.draw(canvas);
                } else {
                    canvas.drawRect(mIndicatorRect, mIndicatorPaint);
                }
            }
            super.dispatchDraw(canvas);
        }
    }

}
