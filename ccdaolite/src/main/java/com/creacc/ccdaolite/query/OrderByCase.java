package com.creacc.ccdaolite.query;

/**
 * Created by yanhaifeng on 16-11-8.
 */
public class OrderByCase implements QueryCase {

    private static final String ORDER_BY_HEADER = " ORDER BY ";

    public enum Order {

        ASC(" ASC"),
        DESC(" DESC");

        private String mOrderCase;

        Order(String orderCase) {
            mOrderCase = orderCase;
        }
    }

    private String mOrderByCase;

    public OrderByCase(String orderByColumn) {
        this(orderByColumn, Order.ASC);
    }

    public OrderByCase(String orderByColumn, Order order) {
        mOrderByCase = new StringBuffer(ORDER_BY_HEADER)
                .append(orderByColumn)
                .append(order.mOrderCase)
                .toString();
    }

    @Override
    public String toQuerySQL() {
        return mOrderByCase;
    }
}
