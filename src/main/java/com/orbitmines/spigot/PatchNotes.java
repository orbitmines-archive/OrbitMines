package com.orbitmines.spigot;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.TableServerData;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.discordbot.DiscordBot;
import com.orbitmines.discordbot.utils.BotToken;
import com.orbitmines.discordbot.utils.ColorUtils;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.chat.ComponentMessage;
import com.orbitmines.spigot.api.handlers.itembuilders.WrittenBookBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PatchNotes {

    private final static int DAYS_PATCH_IS_NEW = 2;

    private final OrbitMines orbitMines;

    private Instance hubInstance;

    private Map<Server, List<Instance>> instances;

    public PatchNotes(OrbitMines orbitMines) {
        this.orbitMines = orbitMines;
        this.instances = new HashMap<>();

        add(new PatchNotes.Instance(Server.HUB, "v1.0.0", "OM Release", "The first OrbitMines release, containing commands, stats, loot and many other features!", "2018-07-02",
                new PatchNotes.Feature("PATCH NOTES", Color.RED, "With the patch notes users will be able to view additions and changes to OrbitMines more easily through the 'Patch Notes' npc in game or through Discord.", "https://i.imgur.com/EsOVG5S.png")
        ));

        add(new PatchNotes.Instance(Server.SURVIVAL, "v1.0.0", "Survival Release", "The first Survival release, presenting new systems for claiming and warps and many other features!", "2018-07-02",
               new Feature("", Color.RED, "")
        ));
    }

    public Instance getHubInstance() {
        return hubInstance;
    }

    public void add(Instance instance) {
        if (!instances.containsKey(instance.server))
            instances.put(instance.server, new ArrayList<>());

        instances.get(instance.server).add(instance);
    }

    public void build() {
        hubInstance = new Instance(null, null, null, null, null);

        for (Server server : instances.keySet()) {
            for (Instance instance : instances.get(server)) {
                instance.build(this);
            }

            /* Broadcast Patch Notes on Discord */
            if (orbitMines.getServerHandler().getServer() != server)
                continue;

            Instance latest = getLatest(server);

            if (!Database.get().contains(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, server.toString()), new Where(TableServerData.TYPE, "LATEST_PATCH")))
                Database.get().insert(Table.SERVER_DATA, server.toString(), "LATEST_PATCH", latest.version);
            else if (Database.get().getString(Table.SERVER_DATA, TableServerData.DATA, new Where(TableServerData.SERVER, server.toString()), new Where(TableServerData.TYPE, "LATEST_PATCH")).equals(latest.version))
                continue;

            broadcastToDiscord(latest);
        }
    }

    public void open(OMPlayer omp, Server server, String version) {
        get(server, version).open(omp);
    }

    public Instance get(Server server, String version) {
        for (Instance instance : instances.get(server)) {
            if (instance.getVersion().equals(version))
                return instance;
        }
        return null;
    }

    public boolean exists(Server server, String version) {
        return get(server, version) != null;
    }

    public Instance getLatest(Server server) {
        return instances.get(server).get(0);
    }

    public List<Instance> getLatest() {
        List<Instance> latest = new ArrayList<>();

        for (Server server : instances.keySet()) {
            Instance latestForServer = getLatest(server);

            if (latest.size() == 0) {
                latest.add(latestForServer);
                continue;
            }

            long time1 = latestForServer.getDate().getTime();
            long time2 = latest.get(0).getDate().getTime();

            if (time1 > time2)
                latest.clear();

            if (time1 >= time2)
                latest.add(latestForServer);
        }

        return latest;
    }

    private void broadcastToDiscord(Instance instance) {
        DiscordBot discord = orbitMines.getServerHandler().getDiscord();
        BotToken token = orbitMines.getServerHandler().getToken();

        TextChannel channel = discord.getChannel(token, DiscordBot.ChannelType.patch_notes);

        channel.sendMessage("@everyone **" + instance.getServer().getName() + " " + instance.getVersion() + "** has been released! » \"**" + instance.getName() + "**\"").queue();
        channel.sendMessage("_" + DateUtils.SIMPLE_FORMAT.format(instance.getDate()) + ": " + instance.getDescription() + "_").queue();

        for (Feature feature : instance.getFeatures()) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(feature.getName());
            builder.setDescription(feature.getDescription());
            builder.setColor(ColorUtils.from(feature.getColor()));

            if (feature.getImageLink() != null)
                builder.setImage(feature.getImageLink());

            channel.sendMessage(builder.build()).queue();
        }
    }

    public static class Instance {

        private final Server server;

        private final String version;
        private final String name;
        private final String description;

        private final Date date;

        private Page[] pages;
        private Feature[] features;

        private final Map<Language, WrittenBookBuilder> book;

        public Instance(Server server, String version, String name, String description, String date, Feature... features) {
            this.server = server;
            this.version = version;
            this.name = name;
            this.description = description;
            this.date = date == null ? null : DateUtils.parse(DateUtils.SIMPLE_FORMAT, date);
            this.features = features;
            this.book = new HashMap<>();

            this.pages = new Page[features.length];
            for (int i = 0; i < features.length; i++) {
                Feature feature = features[i];

                ComponentMessage image = new ComponentMessage();

                if (feature.getImageLink() != null)
                    image.add(new Message("§7§o[IMAGE]"), ClickEvent.Action.OPEN_URL, new Message(feature.getImageLink()), HoverEvent.Action.SHOW_TEXT, new Message("§7§oKlik hier om de image te openen.", "§7§oClick here to open the image."));

                this.pages[i] = new Page(
                        new ComponentMessage()
                                .add(new Message(server.getDisplayName() + "§r §0" + version)),
                        new ComponentMessage(),
                        new ComponentMessage()
                                .add(new Message(feature.color.getChatColor() + "§l" + feature.getName())),
                        new ComponentMessage()
                                .add(new Message("§0" + feature.description)),
                        new ComponentMessage(),
                        image
                );
            }
        }

        public Server getServer() {
            return server;
        }

        public String getVersion() {
            return version;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Date getDate() {
            return date;
        }

        public Page[] getPages() {
            return pages;
        }

        public Feature[] getFeatures() {
            return features;
        }

        public WrittenBookBuilder getBook(Language language) {
            return book.get(language).clone();
        }

        public boolean isNew() {
            Date newTo = new Date(this.date.getTime() + TimeUnit.DAYS.toMillis(DAYS_PATCH_IS_NEW));

            return DateUtils.now().compareTo(newTo) < 0;
        }

        public void open(OMPlayer omp) {
            OrbitMines.getInstance().getNms().customItem().openBook(omp.getPlayer(), getBook(omp.getLanguage()));
        }

        public void build(PatchNotes patchNotes) {
            for (Language language : Language.values()) {
                WrittenBookBuilder builder = new WrittenBookBuilder(1, "§f", "§8§lOrbit§7§lMines");

                if (server != null) {
                    ComponentMessage versions = new ComponentMessage();

                    if (patchNotes.instances.get(server).size() != 1) {
                        Instance prev = getPrevious(patchNotes);
                        Instance next = getNext(patchNotes);

                        if (prev != null)
                            versions.add(new Message("§0« " + prev.getVersion()), ClickEvent.Action.RUN_COMMAND, new Message("/patchnotes " + server.toString() + " " + prev.getVersion()), HoverEvent.Action.SHOW_TEXT, new Message("§7" + new Message("Versie", "Version").lang(language) + ": " + prev.getVersion() + "\n" + "§7" + new Message("Naam", "Name").lang(language) + ": " + prev.getName()));
                        else
                            versions.add(new Message("§2X " + version), HoverEvent.Action.SHOW_TEXT, new Message("§aHuidige Versie", "§aCurrent Version"));

                        versions.add(new Message("        "));

                        if (next != null)
                            versions.add(new Message("§0" + next.getVersion() + " »"), ClickEvent.Action.RUN_COMMAND, new Message("/patchnotes " + server.toString() + " " + next.getVersion()), HoverEvent.Action.SHOW_TEXT, new Message("§7" + new Message("Versie", "Version").lang(language) + ": " + next.getVersion() + "\n" + "§7" + new Message("Naam", "Name").lang(language) + ": " + next.getName()));
                        else
                            versions.add(new Message("§2" + version + " X"), HoverEvent.Action.SHOW_TEXT, new Message("§aHuidige Versie", "§aCurrent Version"));
                    }

                    builder.addPage(new Page(
                            new ComponentMessage()
                                    .add(new Message("     §c§lPATCH NOTES")),
                            new ComponentMessage()
                                    .add(new Message("       §8§lOrbit§7§lMines")),
                            new ComponentMessage(),
                            new ComponentMessage(),
                            new ComponentMessage(),
                            new ComponentMessage()
                                    .add(new Message(" §8Server: " + server.getDisplayName())),
                            new ComponentMessage()
                                    .add(new Message(" §8" + new Message("Versie", "Version").lang(language) + ": §0" + version)),
                            new ComponentMessage()
                                    .add(new Message(" §8" + new Message("Datum", "Date").lang(language) + ": §0" + DateUtils.SIMPLE_FORMAT.format(date))),
                            new ComponentMessage()
                                    .add(new Message(" §8" + new Message("Naam", "Name").lang(language) + ": §0\"" + name + "\"")),
                            new ComponentMessage(),
                            new ComponentMessage(),
                            new ComponentMessage(),
                            versions

                    ).serialize(language));
                }

                for (Page page : pages) {
                    builder.addPage(page.serialize(language));
                }

                this.book.put(language, builder);
            }
        }

        private boolean isLatest(PatchNotes patchNotes) {
            return patchNotes.instances.get(server).indexOf(this) == 0;
        }

        private Instance getPrevious(PatchNotes patchNotes) {
            int index = patchNotes.instances.get(server).indexOf(this);

            if (patchNotes.instances.get(server).size() > (index + 1))
                return patchNotes.instances.get(server).get(index + 1);

            return null;
        }

        private Instance getNext(PatchNotes patchNotes) {
            return isLatest(patchNotes) ? null : patchNotes.instances.get(server).get(patchNotes.instances.get(server).indexOf(this) - 1);
        }
    }

    public static class Feature {

        private final String name;
        private final Color color;
        private final String description;
        private final String imageLink;

        public Feature(String name, Color color, String description) {
            this(name, color, description, null);
        }

        public Feature(String name, Color color, String description, String imageLink) {
            this.name = name;
            this.color = color;
            this.description = description;
            this.imageLink = imageLink;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }

        public String getDescription() {
            return description;
        }

        public String getImageLink() {
            return imageLink;
        }
    }

    public static class Page {

        private ComponentMessage[] lines;

        public Page(ComponentMessage ... lines) {
            this.lines = lines;//TODO MAKE IT EASY
        }

        public ComponentMessage[] getLines() {
            return lines;
        }

        public TextComponent serialize(Language language) {
            TextComponent[] tcs = new TextComponent[this.lines.length];

            for (int i = 0; i < lines.length; i++) {
                TextComponent tc = lines[i].getAsComponent(language);
                tc.addExtra("\n");
                tcs[i] = tc;
            }

            return new TextComponent(tcs);
        }
    }
}
