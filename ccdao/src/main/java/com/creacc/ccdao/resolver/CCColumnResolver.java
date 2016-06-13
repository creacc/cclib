package com.creacc.ccdao.resolver;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;

/**
 * 数据库列解析器
 * 用于解析数据库列数据
 *
 * Created by Creacc on 2016/5/8.
 */
public abstract class CCColumnResolver<T> {

    /**
     * 默认数据解析器Map
     * 用于存储基础数据类型的解析器
     */
    private static final HashMap<Class<?>, Class<? extends CCColumnResolver>> DEFAULT_MAP =
            new HashMap<Class<?>, Class<? extends CCColumnResolver>>();

    /**
     * 初始化默认解析器Map
     */
    static {
        DEFAULT_MAP.put(byte.class, ByteResolver.class);
        DEFAULT_MAP.put(Byte.class, ByteResolver.class);

        DEFAULT_MAP.put(short.class, ShortResolver.class);
        DEFAULT_MAP.put(Short.class, ShortResolver.class);

        DEFAULT_MAP.put(float.class, FloatResolver.class);
        DEFAULT_MAP.put(Float.class, FloatResolver.class);

        DEFAULT_MAP.put(double.class, DoubleResolver.class);
        DEFAULT_MAP.put(Double.class, DoubleResolver.class);

        DEFAULT_MAP.put(long.class, LongResolver.class);
        DEFAULT_MAP.put(Long.class, LongResolver.class);

        DEFAULT_MAP.put(int.class, IntegerResolver.class);
        DEFAULT_MAP.put(Integer.class, IntegerResolver.class);

        DEFAULT_MAP.put(String.class, StringResolver.class);

        DEFAULT_MAP.put(byte[].class, ByteArrayResolver.class);
    }

    /**
     * 获取默认数据类型的解析器
     * @param type 数据类型
     * @return 数据解析器
     */
    public static Class<? extends CCColumnResolver> getDefaultSerializer(Class<?> type) {
        return DEFAULT_MAP.get(type);
    }

    /**
     * 数据库列名
     */
    private String mColumnName;

    /**
     * 默认构造器
     * @param name 数据库列名
     */
    public CCColumnResolver(String name) {
        mColumnName = name;
    }

    /**
     * 序列化数据（将应用数据转换为数据库存储类型数据）
     * @param value 应用数据
     * @param values 数据容器 {@link ContentValues}
     */
    public void serialize(T value, ContentValues values) {
        innerSerialize(values, mColumnName, value);
    }

    /**
     * 反序列化数据（将数据库中读取的数据解析成应用数据）
     * @param cursor 数据库指针
     * @return 应用数据
     */
    public T deserialize(Cursor cursor) {
        return innerDeserialize(cursor, cursor.getColumnIndexOrThrow(mColumnName));
    }

    /**
     * 列类型
     * @return 列类型在数据库中的类型名称
     */
    public abstract String columnType();

    /**
     *  内部序列化
     * @param values 数据容器 {@link ContentValues}
     * @param key 数据键
     * @param value 数据值
     */
    protected abstract void innerSerialize(ContentValues values, String key, T value);

    /**
     * 内部反序列化
     * @param cursor 数据库指针
     * @param index 列索引
     * @return 应用数据
     */
    protected abstract T innerDeserialize(Cursor cursor, int index);

    public abstract String getStringValue(T value);
}
