package com.orbitmines.api.database;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Where extends Set {

    public Where(Column column, boolean value) {
        super(column, value);
    }

    public Where(Column column, int value) {
        super(column, value);
    }

    public Where(Column column, long value) {
        super(column, value);
    }

    public Where(Column column, String value) {
        super(column, value);
    }
}
