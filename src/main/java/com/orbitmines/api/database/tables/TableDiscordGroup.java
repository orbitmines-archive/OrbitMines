package com.orbitmines.api.database.tables;

import com.orbitmines.api.database.Column;
import com.orbitmines.api.database.Table;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TableDiscordGroup extends Table {

    public static final Column UUID = new Column("UUID", Column.Type.VARCHAR, 36);
    public static final Column CATEGORY_ID = new Column("CategoryId", Column.Type.BIGINT);
    public static final Column TEXT_CHANNEL_ID = new Column("TextChannelId", Column.Type.BIGINT);
    public static final Column VOICE_CHANNEL_ID = new Column("VoiceChannelId", Column.Type.BIGINT);
    public static final Column ROLE_ID = new Column("RoleId", Column.Type.BIGINT);
    public static final Column NAME = new Column("Name", Column.Type.VARCHAR, 100);
    public static final Column COLOR = new Column("Color", Column.Type.VARCHAR, 32);
    public static final Column MEMBERS = new Column("Members", Column.Type.TEXT);

    public TableDiscordGroup() {
        super("DiscordGroup", UUID, CATEGORY_ID, TEXT_CHANNEL_ID, VOICE_CHANNEL_ID, ROLE_ID, NAME, COLOR, MEMBERS);
    }
}
