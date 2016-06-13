package com.creacc.ccdao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库句柄<br/>
 * 用于承载数据库文件的句柄<br/>
 * <br/>
 * Created by Creacc on 2016/5/8.
 */
public class CCDao {

    private CCDaoContext mDaoContext;

    /**
     * 构造器
     * @param context 上下文
     * @param path 数据库文件路径（包含文件名）
     * @param version 数据库版本
     */
    public CCDao(Context context, String path, int version) {
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, path, null, version) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        mDaoContext = new CCDaoContext(helper);
    }

    /**
     * 获取数据库上下文<br/>
     * 用于进行数据库操作
     * @return 数据库上下文
     */
    public CCDaoContext getContext() {
        return mDaoContext;
    }

    /**
     * 从标记{@link CCDaoEntity}注解的类中生成数据库句柄
     * @param context 上下文
     * @param dir 数据库目录（不包含文件名，文件名可在{@link CCDaoEntity}中指定）
     * @param entityClass 实体类
     * @return 数据库句柄<br/>
     * {@link CCDaoException} 如果实体类未进行{@link CCDaoEntity}标注，或者{@link CCDaoEntity}中version字段小于等于1时抛出此异常
     */
    public static CCDao fromEntityClass(Context context, String dir, Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(CCTableEntity.class)) {
            if (entityClass.isAnnotationPresent(CCDaoEntity.class)) {
                CCDaoEntity annotation = entityClass.getAnnotation(CCDaoEntity.class);
                String format = dir.endsWith("/") ? "%s%s.db" : "%s/%s.db";
                String path = String.format(format, dir, annotation.name());
                return new CCDao(context, path, annotation.version());
            } else {
                throw new CCDaoException("Please pass version or use CCDaoEntity annotation");
            }
        } else {
            throw new CCDaoException("Please use CCTableEntity annotation");
        }
    }

}
