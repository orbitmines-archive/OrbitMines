package com.orbitmines.api.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Join implements From {

    private List<Join> childs;

    private Type type;

    private Table leftTable;
    private Column leftColumn;

    private Table rightTable;
    private Column rightColumn;

    public Join(Type type, Table leftTable, Column leftColumn, Table rightTable, Column rightColumn) {
        this.type = type;

        this.leftTable = leftTable;
        this.leftColumn = leftColumn;

        this.rightTable = rightTable;
        this.rightColumn = rightColumn;

        if(!Arrays.asList(leftTable.getColumns()).contains(leftColumn))
            throw new IllegalStateException("Left Table doesn't contain the left column!");

        if(!Arrays.asList(rightTable.getColumns()).contains(rightColumn))
            throw new IllegalStateException("Right Table doesn't contain the right right column!");

        if(!leftColumn.toTypeString(false).equals(rightColumn.toTypeString(false)))
            throw new IllegalStateException("The left column isn't the same type as the right column!");

        this.childs = new ArrayList<>();
    }

    public Join(Type type, ColumnKey pk, ColumnKey fk){
        this(type, pk.getTable(), pk, fk.getTable(), fk);
    }

    /* OVERRIDABLE METHODS */
    @Override
    public String toString() {
        return String.format("%s %s %s ON (%s.%s = %s.%s) %s", leftTable, type, rightTable, leftTable, leftColumn, rightTable, rightColumn, getChilds());
    }

    /* SETTERS */
    public void addChild(Join join){
        this.childs.add(join);
    }

    /* GETTERS */
    private String getChilds(){
        StringBuilder sb = new StringBuilder();

        for(Join join : childs){

            Table commonTable = join.leftTable.equals(leftTable) ? join.leftTable : join.rightTable.equals(rightTable) ? join.rightTable : null;
            Column commonColumn = join.leftColumn.equals(leftColumn) ? join.leftColumn : join.rightColumn.equals(rightColumn) ? join.rightColumn : null;

            if(commonColumn == null || commonTable == null)
                throw new IllegalStateException("One of the joins doesn't have anything in common with the parent Join!");

            Table unusedTable = commonTable.equals(leftTable) ? join.rightTable : join.leftTable;
            Column unusedColumn = commonColumn.equals(leftColumn) ? join.rightColumn : join.leftColumn;

            sb.append(String.format("%s %s ON (%s.%s = %s.%s) ", join.type, unusedTable, commonTable, commonColumn, unusedTable, unusedColumn));
            sb.append(join.getChilds());
        }

        return sb.toString();
    }

    /* SUB-ENUM */
    public enum Type {

        INNER_JOIN("INNER JOIN"),
        LEFT_JOIN("LEFT JOIN"),
        LEFT_OUTER_JOIN("LEFT OUTER JOIN"),
        RIGHT_JOIN("RIGHT JOIN"),
        RIGHT_OUTER_JOIN("RIGHT_OUTER_JOIN"),
        FULL_OUTER_JOIN("FULL OUTER JOIN");

        private String query;

        Type(String query){
            this.query = query;
        }

        @Override
        public String toString() {
            return query;
        }
    }
}
