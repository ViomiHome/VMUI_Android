package com.viomi.vmui.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.viomi.vmui.R;
import com.viomi.vmui.utils.VMUIDateFormatUtils;
import com.viomi.vmui.utils.VMUIDisplayHelper;
import com.viomi.vmui.utils.VMUIResHelper;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VDialog extends Dialog {
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private boolean mCanceledOnTouchOutsideSet = true;

    public VDialog(@NonNull Context context) {
        super(context);
    }

    public VDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected VDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    private void initDialog() {
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.mCancelable = flag;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel && !mCancelable) {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = cancel;
        mCanceledOnTouchOutsideSet = true;
    }

    @Override
    public void show() {
        super.show();
    }

    boolean shouldWindowCloseOnTouchOutside() {
        if (!mCanceledOnTouchOutsideSet) {
            if (Build.VERSION.SDK_INT < 11) {
                mCanceledOnTouchOutside = true;
            } else {
                TypedArray a = getContext().obtainStyledAttributes(
                        new int[]{android.R.attr.windowCloseOnTouchOutside});
                mCanceledOnTouchOutside = a.getBoolean(0, true);
                a.recycle();
            }
            mCanceledOnTouchOutsideSet = true;
        }
        return mCanceledOnTouchOutside;
    }

    public void cancelOutSide() {
        if (mCancelable && isShowing() && shouldWindowCloseOnTouchOutside()) {
            cancel();
        }
    }

    public static class MessageDialogBuilder extends VDialogBuilder<MessageDialogBuilder> {
        protected CharSequence mMessage;
        private TextView mTextView;

        public MessageDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(int resId) {
            return setMessage(getBaseContext().getResources().getString(resId));
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            if (mMessage != null && mMessage.length() != 0) {
                mTextView = new TextView(context);
                assignMessageTvWithAttr(mTextView, hasTitle(), R.attr.dialog_message_content_style);
                mTextView.setText(mMessage);
                parent.addView(mTextView);
            }
        }

        public static void assignMessageTvWithAttr(TextView messageTv, boolean hasTitle, int defAttr) {
            VMUIResHelper.assignTextViewWithAttr(messageTv, defAttr);

            if (!hasTitle) {
                TypedArray a = messageTv.getContext().obtainStyledAttributes(null,
                        R.styleable.VMUIDialogMessageTvCustomDef, defAttr, 0);
                int count = a.getIndexCount();
                for (int i = 0; i < count; i++) {
                    int attr = a.getIndex(i);
                    if (attr == R.styleable.VMUIDialogMessageTvCustomDef_dialog_paddingTopWhenNotTitle) {
                        messageTv.setPadding(
                                messageTv.getPaddingLeft(),
                                a.getDimensionPixelSize(attr, messageTv.getPaddingTop()),
                                messageTv.getPaddingRight(),
                                messageTv.getPaddingBottom()
                        );
                    }
                }
                a.recycle();
            }
        }
    }

    public static class EditTextDialogBuilder extends VDialogBuilder<EditTextDialogBuilder> {

        protected String mPlaceholder;
        protected EditText mEditText;
        protected TransformationMethod mTransformationMethod;
        protected RelativeLayout mMainLayout;
        protected ImageView mRightImageView;
        private int mInputType = InputType.TYPE_CLASS_TEXT;

        public EditTextDialogBuilder(Context context) {
            super(context);
        }

        public EditTextDialogBuilder setPlaceholder(String placeholder) {
            this.mPlaceholder = placeholder;
            return this;
        }

        public EditTextDialogBuilder setPlaceholder(int resId) {
            return setPlaceholder(getBaseContext().getResources().getString(resId));
        }

        /**
         * 设置 EditText 的 inputType
         */
        public EditTextDialogBuilder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        /**
         * 设置密码不可见（new PasswordTransformationMethod()）
         *
         * @param method
         * @return
         */
        public EditTextDialogBuilder setTransformationMethod(TransformationMethod method) {
            mTransformationMethod = method;
            return this;
        }

        protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            return editLp;
        }

        protected RelativeLayout.LayoutParams createRightIconLayoutParams() {
            RelativeLayout.LayoutParams rightIconLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rightIconLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rightIconLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            rightIconLp.leftMargin = VMUIDisplayHelper.dpToPx(5);
            return rightIconLp;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            mEditText = new AppCompatEditText(context);
            MessageDialogBuilder.assignMessageTvWithAttr(mEditText, hasTitle(), R.attr.dialog_edit_content_style);
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
            mEditText.setId(R.id.vmui_dialog_edit_input);

            mRightImageView = new ImageView(context);
            mRightImageView.setId(R.id.vmui_dialog_edit_right_icon);
            mRightImageView.setVisibility(View.GONE);

            mMainLayout = new RelativeLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = mEditText.getPaddingTop();
            lp.leftMargin = mEditText.getPaddingLeft();
            lp.rightMargin = mEditText.getPaddingRight();
            lp.bottomMargin = mEditText.getPaddingBottom();
            mMainLayout.setBackgroundResource(R.drawable.vmui_edittext_bg_border_bottom);
            mMainLayout.setLayoutParams(lp);

            if (mTransformationMethod != null) {
                mEditText.setTransformationMethod(mTransformationMethod);
            } else {
                mEditText.setInputType(mInputType);
            }

            mEditText.setBackgroundResource(0);
            mEditText.setPadding(0, 0, 0, VMUIDisplayHelper.dpToPx(5));
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            if (mPlaceholder != null) {
                mEditText.setHint(mPlaceholder);
            }
            mMainLayout.addView(mEditText, createEditTextLayoutParams());
            mMainLayout.addView(mRightImageView, createRightIconLayoutParams());

            parent.addView(mMainLayout);
        }

        @Override
        protected void onAfter(VDialog dialog, LinearLayout parent, Context context) {
            super.onAfter(dialog, parent, context);
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    inputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            });
            mEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    inputMethodManager.showSoftInput(mEditText, 0);
                }
            }, 300);
        }
    }

    public static class SingleCheckableDialogBuilder extends VDialogBuilder<SingleCheckableDialogBuilder> {
        protected PickerView mPickerView;
        protected List<String> mData = new ArrayList<>();
        protected String mSelected;

        public SingleCheckableDialogBuilder(Context context) {
            super(context);
        }

        public SingleCheckableDialogBuilder setData(List<String> list) {
            mData = list;
            return this;
        }

        public String getSelected() {
            return mSelected;
        }

        public PickerView getPickerView() {
            return mPickerView;
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            mPickerView = new PickerView(context);
            mPickerView.setDataList(mData);
            mPickerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
            mPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    mSelected = selected;
                }
            });
            mPickerView.setSelected(0);
            parent.addView(mPickerView);
        }

    }

    public static class MultiCheckableDialogBuilder extends VDialogBuilder<MultiCheckableDialogBuilder> {
        protected ListView mListView;
        protected ListAdapter mAdapter;
        protected List<String> mData = new ArrayList<>();
        protected AdapterView.OnItemClickListener onItemClickListener;

        public MultiCheckableDialogBuilder(Context context) {
            super(context);
        }

        public MultiCheckableDialogBuilder setData(List<String> list) {
            this.mData = list;
            return this;
        }

        private void initData() {
            onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((ListAdapter.DataHolder) mAdapter.getItem(position)).checked = !((ListAdapter.DataHolder) mAdapter.getItem(position)).checked;
                    mAdapter.notifyDataSetChanged();
                }
            };
        }

        public String getSelectContent() {
            return mAdapter.getSelectContent();
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            initData();
            //mListView = new VMUIWrapContentListView(context, VMUIDisplayHelper.dp2px(context, 200));
            mListView = new ListView(context, null, 0, R.style.CustomListViewTheme);
            mAdapter = new ListAdapter(dialog.getContext(), mData);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, VMUIDisplayHelper.dp2px(context, 200));
            mListView.setLayoutParams(lp);
            mListView.setAdapter(mAdapter);
            mListView.setVerticalScrollBarEnabled(false);
            mListView.setOnItemClickListener(onItemClickListener);
            mListView.setDivider(getBaseContext().getDrawable(R.color.divider_gray));
            mListView.setDividerHeight(1);
            mListView.setSelector(getBaseContext().getDrawable(R.color.transparent));
            parent.addView(mListView);
        }
    }

    public static class LocationPickerDialogBuilder extends VDialogBuilder<LocationPickerDialogBuilder> {

        protected PickerView mProvincePickerView;
        protected PickerView mCityPickerView;
        protected PickerView mDistrictPickerView;
        protected Context mContext;
        private int mProvinceIndex = 0, mCityIndex = 0, mDistrictIndex = 0;
        private String mSelectContent;
        private List<VDialogLocationEntity.ResultBean> resultBeans;
        private List<String> mProvinces = new ArrayList<>(), mCities = new ArrayList<>(), mDistricts = new ArrayList<>();

        public LocationPickerDialogBuilder(Context context) {
            super(context);
            this.mContext = context;
        }

        private void initData() {
            String content = VMUIResHelper.readAssetsFile(mContext, "city.json", "UTF-8");
            resultBeans = JSON.parseObject(content, VDialogLocationEntity.class).getResult();
            for (int i = 0; i < resultBeans.size(); i++) {
                mProvinces.add(resultBeans.get(i).getProvince());
            }
            initLocationUnits(0);
            mProvincePickerView.setDataList(mProvinces);
            mProvincePickerView.setSelected(0);
            mCityPickerView.setDataList(mCities);
            mCityPickerView.setSelected(0);
            mDistrictPickerView.setDataList(mDistricts);
            mDistrictPickerView.setSelected(0);
            setSelectContent(mDistricts.get(mDistrictIndex));
            setCanScroll();
        }

        private void initLocationUnits(int i) {
            mCities.clear();
            mDistricts.clear();
            for (int j = 0; j < resultBeans.get(i).getCity().size(); j++) {
                mCities.add(resultBeans.get(i).getCity().get(j).getCity());
                for (int k = 0; k < resultBeans.get(i).getCity().get(j).getDistrict().size(); k++) {
                    mDistricts.add(resultBeans.get(i).getCity().get(j).getDistrict().get(k).getDistrict());
                }
            }
        }

        private void initCityUnit(int i) {
            mCities.clear();
            for (int j = 0; j < resultBeans.get(i).getCity().size(); j++) {
                mCities.add(resultBeans.get(i).getCity().get(j).getCity());
            }
        }

        private void initDistrictUnit(int i, int j) {
            mDistricts.clear();
            for (int k = 0; k < resultBeans.get(i).getCity().get(j).getDistrict().size(); k++) {
                mDistricts.add(resultBeans.get(i).getCity().get(j).getDistrict().get(k).getDistrict());
            }
        }

        private void setCanScroll() {
            mProvincePickerView.setCanScroll(mProvinces.size() > 1);
            mCityPickerView.setCanScroll(mCities.size() > 1);
            mDistrictPickerView.setCanScroll(mDistricts.size() > 1);
        }

        private void setProvinceIndex(String province) {
            for (int i = 0; i < mProvinces.size(); i++) {
                if (mProvinces.get(i).equals(province)) {
                    mProvinceIndex = i;
                    break;
                }
            }
        }

        private void setCityIndex(String city) {
            for (int i = 0; i < mCities.size(); i++) {
                if (mCities.get(i).equals(city)) {
                    mCityIndex = i;
                    break;
                }
            }
        }

        private void linkageCityUnit(final boolean showAnim) {
            initCityUnit(mProvinceIndex);

            mCityPickerView.setSelected(0);
            if (showAnim) {
                mCityPickerView.startAnim();
            }
            mCityPickerView.setCanScroll(mCities.size() > 1);
        }

        private void linkageDistrictUnit(final boolean showAnim) {
            initDistrictUnit(mProvinceIndex, mCityIndex);
            mDistrictPickerView.setSelected(0);
            if (showAnim) {
                mDistrictPickerView.startAnim();
            }
        }

        private void setSelectContent(String district) {
            JSONObject object = new JSONObject();
            object.put("province", mProvinces.get(mProvinceIndex));
            object.put("city", mCities.get(mCityIndex));
            object.put("district", district);
            this.mSelectContent = object.toJSONString();
        }

        public String getSelectContent() {
            return this.mSelectContent;
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            mProvincePickerView = new PickerView(context);
            mCityPickerView = new PickerView(context);
            mDistrictPickerView = new PickerView(context);

            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, 400);
            lp1.weight = 1.0f;
            mProvincePickerView.setLayoutParams(lp1);
            mCityPickerView.setLayoutParams(lp1);
            mDistrictPickerView.setLayoutParams(lp1);
            mCityPickerView.setCanShowAnim(false);
            mDistrictPickerView.setCanShowAnim(false);
            mProvincePickerView.setCanScrollLoop(false);
            mCityPickerView.setCanScrollLoop(false);
            mDistrictPickerView.setCanScrollLoop(false);

            initData();

            ll.addView(mProvincePickerView);
            ll.addView(mCityPickerView);
            ll.addView(mDistrictPickerView);

            mProvincePickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setProvinceIndex(selected);
                    linkageCityUnit(true);
                }
            });

            mCityPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setCityIndex(selected);
                    linkageDistrictUnit(true);
                }
            });

            mDistrictPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    setSelectContent(selected);
                }
            });

            parent.addView(ll);
        }
    }

    public static class DatePickerBuilder extends VDialogBuilder<DatePickerBuilder> {
        protected PickerView mYearPickerView;
        protected PickerView mMonthPickerView;
        protected PickerView mDayPickerView;

        protected long beginTimestamp = VMUIDateFormatUtils.str2Long("2009-05-01", 0);
        protected long endTimestamp = System.currentTimeMillis();
        protected int mShowDateFormatPattern = 0;
        private Calendar mBeginTime, mEndTime, mSelectedTime;
        private int mBeginYear, mBeginMonth, mBeginDay, mEndYear, mEndMonth, mEndDay;
        private List<String> mYearUnits = new ArrayList<>(), mMonthUnits = new ArrayList<>(), mDayUnits = new ArrayList<>();
        /**
         * 时间单位的最大显示值
         */
        private static final int MAX_MONTH_UNIT = 12;
        /**
         * 级联滚动延迟时间
         */
        private static final long LINKAGE_DELAY_DEFAULT = 100L;
        private DecimalFormat mDecimalFormat = new DecimalFormat("00");

        private void initData() {
            mSelectedTime.setTimeInMillis(mBeginTime.getTimeInMillis());

            mBeginYear = mBeginTime.get(Calendar.YEAR);
            // Calendar.MONTH 值为 0-11
            mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1;
            mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH);

            mEndYear = mEndTime.get(Calendar.YEAR);
            mEndMonth = mEndTime.get(Calendar.MONTH) + 1;
            mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH);

            boolean canSpanYear = mBeginYear != mEndYear;
            boolean canSpanMon = !canSpanYear && mBeginMonth != mEndMonth;
            if (canSpanYear) {
                initDateUnits(MAX_MONTH_UNIT, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else if (canSpanMon) {
                initDateUnits(mEndMonth, mBeginTime.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        }

        private void initDateUnits(int endMonth, int endDay) {
            for (int i = mBeginYear; i <= mEndYear; i++) {
                mYearUnits.add(String.valueOf(i));
            }

            for (int i = mBeginMonth; i <= endMonth; i++) {
                mMonthUnits.add(mDecimalFormat.format(i));
            }

            for (int i = mBeginDay; i <= endDay; i++) {
                mDayUnits.add(mDecimalFormat.format(i));
            }

            mYearPickerView.setDataList(mYearUnits);
            mYearPickerView.setSelected(0);
            mMonthPickerView.setDataList(mMonthUnits);
            mMonthPickerView.setSelected(0);
            mDayPickerView.setDataList(mDayUnits);
            mDayPickerView.setSelected(0);

            setCanScroll();
        }

        private void setCanScroll() {
            mYearPickerView.setCanScroll(mYearUnits.size() > 1);
            mMonthPickerView.setCanScroll(mMonthUnits.size() > 1);
            mDayPickerView.setCanScroll(mDayUnits.size() > 1);
        }

        public DatePickerBuilder(Context context) {
            super(context);
        }

        public DatePickerBuilder setBeginTimestamp(long timestamp) {
            this.beginTimestamp = timestamp;
            return this;
        }

        public DatePickerBuilder setEndTimestamp(long timestamp) {
            this.endTimestamp = timestamp;
            return this;
        }

        public long getSelectTimeStamp() {
            return mSelectedTime.getTimeInMillis();
        }

        public DatePickerBuilder setShowDateFormatPattern(int typ) {
            this.mShowDateFormatPattern = typ;
            return this;
        }

        @Override
        protected void onCreateContent(VDialog dialog, ViewGroup parent, Context context) {
            mBeginTime = Calendar.getInstance();
            mBeginTime.setTimeInMillis(beginTimestamp);
            mEndTime = Calendar.getInstance();
            mEndTime.setTimeInMillis(endTimestamp);
            mSelectedTime = Calendar.getInstance();

            mYearPickerView = new PickerView(context, "年");
            mMonthPickerView = new PickerView(context, "月");
            mDayPickerView = new PickerView(context, "日");

            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, 400);
            lp1.weight = 4.0f;
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, 400);
            lp2.weight = 2.0f;
            mYearPickerView.setLayoutParams(lp1);
            mMonthPickerView.setLayoutParams(lp2);
            mDayPickerView.setLayoutParams(lp2);
            mMonthPickerView.setCanShowAnim(false);
            mDayPickerView.setCanShowAnim(false);
            mYearPickerView.setCanScrollLoop(false);
            mMonthPickerView.setCanScrollLoop(false);
            mDayPickerView.setCanScrollLoop(false);

            initData();

            if (mShowDateFormatPattern == 0) {
                ll.addView(mYearPickerView);
                ll.addView(mMonthPickerView);
                ll.addView(mDayPickerView);
            } else if (mShowDateFormatPattern == 1) {
                ll.addView(mMonthPickerView);
                ll.addView(mDayPickerView);
            } else {
                ll.addView(mDayPickerView);
            }

            mYearPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    if (view == null || TextUtils.isEmpty(selected)) return;

                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    mSelectedTime.set(Calendar.YEAR, timeUnit);
                    linkageMonthUnit(true, LINKAGE_DELAY_DEFAULT);
                }
            });
            mMonthPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    int lastSelectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
                    mSelectedTime.add(Calendar.MONTH, timeUnit - lastSelectedMonth);
                    linkageDayUnit(true, LINKAGE_DELAY_DEFAULT);
                }
            });
            mDayPickerView.setOnSelectListener(new PickerView.OnSelectListener() {
                @Override
                public void onSelect(View view, String selected) {
                    int timeUnit;
                    try {
                        timeUnit = Integer.parseInt(selected);
                    } catch (Throwable ignored) {
                        return;
                    }

                    mSelectedTime.set(Calendar.DAY_OF_MONTH, timeUnit);
                }
            });
            parent.addView(ll);
        }

        /**
         * 联动“月”变化
         *
         * @param showAnim 是否展示滚动动画
         * @param delay    联动下一级延迟时间
         */
        private void linkageMonthUnit(final boolean showAnim, final long delay) {
            int minMonth;
            int maxMonth;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            if (mBeginYear == mEndYear) {
                minMonth = mBeginMonth;
                maxMonth = mEndMonth;
            } else if (selectedYear == mBeginYear) {
                minMonth = mBeginMonth;
                maxMonth = MAX_MONTH_UNIT;
            } else if (selectedYear == mEndYear) {
                minMonth = 1;
                maxMonth = mEndMonth;
            } else {
                minMonth = 1;
                maxMonth = MAX_MONTH_UNIT;
            }

            // 重新初始化时间单元容器
            mMonthUnits.clear();
            for (int i = minMonth; i <= maxMonth; i++) {
                mMonthUnits.add(mDecimalFormat.format(i));
            }
            mMonthPickerView.setDataList(mMonthUnits);

            // 确保联动时不会溢出或改变关联选中值
            int selectedMonth = getValueInRange(mSelectedTime.get(Calendar.MONTH) + 1, minMonth, maxMonth);
            mSelectedTime.set(Calendar.MONTH, selectedMonth - 1);
            mMonthPickerView.setSelected(selectedMonth - minMonth);
            if (showAnim) {
                mMonthPickerView.startAnim();
            }

            // 联动“日”变化
            mMonthPickerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linkageDayUnit(showAnim, delay);
                }
            }, delay);
        }

        /**
         * 联动“日”变化
         *
         * @param showAnim 是否展示滚动动画
         * @param delay    联动下一级延迟时间
         */
        private void linkageDayUnit(final boolean showAnim, final long delay) {
            int minDay;
            int maxDay;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            int selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth) {
                minDay = mBeginDay;
                maxDay = mEndDay;
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth) {
                minDay = mBeginDay;
                maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth) {
                minDay = 1;
                maxDay = mEndDay;
            } else {
                minDay = 1;
                maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            mDayUnits.clear();
            for (int i = minDay; i <= maxDay; i++) {
                mDayUnits.add(mDecimalFormat.format(i));
            }
            mDayPickerView.setDataList(mDayUnits);

            int selectedDay = getValueInRange(mSelectedTime.get(Calendar.DAY_OF_MONTH), minDay, maxDay);
            mSelectedTime.set(Calendar.DAY_OF_MONTH, selectedDay);
            mDayPickerView.setSelected(selectedDay - minDay);
            if (showAnim) {
                mDayPickerView.startAnim();
            }
        }

        private int getValueInRange(int value, int minValue, int maxValue) {
            if (value < minValue) {
                return minValue;
            } else if (value > maxValue) {
                return maxValue;
            } else {
                return value;
            }
        }
    }
}
