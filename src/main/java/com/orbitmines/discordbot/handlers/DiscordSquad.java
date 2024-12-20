package com.orbitmines.discordbot.handlers;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.*;
import com.orbitmines.api.database.*;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.tables.TableDiscordGroup;
import com.orbitmines.api.database.tables.TableDiscordGroupData;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.bungeecord.OrbitMinesBungee;
import com.orbitmines.bungeecord.handlers.BungeePlayer;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.DiscordUtils;
import com.orbitmines.spigot.api.utils.Serializer;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.File;
import java.util.*;

public class DiscordSquad {

    private static final List<DiscordSquad> groups = new ArrayList<>();

    public static final int MAX_NAME_CHARACTERS = 100;

    private final DiscordBot discord;

    private final UUID owner;
    private boolean setup;

    private long categoryId;
    private long textChannelId;
    private long voiceChannelId;
    private long roleId;

    private String name;
    private Color color;
    private List<UUID> members;

    public DiscordSquad(DiscordBot discord, UUID owner) {
        groups.add(this);

        this.discord = discord;

        this.owner = owner;
        this.setup = false;

        this.name = getOwner().getPlayerName() + "_SQUAD";
        this.color = Color.random();
        this.members = new ArrayList<>();
    }

    public DiscordSquad(DiscordBot discord, UUID owner, long categoryId, long textChannelId, long voiceChannelId, long roleId, String name, Color color, List<UUID> members, boolean list) {
        if (list)
            groups.add(this);

        this.discord = discord;

        this.owner = owner;
        this.setup = true;

        this.categoryId = categoryId;
        this.textChannelId = textChannelId;
        this.voiceChannelId = voiceChannelId;
        this.roleId = roleId;

        this.name = name;
        this.color = color;
        this.members = members;
    }

    public Category getCategory() {
        return getGuild().getCategoryById(categoryId);
    }

    public TextChannel getTextChannel() {
        return getGuild().getTextChannelById(textChannelId);
    }

    public VoiceChannel getVoiceChannel() {
        return getGuild().getVoiceChannelById(voiceChannelId);
    }

    public Role getRole() {
        return getGuild().getRoleById(roleId);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return this.color.getChatColor() + "@" + this.name;
    }

    public void updateCategoryName() {
        Category category = getCategory();
        String categoryName = generateCategoryName();

        /* Only update when the category name changed */
        if (!category.getName().equals(categoryName))
            category.getManager().setName(categoryName).queue(channel -> discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage("Successfully changed " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + "'s Discord Squad Category to » **" + categoryName + "**.").queue());
    }

    public void setName(String name) {
        this.name = name;
        getRole().getManager().setName(name).queue(role -> {
            getTextChannel().getManager().setName(name.toLowerCase()).queue(channel -> {
                getVoiceChannel().getManager().setName(name).queue(channel2 -> {
                    discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage("Successfully changed " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + "'s Discord Squad Name to " + getRole().getAsMention() + " » " + getTextChannel().getAsMention() + ".").queue();
                    getTextChannel().sendMessage("Successfully changed name to " + getRole().getAsMention() + " » " + getTextChannel().getAsMention() + ".").queue();
                });
            });
        });

        Database.get().update(Table.DISCORD_GROUP, new Set(TableDiscordGroup.NAME, this.name), new Where(TableDiscordGroup.UUID, this.owner.toString()));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        getRole().getManager().setColor(color.getAwtColor()).queue(role -> {
            String mention = discord.getRole(BotToken.DEFAULT, DiscordBot.CustomRole.valueOf(color.toString())).getAsMention();
            discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage("Successfully changed " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + "'s Discord Squad Color to " + mention + " » " + getRole().getAsMention() + ".").queue();
            getTextChannel().sendMessage("Successfully changed color to " + mention + " » " + getRole().getAsMention() + ".").queue();
        });
        Database.get().update(Table.DISCORD_GROUP, new Set(TableDiscordGroup.COLOR, this.color.toString()), new Where(TableDiscordGroup.UUID, this.owner.toString()));
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID member) {
        String memberName = getName(member);
        sendMessage("Discord", Color.LIME, memberName + " §7is toegevoegd aan " + getDisplayName() + "§7.", memberName + " §7has joined " + getDisplayName() + "§7.");
        sendMessage("Discord", member, Color.LIME, "Je bent " + getDisplayName() + "§7 gejoined.", "You have joined " + getDisplayName() + "§7.");

        String name = DiscordUtils.getDisplay(discord, BotToken.DEFAULT, member);
        User user = discord.getLinkedUser(BotToken.DEFAULT, member);
        if (user != null)
            name += " (" + user.getAsMention() + ")";

        getTextChannel().sendMessage(discord.getRole(BotToken.DEFAULT, DiscordBot.CustomRole.JOIN).getAsMention() + " " + name + " has joined " + getRole().getAsMention() + ".").queue();

        this.members.add(member);
        updateMembers(member);
    }

    public void removeMember(UUID member, boolean forced) {
        /* Remove first so player doesn't get two leave messages */
        this.members.remove(member);

        String memberName = getName(member);
        if (forced) {
            sendMessage("Discord", Color.LIME, memberName + " §7is verwijderd van " + getDisplayName() + "§7.", memberName + " §7has been removed from " + getDisplayName() + "§7.");
            sendMessage("Discord", member, Color.LIME, "Je bent van " + getDisplayName() + "§7 verwijderd.", "You have been removed from " + getDisplayName() + "§7.");
        } else {
            sendMessage("Discord", Color.LIME, memberName + " §7heeft " + getDisplayName() + "§7 verlaten.", memberName + " §7has left " + getDisplayName() + "§7.");
            sendMessage("Discord", member, Color.LIME, "Je bent " + getDisplayName() + "§7 verlaten.", "You have left " + getDisplayName() + "§7.");
        }

        String name = DiscordUtils.getDisplay(discord, BotToken.DEFAULT, member);
        User user = discord.getLinkedUser(BotToken.DEFAULT, member);
        if (user != null)
            name += " (" + user.getAsMention() + ")";

        getTextChannel().sendMessage(discord.getRole(BotToken.DEFAULT, DiscordBot.CustomRole.LEAVE).getAsMention() + " " + name + " has left " + getRole().getAsMention() + ".").queue();

        updateMembers(member);
    }

    private void updateMembers(UUID member) {
        User user = discord.getLinkedUser(BotToken.DEFAULT, member);
        if (user != null)
            discord.updateRanks(user);

        Database.get().update(Table.DISCORD_GROUP, new Set(TableDiscordGroup.MEMBERS, Serializer.serializeUUIDList(this.members)), new Where(TableDiscordGroup.UUID, this.owner.toString()));
    }

    public void sendMessage(String prefix, Color color, String... messages) {
        sendMessage(prefix, owner, color, messages);

        for (UUID member : members) {
            sendMessage(prefix, member, color, messages);
        }
    }

    public void sendMessage(String prefix, UUID uuid, Color color, String... messages) {
        BungeePlayer omp = BungeePlayer.getPlayer(uuid);

        if (omp != null)
            omp.sendMessage(prefix, color, messages);
    }

    public List<BungeePlayer> getPlayers() {
        List<BungeePlayer> list = new ArrayList<>();

        BungeePlayer owner = BungeePlayer.getPlayer(this.owner);
        if (owner != null)
            list.add(owner);

        for (UUID uuid : members) {
            BungeePlayer member = BungeePlayer.getPlayer(uuid);
            if (member != null)
                list.add(member);
        }

        return list;
    }

    private String getName(UUID uuid) {
        BungeePlayer omp = BungeePlayer.getPlayer(uuid);

        if (omp != null)
            return omp.getName();

        CachedPlayer player = CachedPlayer.getPlayer(uuid);
        return player.getRankPrefixColor().getChatColor() + player.getPlayerName();
    }

    public void setup(BungeePlayer omp) {
        omp.sendMessage("Discord", Color.BLUE, "Discord Squad aan het maken...", "Setting up Discord Squad...");

        Guild guild = getGuild();
        GuildController controller = guild.getController();

        controller.createRole().setName(this.name).setColor(this.color.getAwtColor()).setMentionable(true).queue(role -> {
            roleId = role.getIdLong();

            omp.sendMessage("Discord", Color.LIME, "Rol gemaakt.", "Successfully created Role.");

            Collection<Permission> everyone = Collections.singletonList(Permission.MESSAGE_READ);
            Collection<Permission> read = Collections.singletonList(Permission.MESSAGE_READ);

            controller.createCategory(generateCategoryName())
                    .addPermissionOverride(role, read, new ArrayList<>())
                    .addPermissionOverride(discord.getRole(BotToken.DEFAULT, StaffRank.OWNER), read, new ArrayList<>())
                    .addPermissionOverride(discord.getRole(BotToken.DEFAULT, StaffRank.ADMIN), read, new ArrayList<>())
                    .addPermissionOverride(discord.getRole(BotToken.DEFAULT, StaffRank.MODERATOR), read, new ArrayList<>())

                    .addPermissionOverride(guild.getPublicRole(), new ArrayList<>(), everyone)

                    .queue(channel -> {
                categoryId = channel.getIdLong();
                Category category = getCategory();

                omp.sendMessage("Discord", Color.LIME, "Categorie gemaakt.", "Successfully created Category.");

                controller.createTextChannel(this.name.toLowerCase()).setParent(category).queue(channel1 -> {
                    textChannelId = channel1.getIdLong();
                    omp.sendMessage("Discord", Color.LIME, "Text Channel gemaakt.", "Successfully created Text Channel.");

                    controller.createVoiceChannel(this.name).setParent(category).queue(channel2 -> {
                        voiceChannelId = channel2.getIdLong();
                        omp.sendMessage("Discord", Color.LIME, "Voice Channel gemaakt.", "Successfully created Voice Channel.");

                        Database.get().insert(Table.DISCORD_GROUP, this.owner.toString(), categoryId + "", textChannelId + "", voiceChannelId + "", roleId + "", this.name, this.color.toString(), Serializer.serializeUUIDList(members));

                        this.setup = true;
                        omp.updateRanks();

                        String stringUuid = Database.get().getString(Table.DISCORD_GROUP_DATA, TableDiscordGroupData.SELECTED, new Where(TableDiscordGroupData.UUID, omp.getUUID().toString()));

                        if (stringUuid.equals("")) {
                            Database.get().update(Table.DISCORD_GROUP_DATA, new Set(TableDiscordGroupData.SELECTED, omp.getUUID().toString()), new Where(TableDiscordGroupData.UUID, omp.getUUID().toString()));
                            OrbitMinesBungee.getBungee().getMessageHandler().dataTransfer(PluginMessage.UPDATE_DISCORD_GROUP_DATA, omp.getPlayer(), omp.getUUID().toString());
                        }

                        discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage("Successfully created **Discord Squad** for " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + " » " + role.getAsMention() + " » " + getTextChannel().getAsMention() + ".").queue();

                        TextChannel server = getTextChannel();

                        File file = DiscordBot.Images.ORBITMINES_LOGO.getFile("orbitmines_logo");
                        if (file != null)
                           server.sendFile(file).queue();

                        User user = discord.getLinkedUser(BotToken.DEFAULT, omp.getUUID());

                        server.sendMessage("Welcome to " + role.getAsMention() + " " + user.getAsMention() + "!").queue();
                        server.sendMessage(" • Use **/discordsquad** in game to manage your squad").queue();
                        server.sendMessage(" • In order to type in your squad type **!<message>** in game.").queue();
                        server.sendMessage(" • Use **!list** in Discord to view all online players in game.").queue();

                        omp.sendMessage("Discord", Color.BLUE, "Je §9§lDiscord Squad§7 staat klaar!", "Your §9§lDiscord Squad§7 is ready for use!");
                    }, throwable -> {
                        throwable.printStackTrace();
                        omp.sendMessage("Discord", Color.RED, "Er is een probleem met het maken van je Discord Squad.", "An error occurred while creating your Discord Squad.");
                        broadcastError("Voice Channel", true);
                    });
                }, throwable -> {
                    throwable.printStackTrace();
                    omp.sendMessage("Discord", Color.RED, "Er is een probleem met het maken van je Discord Squad.", "An error occurred while creating your Discord Squad.");
                    broadcastError("Text Channel", true);
                });
            }, throwable -> {
                throwable.printStackTrace();
                omp.sendMessage("Discord", Color.RED, "Er is een probleem met het maken van je Discord Squad.", "An error occurred while creating your Discord Squad.");
                broadcastError("Category", true);
            });
        }, throwable -> {
            throwable.printStackTrace();
            omp.sendMessage("Discord", Color.RED, "Er is een probleem met het maken van je Discord Squad.", "An error occurred while creating your Discord Squad.");
            broadcastError("Role", true);
        });
    }

    public void destroy(BungeePlayer omp) {
        omp.sendMessage("Discord", Color.BLUE, "Discord Squad aan het verwijderen...", "Deleting Discord Squad...");

        /* Delete Squad */
        Database.get().delete(Table.DISCORD_GROUP, new Where(TableDiscordGroup.UUID, this.owner.toString()));
        /* Delete Squad from Selected */
        Database.get().update(Table.DISCORD_GROUP_DATA, new Set(TableDiscordGroupData.SELECTED, ""), new Where(TableDiscordGroupData.SELECTED, this.owner.toString()));

        getVoiceChannel().delete().queue((channel) -> {
            omp.sendMessage("Discord", Color.LIME, "Voice Channel verwijderd.", "Successfully deleted Voice Channel.");

            getTextChannel().getManager().setName("archive_" + this.owner.toString() + "_" + DateUtils.now().getTime()).setParent(discord.getCategory(BotToken.DEFAULT, "ARCHIVE")).queue((channel2) -> {
                omp.sendMessage("Discord", Color.LIME, "Text Channel verwijderd.", "Successfully deleted Text Channel.");

                getCategory().delete().queue((category) -> {
                    omp.sendMessage("Discord", Color.LIME, "Category verwijderd.", "Successfully deleted Category.");

                    getRole().delete().queue((role) -> {
                        groups.remove(this);

                        omp.sendMessage("Discord", Color.LIME, "Role verwijderd.", "Successfully deleted Role.");

                        omp.sendMessage("Discord", Color.BLUE, "Je §9§lDiscord Squad§7 is verwijderd!", "Your §9§lDiscord Squad§7 has been deleted!");

                        discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage("Successfully deleted **Discord Squad** for " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + ", Archived Text Channel » " + getTextChannel().getAsMention() + ".").queue();
                    }, (throwable -> {
                        throwable.printStackTrace();
                        omp.sendMessage("Discord", Color.RED, "Er is een probleem met het verwijderen van je Discord Squad.", "An error occurred while deleting your Discord Squad.");
                        broadcastError("Role", false);
                    }));
                }, (throwable -> {
                    throwable.printStackTrace();
                    omp.sendMessage("Discord", Color.RED, "Er is een probleem met het verwijderen van je Discord Squad.", "An error occurred while deleting your Discord Squad.");
                    broadcastError("Category", false);
                }));
            }, (throwable -> {
                throwable.printStackTrace();
                omp.sendMessage("Discord", Color.RED, "Er is een probleem met het verwijderen van je Discord Squad.", "An error occurred while deleting your Discord Squad.");
                broadcastError("Text Channel", false);
            }));
        }, (throwable -> {
            throwable.printStackTrace();
            omp.sendMessage("Discord", Color.RED, "Er is een probleem met het verwijderen van je Discord Squad.", "An error occurred while deleting your Discord Squad.");
            broadcastError("Voice Channel", false);
        }));
    }

    public boolean isSetup() {
        return setup;
    }

    private JDA getJDA() {
        return discord.getJDA(BotToken.DEFAULT);
    }

    private Guild getGuild() {
        return discord.getGuild(BotToken.DEFAULT);
    }

    public CachedPlayer getOwner() {
        return CachedPlayer.getPlayer(this.owner);
    }

    public UUID getOwnerUUID() {
        return this.owner;
    }

    private String generateCategoryName() {
        CachedPlayer owner = getOwner();
        String rankName = owner.getRankName();

        return (rankName.equals(VipRank.NONE.getName()) ? "" : rankName + " ") + owner.getPlayerName();
    }

    private void broadcastError(String type, boolean creating) {
        discord.getChannel(BotToken.DEFAULT, DiscordBot.ChannelType.private_server_log).sendMessage(discord.getEmote(BotToken.DEFAULT, DiscordBot.CustomEmote.barrier).getAsMention() + " " + discord.getRole(BotToken.DEFAULT, StaffRank.OWNER).getAsMention() + ", " + discord.getRole(BotToken.DEFAULT, StaffRank.DEVELOPER).getAsMention() + " » An error occurred while " + (creating ? "creating" : "deleting") + " a **" + type + "** for " + DiscordUtils.getDisplay(discord, BotToken.DEFAULT, this.owner) + "'s **Discord Squad**.").queue();
    }

    public static List<DiscordSquad> getGroups() {
        return groups;
    }

    public static DiscordSquad getGroup(UUID owner) {
        for (DiscordSquad group : groups) {
            if (group.getOwnerUUID().toString().equals(owner.toString()))
                return group;
        }
        return null;
    }

    public static List<DiscordSquad> getGroups(UUID member) {
        List<DiscordSquad> list = new ArrayList<>();

        for (DiscordSquad group : groups) {
            if (group.getOwnerUUID().toString().equals(member.toString()) || group.getMembers().contains(member))
                list.add(group);
        }

        return list;
    }

    public static DiscordSquad getGroup(TextChannel textChannel) {
        for (DiscordSquad group : groups) {
            if (group.getTextChannel().getId().equals(textChannel.getId()))
                return group;
        }
        return null;
    }

    public static DiscordSquad getSelected(UUID member) {
        String selectedString = Database.get().getString(Table.DISCORD_GROUP_DATA, TableDiscordGroupData.SELECTED, new Where(TableDiscordGroupData.UUID, member.toString()));

        if (selectedString.equals(""))
            return null;

        return getGroup(UUID.fromString(selectedString));
    }

    public static void setup(DiscordBot discord) {
        for (Map<Column, String> entry : Database.get().getEntries(Table.DISCORD_GROUP)) {
            UUID owner = UUID.fromString(entry.get(TableDiscordGroup.UUID));
            long categoryId = Long.parseLong(entry.get(TableDiscordGroup.CATEGORY_ID));
            long textChannelId = Long.parseLong(entry.get(TableDiscordGroup.TEXT_CHANNEL_ID));
            long voiceChannelId = Long.parseLong(entry.get(TableDiscordGroup.VOICE_CHANNEL_ID));
            long roleId = Long.parseLong(entry.get(TableDiscordGroup.ROLE_ID));
            String name = entry.get(TableDiscordGroup.NAME);
            Color color = Color.valueOf(entry.get(TableDiscordGroup.COLOR));
            List<UUID> members = Serializer.parseUUIDList(entry.get(TableDiscordGroup.MEMBERS));

            new DiscordSquad(discord, owner, categoryId, textChannelId, voiceChannelId, roleId, name, color, members, true);
        }
    }

    public static DiscordSquad getFromDatabase(DiscordBot discord, UUID owner) {
        if (!Database.get().contains(Table.DISCORD_GROUP, TableDiscordGroup.UUID, new Where(TableDiscordGroup.UUID, owner.toString())))
            return null;

        Map<Column, String> entry = Database.get().getValues(Table.DISCORD_GROUP, new Where(TableDiscordGroup.UUID, owner.toString()));

        long categoryId = Long.parseLong(entry.get(TableDiscordGroup.CATEGORY_ID));
        long textChannelId = Long.parseLong(entry.get(TableDiscordGroup.TEXT_CHANNEL_ID));
        long voiceChannelId = Long.parseLong(entry.get(TableDiscordGroup.VOICE_CHANNEL_ID));
        long roleId = Long.parseLong(entry.get(TableDiscordGroup.ROLE_ID));
        String name = entry.get(TableDiscordGroup.NAME);
        Color color = Color.valueOf(entry.get(TableDiscordGroup.COLOR));
        List<UUID> members = Serializer.parseUUIDList(entry.get(TableDiscordGroup.MEMBERS));

        DiscordSquad group = new DiscordSquad(discord, owner, categoryId, textChannelId, voiceChannelId, roleId, name, color, members, false);
        groups.remove(group);
        return group;
    }

    public static boolean exists(DiscordBot discord, BotToken token, String name) {
        if (name.equalsIgnoreCase("everyone") || name.equalsIgnoreCase("here") || discord.getGuild(token).getRolesByName(name, true).size() != 0)
            return true;

        /* As backup, but shouldn't happen */
        for (Map<Column, String> entry : Database.get().getEntries(Table.DISCORD_GROUP, TableDiscordGroup.NAME)) {
            if (entry.get(TableDiscordGroup.NAME).equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static List<DiscordSquad> getGroupsFromDatabase(DiscordBot discord, UUID member) {
        List<DiscordSquad> list = new ArrayList<>();

        for (Map<Column, String> entry : Database.get().getEntries(Table.DISCORD_GROUP)) {
            UUID owner = UUID.fromString(entry.get(TableDiscordGroup.UUID));
            List<UUID> members = Serializer.parseUUIDList(entry.get(TableDiscordGroup.MEMBERS));

            if (!owner.toString().equals(member.toString()) && !members.contains(member))
                continue;

            long categoryId = Long.parseLong(entry.get(TableDiscordGroup.CATEGORY_ID));
            long textChannelId = Long.parseLong(entry.get(TableDiscordGroup.TEXT_CHANNEL_ID));
            long voiceChannelId = Long.parseLong(entry.get(TableDiscordGroup.VOICE_CHANNEL_ID));
            long roleId = Long.parseLong(entry.get(TableDiscordGroup.ROLE_ID));
            String name = entry.get(TableDiscordGroup.NAME);
            Color color = Color.valueOf(entry.get(TableDiscordGroup.COLOR));

            list.add(new DiscordSquad(discord, owner, categoryId, textChannelId, voiceChannelId, roleId, name, color, members, false));
        }

        return list;
    }
}
