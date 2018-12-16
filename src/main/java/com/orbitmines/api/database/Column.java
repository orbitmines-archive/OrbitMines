package com.orbitmines.api.database;

public class Column {

    private final String name;
    private final Type type;
    private final String defaultValue;
    private final int[] args;

    public Column(String name, Type type, int... args) {
        this(name, type, null, args);
    }

    public Column(String name, Type type, String defaultValue, int... args) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.args = args;
    }

    /* GETTERS */
    public Type getType() {
        return type;
    }

    public int[] getArgs() {
        return args;
    }

    private String getDefaultValue() {
        return defaultValue;
    }

    /* toString() METHODS */
    @Override
    public String toString() {
        return name;
    }

    String toTypeString(boolean table) {
        StringBuilder query = new StringBuilder(String.format("`%s` %s", toString(), type.toString(args)));

        if (defaultValue != null)
            query.append(table ? "SET " : "")
                    .append("DEFAULT ")
                    .append(defaultValue);

        return query.toString();
    }

    /* SUB-ENUM */
    public enum Type {

        /* Numeric */
        INT,
        TINYINT,
        SMALLINT,
        MEDIUMINT,
        BIGINT,
        FLOAT,
        DOUBLE,
        DECIMAL,

        /* Date and Time */
        DATE,/* YYYY-MM-DD */
        DATETIME,/* YYYY-MM-DD HH:MM:SS */
        TIMESTAMP,
        TIME,/* HH:MM:SS */
        YEAR,

        /* String */
        CHAR,
        VARCHAR,
        TEXT,
        BLOB,
        TINYTEXT,
        TINYBLOB,
        MEDIUMTEXT,
        MEDIUMBLOB,
        LONGTEXT,
        LONGBLOB,
        ENUM;

        @Override
        public String toString() {
            return toString((int[]) null);
        }

        public String toString(int... args) {
            if (args == null || args.length == 0)
                return super.toString();

            StringBuilder stringBuilder = new StringBuilder(super.toString());
            stringBuilder.append("(");

            for (int i = 0; i < args.length; i++) {
                if (i != 0)
                    stringBuilder.append(",");

                stringBuilder.append(args[i]);
            }

            stringBuilder.append(")");

            return stringBuilder.toString();
        }
    }
}
