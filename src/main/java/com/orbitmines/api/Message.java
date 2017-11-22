package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Message {

    public static String FORMAT(String prefix, Color prefixColor, String message) {
        return prefix == null ? message : prefixColor.getChatColor() + prefix + " » §7" + message;
    }

    public static final Message UNKNOWN_COMMAND = new Message("Commands", Color.RED, "Die command bestaat niet. Gebruik §6/help §7voor help.", "Unknown Command. Use §6/help §7for help.");

    public static Message REQUIRE_RANK(VipRank vipRank) {
        return new Message("Rank", Color.RED, "§7Je moet een " + vipRank.getRankString() + " VIP§7 zijn om dit te doen!", "§7You have to be " + (vipRank == VipRank.IRON || vipRank == VipRank.EMERALD ? "an" : "a") + " " + vipRank.getRankString() + " VIP§7 to do this!");
    }

    public static final Message ENTER_2FA = new Message("2FA", Color.LIME, "Type je 2FA code in de chat.", "Enter your 2FA code in chat.");
    public static final String PREFIX_2FA = "2FA";

    private final String prefix;
    private final Color prefixColor;
    private final String[] messages;

    public Message(String... messages) {
        this(null, null, messages);
    }

    public Message(String prefix, Color prefixColor, String... messages) {
        this.prefix = prefix;
        this.prefixColor = prefixColor;
        this.messages = messages;
    }

    public String getPrefix() {
        return prefix;
    }

    public Color getPrefixColor() {
        return prefixColor;
    }

    public String lang(Language language) {
        String message = messages.length < language.ordinal() + 1 ? messages[messages.length -1] : messages[language.ordinal()];
        return FORMAT(prefix, prefixColor, message);
    }
}
