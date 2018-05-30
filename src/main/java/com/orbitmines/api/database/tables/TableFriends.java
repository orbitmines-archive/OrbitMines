package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class TableFriends extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column FRIENDS = new Column("Friends", Column.Type.TEXT); /* Max 1500 */
    public static final Column FAVORITE = new Column("Favorite", Column.Type.TEXT); /* Max 1500 */
    public static final Column SENT_INVITES = new Column("SentInvites", Column.Type.TEXT); /* Max 1500 */
    public static final Column INVITES = new Column("Invites", Column.Type.TEXT); /* Max 1500 */

    public TableFriends() {
        super("Friends", UUID, FRIENDS, FAVORITE, SENT_INVITES, INVITES);
    }

    public String[] values(String uuid, String friends, String favorite, String sentInvites, String invites) {
        return new String[] { uuid, friends, favorite, sentInvites, invites };
    }
}
