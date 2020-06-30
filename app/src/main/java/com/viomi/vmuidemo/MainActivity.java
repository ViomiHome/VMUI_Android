package com.viomi.vmuidemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.viomi.vmui.Dialog.VDialog;
import com.viomi.vmui.Dialog.VDialogAction;
import com.viomi.vmui.VActionSheet;
import com.viomi.vmui.VPopup;
import com.viomi.vmui.VToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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
                "Colors",
                "Button",
                "NavBar",
                "Tabs",
                "NoticeBar",
                "TabBar",
                "Segment",
                "Dialog",
                "TextDialog"
                , "Picker"
                , "MultiPicker"
                , "LocationPicker"
                , "DatePicker"
                , "DatePicker-Month"
                , "DatePicker-Day"
                , "Popup"
                , "ActionSheet1"
                , "ActionSheet2"
                , "ShareSheet"
                , "Toast"
                , "SearchBar"
                , "EmptyPage"
                , "LoadingPage"
                , "Itemview"
                , "Inputitemview"
                , "InputArea"
                , "Loading"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        listview.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, data));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getBaseContext(), ColorsActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getBaseContext(), ButtonActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getBaseContext(), NavbarActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getBaseContext(), TabsActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getBaseContext(), NoticeBarActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getBaseContext(), TabbarActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(getBaseContext(), SegmentActivity.class));
                        break;
                    case 7:
                        new VDialog.MessageDialogBuilder(MainActivity.this)
                                .addAction("确认", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .setHeadImage(R.mipmap.ic_launcher)
                                .setTitle("弹窗标题")
                                .setMessage("弹窗内容，告知当前状态、信息和解决方法，联系电话：18888888888")
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
                                .setShowTipsText(true)

                                //.setTransformationMethod(new PasswordTransformationMethod())
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
                        builder1.addAction("确认", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
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
                                .setEndTimestamp("2029-05-01")
                                .setTitle("弹窗标题")
                                .showDialog();
                        break;
                    case 13:
                        VDialog.DatePickerBuilder builder5 = new VDialog.DatePickerBuilder(MainActivity.this);
                        builder5.addAction("辅助操作", VDialogAction.ACTION_PROP_NEGATIVE, new VDialogAction.ActionListener() {
                            @Override
                            public void onClick(Dialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).addAction("主操作", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
                            @Override
                            public void onClick(Dialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                                .setShowDateFormatPattern(1)
                                .setBeginTimestamp("2020-01-01")
                                .setEndTimestamp("2020-12-31")
                                .setTitle("弹窗标题")
                                .setSubTitle("副标题")
                                .showDialog();
                        break;
                    case 14:
                        VDialog.DatePickerBuilder builder6 = new VDialog.DatePickerBuilder(MainActivity.this);
                        builder6.addAction("辅助操作", VDialogAction.ACTION_PROP_NEGATIVE, new VDialogAction.ActionListener() {
                            @Override
                            public void onClick(Dialog dialog, int index) {
                                dialog.dismiss();
                            }
                        }).addAction("主操作", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
                            @Override
                            public void onClick(Dialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                                .setShowDateFormatPattern(2)
                                .setBeginTimestamp("2020-01-01")
                                .setEndTimestamp("2020-01-31")
                                .setTitle("弹窗标题")
                                .setSubTitle("副标题")
                                .showDialog();
                        break;
                    case 15:
                        //Popup
                        String Text = "【全新启动页】生活家居好管家，品质颜值都在手\r\n【购物车优化】优惠多少更清晰，规格重选不用愁\r\n【类目页优化】类目上新先知晓，好货热销等你瞅\r\n【礼品卡优化】友谊要有保鲜期，转赠礼品马上拆";
                        View content = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_layout_popup, null);
                        TextView tv = content.findViewById(R.id.tv_update_content);
                        tv.setText(Text);
                        new VPopup.VPopupBuilder(MainActivity.this)
                                .setHeadImage(R.mipmap.ic_launcher_round)
                                .setTitle("发现新版本v3.2.2.1")
                                .setShowCloseImage(true)
                                .setShowOperateButton(true)
                                .setCanceledOnTouchOutside(false)
                                .setOperateButtonText("立即升级")
                                .setContentLayoutView(content)
                                .setViewDismissClickListener(new VPopup.VPopupBuilder.OnViewDismissClickListener() {
                                    @Override
                                    public void onViewDismissClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .setViewClickListener(new VPopup.VPopupBuilder.OnViewClickListener() {
                                    @Override
                                    public void onViewClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .showPopup();
                        break;
                    case 16:
                        new VActionSheet.MultiButtonActionSheetBuilder(MainActivity.this)
                                .setTitle("这是一个清晰的描述")
                                .addItem("按钮1")
                                .addItem("按钮2")
                                .addItem("按钮3")
                                .addAction("确认", VDialogAction.ACTION_PROP_POSITIVE, new VDialogAction.ActionListener() {
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
                    case 17:
                        new VActionSheet.MultiButtonActionSheetBuilder(MainActivity.this)
                                .setTitle("这是一个清晰的描述，可以为一行也可以为两行，这仅仅是一个清晰的描述")
                                .addAction("危险按钮", VDialogAction.ACTION_PROP_DANGER, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                        VToast.makeErrorText(MainActivity.this, "您按下的是：危险按钮", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addAction("按钮1", VDialogAction.ACTION_PROP_COMMON, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                        VToast.makeErrorText(MainActivity.this, "您按下的是：按钮1", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addAction("按钮2", VDialogAction.ACTION_PROP_COMMON, new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                        VToast.makeErrorText(MainActivity.this, "您按下的是：按钮2", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addAction("主操作", new VDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(Dialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .setActionContainerOrientation(LinearLayout.VERTICAL)
                                .showActionSheet();

                        break;
                    case 18:
                        new VActionSheet.ShareSheetDialogBuilder(MainActivity.this)
                                .addItem("QQ空间", R.mipmap.ic_launcher)
                                .addItem("微信", R.mipmap.ic_launcher)
                                .addItem("腾讯微博", R.mipmap.ic_launcher)
                                .addItem("新浪微博", R.mipmap.ic_launcher)
                                .setOnSheetItemClickListener(new VActionSheet.ShareSheetDialogBuilder.OnSheetItemClickListener() {
                                    @Override
                                    public void onItemClick(Dialog dialog, String selected) {
                                        //Toast.makeText(MainActivity.this, "您选择的内容是：" + selected, Toast.LENGTH_SHORT).show();
                                        //dialog.dismiss();
                                        VToast.makeSuccessText(MainActivity.this, "您选择的内容是：" + selected, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .showActionSheet();
                        break;
                    case 19:
                        startActivity(new Intent(getBaseContext(), ToastActivity.class));
                        break;
                    case 20://SearchBar
                        startActivity(new Intent(getBaseContext(), SearchBarActivity.class));
                        break;
                    case 21://EmptyPage
                        startActivity(new Intent(getBaseContext(), EmtyActivity.class));
                        break;
                    case 22://LoadingPage
                        startActivity(new Intent(getBaseContext(), LoadingActivity.class));
                        break;
                    case 23://itemview
                        startActivity(new Intent(getBaseContext(), ItemViewActivity.class));
                        break;
                    case 24://inputitemview
                        startActivity(new Intent(getBaseContext(), InputItemActivity.class));
                        break;
                    case 25://inputArea
                        startActivity(new Intent(getBaseContext(), InputAreaActivity.class));
                        break;
                    case 26://Loading
                        startActivity(new Intent(getBaseContext(), LoadingStatusActivity.class));
                        break;
                }
            }
        });
    }
}
