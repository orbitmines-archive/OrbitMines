package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableDiscordGroupData extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column SELECTED = new Column("Selected", Column.Type.VARCHAR, 36);
    public static final Column SENT_INVITES = new Column("SentInvites", Column.Type.TEXT); /* Max 1500 */
    public static final Column INVITES = new Column("Invites", Column.Type.TEXT); /* Max 1500 */

    public TableDiscordGroupData() {
        super("DiscordGroupData", UUID, SELECTED, SENT_INVITES, INVITES);
    }
}
