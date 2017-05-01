package com.creacc.ccdaolite.query;

/**
 * Created by yanhaifeng on 16-11-7.
 */
public class WhereCase implements QueryCase {

    private static final String DEFAULT_WHERE_OPERATOR = "=";
    private static final String WHERE_HEADER = " WHERE ";
    private static final String WHERE_AND = " AND ";
    private static final String WHERE_OR = " OR ";
    private static final String VALUE_SURROUND = "'";
    private static final String FUNCTION_PREFIX = "(";

    private StringBuilder mCaseBuilder;

    public WhereCase(String whereColumn, String whereValue) {
        this(whereColumn, whereValue, DEFAULT_WHERE_OPERATOR);
    }

    public WhereCase(String whereColumn, String whereValue, String whereOperator) {
        mCaseBuilder = new StringBuilder();
        putCase(whereColumn, whereValue, whereOperator);
    }

    public WhereCase and(String whereColumn, String whereValue) {
        return and(whereColumn, whereValue, DEFAULT_WHERE_OPERATOR);
    }

    public WhereCase and(String whereColumn, String whereValue, String whereOperator) {
        mCaseBuilder.append(WHERE_AND);
        return putCase(whereColumn, whereValue, whereOperator);
    }

    public WhereCase or(String whereColumn, String whereValue) {
        return or(whereColumn, whereValue, DEFAULT_WHERE_OPERATOR);
    }

    public WhereCase or(String whereColumn, String whereValue, String whereOperator) {
        mCaseBuilder.append(WHERE_OR);
        return putCase(whereColumn, whereValue, whereOperator);
    }

    private WhereCase putCase(String whereColumn, String whereValue, String whereOperator) {
        mCaseBuilder.append(whereColumn)
                .append(whereOperator);
        if (whereValue.startsWith(FUNCTION_PREFIX)) {
            mCaseBuilder.append(whereValue);
        } else {
            mCaseBuilder.append(VALUE_SURROUND)
                    .append(whereValue)
                    .append(VALUE_SURROUND);
        }
        return this;
    }

    public String toSelectionSQL() {
        return mCaseBuilder.toString();
    }

    @Override
    public String toQuerySQL() {
        return WHERE_HEADER + mCaseBuilder.toString();
    }

    public interface WhereCaseAdapter<T> {

        WhereCase getWhereCase(T entity);

    }
}
