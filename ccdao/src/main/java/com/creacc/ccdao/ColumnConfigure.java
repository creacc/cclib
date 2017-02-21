package com.creacc.ccdao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.creacc.ccdao.annotation.CCColumn;
import com.creacc.ccdao.annotation.CCKeyColumn;
import com.creacc.ccdao.resolver.CCColumnResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Creacc on 2016/5/12.
 */
class ColumnConfigure {

    public static final int FLAG_NORMAL = 0x0000;

    public static final int FLAG_KEY = 0x0001;

    public static final int FLAG_INCREMENT_KEY = 0x0002;

    public String columnName;

    public Field columnField;

    public CCColumnResolver columnResolver;

    public int columnFlag;

    public String whereOperator;

    private ColumnConfigure(String name, Field field, CCColumnResolver resolver) {
        columnName = name;
        columnField = field;
        columnResolver = resolver;
        columnFlag = getColumnFlag(field);
    }

    public boolean isKey() {
        return (columnFlag & FLAG_KEY) != 0;
    }

    public boolean isIncrementKey() {
        return (columnFlag & FLAG_INCREMENT_KEY) != 0;
    }

    public static ColumnConfigure generateConfigure(Field field, CCColumn annotation) {
        String columnName = getColumnName(field, annotation);
        CCColumnResolver columnSerializer = getColumnSerializer(field, annotation, columnName);
        if (columnSerializer != null) {
            return new ColumnConfigure(columnName, field, columnSerializer);
        }
        return null;
    }

    private static int getColumnFlag(Field field) {
        if (field.isAnnotationPresent(CCKeyColumn.class)) {
            CCKeyColumn keyColumn = field.getAnnotation(CCKeyColumn.class);
            if (keyColumn.increment()) {
                return ColumnConfigure.FLAG_INCREMENT_KEY;
            }
            return ColumnConfigure.FLAG_KEY;
        }
        return ColumnConfigure.FLAG_NORMAL;
    }

    @NonNull
    private static String getColumnName(Field field, CCColumn annotation) {
        String name;
        if (annotation == null || TextUtils.isEmpty(name = annotation.name())) {
            name = field.getName();
        }
        return name;
    }

    @Nullable
    private static CCColumnResolver getColumnSerializer(Field field, CCColumn annotation, String columnName) {
        Class<? extends CCColumnResolver> serializerClass;
        if (annotation == null || (serializerClass = annotation.serializer()).equals(CCColumnResolver.class)) {
            serializerClass = CCColumnResolver.getDefaultSerializer(field.getType());
        }
        if (serializerClass != null) {
            try {
                return serializerClass.getConstructor(String.class).newInstance(columnName);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
