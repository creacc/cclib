package com.creacc.ccdao;

/**
 * Created by Creacc on 2016/10/30.
 */

public class DefaultInstanceGenerator<T> implements EntityInstanceGenerator<T> {

    private Class<T> mEntityClass;

    public DefaultInstanceGenerator(Class<T> entityClass) {
        mEntityClass = entityClass;
    }

    @Override
    public T generateInstance() {
        try {
            return mEntityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
