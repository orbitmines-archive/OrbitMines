package com.orbitmines.api.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database database;

    private final String hostName;
    private final int port;
    private final String databaseName;
    private final String userName;
    private final String password;

    private Connection connection;

    public Database(String hostName, int port, String databaseName, String userName, String password) {
        this.hostName = hostName;
        this.port = port;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;

        database = this;
    }

    public static Database get() {
        return database;
    }

    /*
         Connection methods
    */
    public void openConnection() {
        try {
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", hostName, port, databaseName), userName, password);
        } catch (SQLException e) {
            System.out.println("Database driver couldn't be found!");
            System.out.println("Shutting down application....");
            System.exit(1);
        }
    }

    private void checkConnection() throws SQLException {
        if (connection.isClosed())
            openConnection();
    }

    /*
        Init methods
    */
    public void setupTables() {
        for (Table table : Table.ALL) {

            /* Create table if it does not exist */
            StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(table.toString()).append("` ("); //CREATE TABLE IF NOT EXISTS `Naam` (column1 tinyint, column2 int);

            for (int i = 0; i < table.getColumns().length; i++) {
                if (i != 0)
                    query.append(", ");

                Column column = table.getColumns()[i];
                query.append(column.toTypeString(true));


            }

            for (Constraint constraint : table.getConstraints())
                query.append(constraint.toString());

            query.append(");");

            this.executeQuery(query.toString());
        }

        setupColumns();
    }

    private void setupColumns() {
        DatabaseMetaData data;

        try {
            checkConnection();

            data = connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        for (Table table : Table.ALL) {

            for (int i = 1; i < table.getColumns().length; i++) {
                Column column = table.getColumns()[i];

                try {
                    if (data.getColumns(null, null, table.toString(), column.toString()).next())
                        continue;

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String query = String.format("ALTER TABLE `%s` ADD COLUMN %s AFTER `%s`;", table.toString(), column.toTypeString(false), table.getColumns()[i - 1].toString());

                this.executeQuery(query);

                //TODO: CHECK IF DEFAULT VALUE ACTUALLY WORKS IN QUERY!;
            }

            //TODO: CHECK IF THIS WORKS!
            for(Constraint cs : table.getConstraints()){
                this.executeQuery(String.format("IF OBJECT_ID('dbo.%s', 'C') IS NOT NULL ALTER TABLE dbo.%s ADD %s", cs.getName(), cs.getPrimaryTable().toString(), cs.toString()));
            }
        }
    }

    /*
        CONTAINS METHOD
    */
    public boolean contains(From from, Column[] columns, Where... wheres) {
        String query = String.format("SELECT %s FROM `%s` %s;", toString(columns), from.toString(), toString(wheres));

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            boolean b1 = rs.next();
            rs.close();

            return b1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean contains(Table table, Column column, Where... wheres) {
        return contains(table, new Column[]{column}, wheres);
    }

    /*
        INPUT METHODS
    */
    public void insert(Table table, String... values) {
        this.executeQuery(String.format("INSERT INTO `%s` (%s) %s;", table.toString(), toString(table.getColumns()), table.values(values)));
    }

    public void update(Table table, Set set, Where... wheres) {
        update(table, new Set[]{set}, wheres);
    }

    public void update(Table table, Set[] sets, Where[] wheres) {
        try {
            checkConnection();

            Statement s = connection.createStatement();
            s.executeUpdate(String.format("UPDATE `%s` %s%s", table.toString(), toString(sets), toString(wheres)));


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /*
        GETTERS (SQL-Based)
    */
    public int getCount(From from, Where... wheres) {
        int count = 0;

        String query = String.format("SELECT COUNT(*) AS count FROM `%s` %s;", from.toString(), toString(wheres));

        try {
            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public long getLongSum(From from, Column column, Where... wheres) {
        long sum = 0;

        String query = String.format("SELECT SUM(%s) AS sum FROM `%s`%s", column.toString(), from.toString(), toString(wheres));

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                sum = rs.getLong("sum");
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sum;
    }

    /*
        GETTERS
    */
    public <T extends Object> T get(From from, Column column, Where... wheres){
        T genericType = null;

        String query = String.format("SELECT `%s` FROM `%s`%s", column.toString(), from.toString(), toString(wheres));

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                genericType = (T) rs.getObject(column.toString());
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return genericType;

    }

    /**
        getValues() methods
     */
    public Map<Column, Object> getValues(Table table, Where... wheres) {
        return getValues(table, table.getColumns(), wheres);
    }

    public Map<Column, Object> getValues(From from, Column column, Where... wheres) {
        return getValues(from, new Column[]{column}, wheres);
    }

    public <T extends Object> Map<Column, Object> getValues(From from, Column[] columns, Where... wheres) {
        Map<Column, Object> values = new HashMap<>();

        String query = String.format("SELECT %s FROM `%s` %s", toString(columns), from.toString(), toString(wheres));

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                for (Column column : columns)
                    values.put(column, (T) rs.getObject(column.toString()));
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    /*
        getEntries() methods
    */
    public List<Map<Column, Object>> getEntries(Table table, Where... wheres) {
        return getEntries(table, table.getColumns(), wheres);
    }

    public List<Map<Column, Object>> getEntries(From from, Column column, Where... wheres) {
        return getEntries(from, new Column[]{column}, wheres);
    }

    public <T extends Object> List<Map<Column, Object>> getEntries(From from, Column[] columns, Where... wheres) {
        List<Map<Column, Object>> values = new ArrayList<>();

        String query = String.format("SELECT %s FROM `%s` %s", toString(columns), from.toString(), toString(wheres));

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                Map<Column, Object> entry = new HashMap<>();
                for (Column column : columns)
                    entry.put(column, (T) rs.getObject(column.toString()));

                values.add(entry);
            }

            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    /**
        PRIVATE METHODS
    */
    private void executeQuery(String query) {
        try {
            checkConnection();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String toString(Set[] sets) {
        if(sets == null || sets.length == 0)
            return "";

        StringBuilder sb = new StringBuilder(" SET ");

        for(int i = 0; i < sets.length; i++){
            if(i != 0)
                sb.append(",");

            sb.append(sets[i].toString());
        }

        return sb.toString();
    }

    private String toString(Column[] columns) { //column1, column2, column3,
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("`");

        for (int i = 0; i < columns.length; i++) {
            if (i != 0)
                stringBuilder.append("`,`");

            stringBuilder.append(columns[i].toString());
        }

        stringBuilder.append("`");

        return stringBuilder.toString();
    }

    private String toString(Where[] wheres) {
        if (wheres == null || wheres.length == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder(" WHERE ");

        for (int i = 0; i < wheres.length; i++) {
            if (i != 0)
                stringBuilder.append(" AND ");

            stringBuilder.append(wheres[i].toString());
        }

        return stringBuilder.toString();
    }
}
