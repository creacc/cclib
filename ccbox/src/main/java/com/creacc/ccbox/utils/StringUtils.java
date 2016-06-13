package com.creacc.ccbox.utils;

import android.text.TextUtils;

/**
 * Created by apple on 2016/1/28.
 */
public class StringUtils {

    public static String join(String space, String... contents) {
        StringBuilder builder = new StringBuilder();
        String lastContent = "";
        for (int i = 0; i < contents.length; i++) {
            String content = contents[i];
            if (TextUtils.isEmpty(content) == false && content.equals(lastContent) == false) {
                if (i > 0) {
                    builder.append(space);
                }
                builder.append(content);
                lastContent = content;
            }
        }
        return builder.toString();
    }

    public static <T> String join(String space, StringJoinAdapter adapter) {
        int count = adapter.getCount();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                builder.append(space);
            }
            builder.append(adapter.getValue(i));
        }
        return builder.toString();
    }

    public static String[] toArray(StringJoinAdapter adapter) {
        int count = adapter.getCount();
        String[] array = new String[count];
        for (int i = 0; i < count; i++) {
            array[i] = adapter.getValue(i);
        }
        return array;
    }

    public interface StringJoinAdapter {

        int getCount();

        String getValue(int index);
    }
}
