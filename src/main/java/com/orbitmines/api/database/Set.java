package com.orbitmines.api.database;

public class Set<T> {

    protected final Column column;
    protected final T value;

    public Set(Column column, T t){
        this.column = column;
        this.value = t;
    }

    /* GETTERS */
    public Column getColumn() {
        return column;
    }

    public T getValue() {
        return value;
    }

    /* OVERRIDABLE */
    @Override
    public String toString() {
        return "`" + column + "`='" + value.toString() + "'";
    }
}
