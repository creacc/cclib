package com.creacc.ccbox.utils;

/**
 * Created by Administrator on 2015/12/18.
 */
public class LoopUtils {

    public static void loop(ILoop loop, int count) {
        int index = 0;
        while (index < count) {
            loop.onLoop(index++);
        }
    }

    public interface ILoop {

        void onLoop(int index);
    }
}
