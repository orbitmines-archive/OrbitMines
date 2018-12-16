package com.orbitmines.api.database;

public class Where<T> extends Set {

    private final Operator operator;

    public Where(Column column, T value) {
        this(Operator.EQUALS, column, value);
    }

    @SafeVarargs
    public Where(Operator operator, Column column, T... value) {
        super(column, value);

        if(value.length == 0)
            throw new IllegalArgumentException("Cannot search on 0 values!");


        if(value.length > 1 && operator != Operator.IN)
            throw new IllegalArgumentException("Cannot search into an array of values without IN operator!");

        this.operator = operator;
    }

    /* OVERRIDABLE */
    @Override
    public String toString() {
        Object[] obj;

        if(getValue() instanceof Object[])
            obj = new Object[] {getValue()};
        else
            obj = (Object[]) getValue();

        StringBuilder values = new StringBuilder();

        for(int i = 0; i <  obj.length; i++){
            if(i > 0 || i == (obj.length - 1))
                values.append(",");

            values.append(obj[i]);
        }

        return String.format("`%s`%s'%s'", column, operator.getOperator(), values.toString());
    }

    /* SUB ENUM */
    public enum Operator {

        EQUALS("="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">="),
        IS(" IS "),
        IS_NOT(" IS NOT "),
        LESSER_THAN("<"),
        LESSER_THAN_OR_EQUAL("<="),
        LIKE(" LIKE "),
        NOT_EQUAL("!="),
        IN(" IN ");

        private final String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }
    }
}
