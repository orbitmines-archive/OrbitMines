package com.orbitmines.api.database;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Set {

    protected final Column column;
    protected final String value;

    public Set(Column column, boolean value) {
        this(column, value ? "1" : "0");
    }

    public Set(Column column, int value) {
        this(column, "" + value);
    }


    public Set(Column column, long value) {
        this(column, "" + value);
    }


    public Set(Column column, double value) {
        this(column, "" + value);
    }

    public Set(Column column, String value) {
        this.column = column;
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "`" + column + "`='" + value + "'";
    }
}
