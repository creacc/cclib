package com.creacc.ccdaolite.query;

/**
 * Created by yanhaifeng on 16-11-8.
 */
public class LimitCase implements QueryCase {

    private static final String LIMIT_HEADER = " LIMIT ";
    private static final String LIMIT_DELIMITER = ",";

    public String mLimitCase;

    public LimitCase(int range) {
        mLimitCase = new StringBuilder(LIMIT_HEADER)
                .append(range)
                .toString();
    }

    public LimitCase(int start, int range) {
        mLimitCase = new StringBuilder(LIMIT_HEADER)
                .append(start)
                .append(LIMIT_DELIMITER)
                .append(range)
                .toString();
    }

    @Override
    public String toQuerySQL() {
        return mLimitCase;
    }
}
