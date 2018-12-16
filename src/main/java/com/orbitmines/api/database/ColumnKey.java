package com.orbitmines.api.database;

import java.util.HashMap;
import java.util.Set;

public class ColumnKey extends Column {

    private HashMap<Action, Response> responses;

    private Table table;
    private Key key;

    public ColumnKey(String name, Type type, Table table, Key key, int... args) {
        super(name, type, args);
        this.responses = new HashMap<>();
        this.table = table;
        this.key = key;
    }

    public void addResponse(Action action, Response response){
        if(key != Key.FOREIGN)
            throw new IllegalStateException("You cannot use this method if the key is a primary key!");

        this.responses.put(action, response);
    }

    /* GETTERS */
    Set<Action> getActions(){
        return responses.keySet();
    }

    Response getResponse(Action action){
        return responses.getOrDefault(action, null);
    }

    Table getTable() {
        return table;
    }

    /* BOOLEANS */
    boolean isPrimaryKey(){
        return key == Key.PRIMARY;
    }

    boolean isForeignKey(){
        return key == Key.FOREIGN;
    }

    /* SUB ENUMS */
    public enum Action {

        /*actions*/
        ON_DELETE("ON DELETE"),
        ON_UPDATE("ON UPDATE");

        private String query;

        Action(String query){
            this.query = query;
        }

        @Override
        public String toString() {
            return query;
        }

    }

    public enum Response {

        NO_ACTION("NO ACTION"),
        UPDATE("UPDATE"),
        CASCADE("CASCADE"),
        SET_NULL("SET NULL");

        private String query;

        Response(String query){
            this.query = query;
        }

        @Override
        public String toString() {
            return query;
        }
    }

    public enum Key {

        PRIMARY,
        FOREIGN;

    }
}
