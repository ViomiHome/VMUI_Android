package com.viomi.vmuidemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.viomi.vmui.Dialog.VDialog;
import com.viomi.vmui.Dialog.VDialogAction;
import com.viomi.vmui.VActionSheet;
import com.viomi.vmui.VToast;
import com.viomi.vmui.utils.VMUIDateFormatUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListView();
    }

    private void initListView() {
        String[] listItems = new String[]{
                "Button",
                "NavBar",
                "Tabs",
                "NoticeBar",
                "TabBar",
                "Segment",
                "SettlementBar",
                "Dialog",
                "TextDialog"
                , "Picker"
                , "MultiPicker"
                , "LocationPicker"
                , "DatePicker"
                , "Popup"
                , "ActionSheet"
                , "ShareSheet"
                , "Toast"
                , "SearchBar"
                , "EmptyPage"
                , "LoadingPage"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        listview.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, data));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), ButtonActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(), TitleActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        new VDialog.MessageDialogBuilder(MainActivity.this)
                                .addAction("确认", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("弹窗标题")
                                .setMessage("这是一个弹窗")
                                .showDialog();
                        break;
                    case 8:
                        VDialog.EditTextDialogBuilder builder = new VDialog.EditTextDialogBuilder(MainActivity.this);
                        builder.addAction("取消", VDialogAction.ACTION_PROP_NEGATIVE, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("确认", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        if (builder.getEditText().getText().toString().isEmpty()) {
                                            Toast.makeText(MainActivity.this, "请输入您的内容", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "您输入的内容是：" + builder.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                })
                                .setPlaceholder("在此输入您的内容")
                                .setTitle("弹窗标题")
                                .setTransformationMethod(new PasswordTransformationMethod())
                                .showDialog();
                        break;
                    case 9:
                        List<String> data = new ArrayList<>();
                        data.add("选项0");
                        data.add("选项1");
                        data.add("选项2");
                        data.add("选项3");
                        data.add("选项4");
                        data.add("选项5");
                        data.add("选项6");
                        VDialog.SingleCheckableDialogBuilder builder1 = new VDialog.SingleCheckableDialogBuilder(MainActivity.this);
                        builder1.addAction("确认", VDialogAction.ACTION_PROP_POSITIVE,new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        Toast.makeText(MainActivity.this, "您选择的内容是：" + builder1.getSelected(), Toast.LENGTH_SHORT).show();
                                        builder1.getPickerView().onDestroy();
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("弹窗标题")
                                .setData(data)
                                .showDialog();
                        break;
                    case 10:
                        List<String> data1 = new ArrayList<>();
                        data1.add("选项0");
                        data1.add("选项1");
                        data1.add("选项2");
                        data1.add("选项3");
                        data1.add("选项4");
                        data1.add("选项5");
                        data1.add("选项6");
                        data1.add("选项7");
                        data1.add("选项8");
                        data1.add("选项9");
                        VDialog.MultiCheckableDialogBuilder builder4 = new VDialog.MultiCheckableDialogBuilder(MainActivity.this);
                        builder4.addAction("确认", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                        Toast.makeText(MainActivity.this, "您选择的内容是：" + builder4.getSelectContent(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setData(data1)
                                .setTitle("弹窗标题")
                                .showDialog();
                        break;
                    case 11:
                        VDialog.LocationPickerDialogBuilder builder3 = new VDialog.LocationPickerDialogBuilder(MainActivity.this);
                        builder3.addAction("确认", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        Toast.makeText(MainActivity.this, "您选择的内容是：" + builder3.getSelectContent(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("弹窗标题")
                                .showDialog();
                        break;
                    case 12:
                        VDialog.DatePickerBuilder builder2 = new VDialog.DatePickerBuilder(MainActivity.this);
                        builder2.addAction("确认", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        Toast.makeText(MainActivity.this, "您选择的内容是：" + builder2.getSelectTime(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .setShowDateFormatPattern(0)
                                .setBeginTimestamp("2001-01-01")
                                .setEndTimestamp("2019-12-31")
                                .setTitle("弹窗标题")
                                .showDialog();
                        break;
                    case 13:
                        //Popup
                        new VActionSheet.MultiButtonActionSheetBuilder(MainActivity.this)
                                .setTitle("这是一个清晰的描述")
                                .addItem("按钮1")
                                .addItem("按钮2")
                                .addItem("按钮3")
                                .addAction("确认", VDialogAction.ACTION_PROP_POSITIVE,new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .setOnSheetItemClickListener(new VActionSheet.MultiButtonActionSheetBuilder.OnSheetItemClickListener() {
                                    @Override
                                    public void onItemClick(Dialog dialog, String selected) {
                                        dialog.dismiss();
                                        VToast.makeErrorText(MainActivity.this, "您按下的按钮是：" + selected, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .showActionSheet();
                        break;
                    case 14:
                        new VActionSheet.MultiButtonActionSheetBuilder(MainActivity.this)
                                .setTitle("这是一个清晰的描述，可以为一行也可以为两行，这仅仅是一个清晰的描述")
                                .addAction("危险按钮", VDialogAction.ACTION_PROP_DANGER, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        index += 1;
                                        VToast.makeText(MainActivity.this, "按下按钮：" + index, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("主操作", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .setActionContainerOrientation(LinearLayout.VERTICAL)
                                .showActionSheet();

                        break;
                    case 15:
                        new VActionSheet.ShareSheetDialogBuilder(MainActivity.this)
                                .addItem("QQ空间",R.mipmap.ic_launcher)
                                .addItem("微信",R.mipmap.ic_launcher)
                                .addItem("腾讯微博",R.mipmap.ic_launcher)
                                .addItem("新浪微博",R.mipmap.ic_launcher)
                                .setOnSheetItemClickListener(new VActionSheet.ShareSheetDialogBuilder.OnSheetItemClickListener() {
                                    @Override
                                    public void onItemClick(Dialog dialog,String selected) {
                                        //Toast.makeText(MainActivity.this, "您选择的内容是：" + selected, Toast.LENGTH_SHORT).show();
                                        //dialog.dismiss();
                                        VToast.makeSuccessText(MainActivity.this, "您选择的内容是：" + selected, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .showActionSheet();
                        break;
                    case 16:
                        startActivity(new Intent(getBaseContext(),ToastActivity.class));
                        break;
                }
            }
        });
    }
}
