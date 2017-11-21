package com.orbitmines.api.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Database {

    private static Database database;

    private final String hostName;
    private final int port;
    private final String databaseName;
    private final String userName;
    private final String password;

    private Connection connection;

    public Database(String hostName, int port, String databaseName, String userName, String password) {
        database = this;

        this.hostName = hostName;
        this.port = port;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void openConnection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName, userName, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void checkConnection() throws SQLException {
        if (connection.isClosed())
            openConnection();
    }

    public void setupTables() {
        for (Table table : Table.ALL) {
            /* Create table if it does not exist */
            StringBuilder tableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(table.toString()).append("` (");

            for (int i = 0; i < table.getColumns().length; i++) {
                if (i != 0)
                    tableQuery.append(", ");

                Column column = table.getColumns()[i];
                tableQuery.append(column.toTypeString());
            }
            tableQuery.append(");");

            String query = tableQuery.toString();
            try {
                checkConnection();

                PreparedStatement ps = connection.prepareStatement(query);
                ps.execute();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
            /* Add columns if they don't exist (First column always stays the same! (Basically a primary key) */
            for (int i = 1; i < table.getColumns().length; i++) {
                Column column = table.getColumns()[i];

                try {
                    if (data.getColumns(null, null, table.toString(), column.toString()).next())
                        /* Column exists, so we move on. */
                        continue;

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    continue;
                }

                String query = "ALTER TABLE `" + table.toString() + "` ADD COLUMN " + column.toTypeString() + " AFTER " + "`" + table.getColumns()[i - 1].toString() + "`;";

                try {
                    checkConnection();

                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.execute();
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static Database get() {
        return database;
    }

    public boolean contains(Table table, Column column, Where... wheres) {
        return contains(table, new Column[]{column}, wheres);
    }

    public boolean contains(Table table, Column[] columns, Where... wheres) {
        String query = "SELECT " + toString(columns) + " FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            boolean bl = rs.next();
            rs.close();

            return bl;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void insert(Table table, String... values) {
        String query = "INSERT INTO `" + table.toString() + "` (" + toString(table.getColumns()) + ")" + table.values(values) + ";";

        try {
            checkConnection();

            PreparedStatement ps = connection.prepareStatement(query);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(Table table, Where... wheres) {
        String query = "DELETE FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            Statement s = connection.createStatement();
            s.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Table table, Set set, Where... wheres) {
        update(table, new Set[]{set}, wheres);
    }

    public void update(Table table, Set[] sets, Where... wheres) {
        try {
            checkConnection();

            Statement s = connection.createStatement();
            s.executeUpdate("UPDATE `" + table.toString() + "`" + toString(sets) + toString(wheres) + ";");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getCount(Table table, Where... wheres) {
        int count = 0;

        String query = "SELECT COUNT(*) AS count FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    public int getInt(Table table, Column column, Where... wheres) {
        int integer = 0;

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                integer = rs.getInt(column.toString());
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return integer;
    }

    public String getString(Table table, Column column, Where... wheres) {
        String string = "";

        String query = "SELECT `" + column.toString() + "` FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                string = rs.getString(column.toString());
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return string;
    }

    public Map<Column, String> getValues(Table table, Where... wheres) {
        return getValues(table, table.getColumns(), wheres);
    }

    public Map<Column, String> getValues(Table table, Column column, Where... wheres) {
        return getValues(table, new Column[] { column }, wheres);
    }

    public Map<Column, String> getValues(Table table, Column[] columns, Where... wheres) {
        Map<Column, String> values = new HashMap<>();

        String query = "SELECT " + toString(columns) + " FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                for (Column column : columns) {
                    values.put(column, rs.getString(column.toString()));
                }
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    public List<Map<Column, String>> getEntries(Table table, Where... wheres) {
        return getEntries(table, table.getColumns(), wheres);
    }

    public List<Map<Column, String>> getEntries(Table table, Column column, Where... wheres) {
        return getEntries(table, new Column[] { column }, wheres);
    }

    public List<Map<Column, String>> getEntries(Table table, Column[] columns, Where... wheres) {
        List<Map<Column, String>> values = new ArrayList<>();

        String query = "SELECT " + toString(columns) + " FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                Map<Column, String> entry = new HashMap<>();
                for (Column column : columns) {
                    entry.put(column, rs.getString(column.toString()));
                }
                values.add(entry);
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    public List<Map<Column, Integer>> getIntEntries(Table table, Where... where) {
        return getIntEntries(table, table.getColumns(), where);
    }

    public List<Map<Column, Integer>> getIntEntries(Table table, Column column, Where... wheres) {
        return getIntEntries(table, new Column[] { column }, wheres);
    }

    public List<Map<Column, Integer>> getIntEntries(Table table, Column[] columns, Where... wheres) {
        List<Map<Column, Integer>> values = new ArrayList<>();

        String query = "SELECT " + toString(columns) + " FROM `" + table.toString() + "`" + toString(wheres) + ";";

        try {
            checkConnection();

            ResultSet rs = connection.prepareStatement(query).executeQuery();

            while (rs.next()) {
                Map<Column, Integer> entry = new HashMap<>();
                for (Column column : columns) {
                    entry.put(column, rs.getInt(column.toString()));
                }
                values.add(entry);
            }

            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }

    private String toString(Where... wheres) {
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

    private String toString(Set... sets) {
        if (sets == null || sets.length == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder(" SET ");

        for (int i = 0; i < sets.length; i++) {
            if (i != 0)
                stringBuilder.append(",");

            stringBuilder.append(sets[i].toString());
        }

        return stringBuilder.toString();
    }

    private String toString(Column... columns) {
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
}
