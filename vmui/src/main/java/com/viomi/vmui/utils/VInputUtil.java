package com.viomi.vmui.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.nio.charset.StandardCharsets;

/**
 * Created by Ljh on 2020/12/14.
 * Description:文本输入工具类
 */
public class VInputUtil {
    public static InputFilter Utf8InputFilter(int maxLen) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                //转换成UTF8的长度
                int destLen = dest.toString().getBytes(StandardCharsets.UTF_8).length;
                int sourceLen = source.toString().getBytes(StandardCharsets.UTF_8).length;
                Log.e("filter", String.valueOf(destLen + sourceLen));
                if (destLen + sourceLen > maxLen) {
                    return "";
                }
                //回退键
                if (source.length() < 1 && (dend - dstart >= 1)) {
                    return dest.subSequence(dstart, dend - 1);
                }
                //返回输入的内容
                return source;
            }
        };
        return filter;
    }
}

