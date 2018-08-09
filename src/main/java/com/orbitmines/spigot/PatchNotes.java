package com.orbitmines.spigot;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.Color;
import com.orbitmines.api.Language;
import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

        add(new Instance(Server.HUB, "v1.0.0", "OM Release", "The first OrbitMines release, containing commands, stats, loot and many other features!", "2018-07-23",
                new Feature(
                        "HUB", Color.TEAL,
                        "We have a new Hub fitting our Space Themed server fitting our server and lore built by ReddReaperz.",
                        "https://i.imgur.com/TGpD01Y.jpg"
                ),
                new Feature(
                        "PATCH NOTES", Color.RED,
                        "With the patch notes users will be able to view additions and changes to OrbitMines more easily through the 'Patch Notes' npc in game or through Discord.",
                        "https://i.imgur.com/EsOVG5S.png"
                ),
                new Feature(
                        "SERVER SELECTOR", Color.TEAL,
                        "The Server Selector is the fastest way to switch servers on OrbitMines and we gave it a cool new look.",
                        "https://i.imgur.com/73Z0iVe.jpg"
                ),
                new Feature(
                        "SPACE TURTLE", Color.LIME,
                        "The SpaceTurtle is the way to receive rewards and vote on our server. It also makes the work of our developers a lot easier so that's also fun :D.",
                        "https://i.imgur.com/NuhCkbY.jpg"
                ),
                new Feature(
                        "FRIENDS", Color.AQUA,
                        "You can now add other players as Friends or Favorite friends and instantly see when they join the server, or to easily give them access to your stuff.",
                        "https://i.imgur.com/vXmPhk6.jpg"
                ),
                new Feature(
                        "STATS", Color.LIME,
                        "Through Stats you can view all your statics on OrbitMines, how long you have played, when you joined, the amount of times you voted and a lot more.",
                        "https://i.imgur.com/eXrrtAh.jpg"
                ),
                new Feature(
                        "ACHIEVEMENTS", Color.FUCHSIA,
                        "After a long time of 'Coming Soon' we have added Achievements to OrbitMines which you can access through your Stats.",
                        "https://i.imgur.com/v9SiNeT.jpg"
                ),
                new Feature(
                        "SETTINGS", Color.RED,
                        "You can now specify these settings; Private Messages, Player Visibility, Stats & Language with use of the Friends system.",
                        "https://i.imgur.com/pfh2P1w.jpg"
                ),
                new Feature(
                        "VOTING SYSTEM", Color.BLUE,
                        "We now have an automatic voting system that reward the top 3 players, but also the entire server if the community goal is reached.",
                        "https://i.imgur.com/mb85nN0.jpg"
                ),
                new Feature(
                        "DISCORD", Color.BLUE,
                        "We have created a link between Discord & OrbitMines which allows you to easily chat with players on our server and access statistics from the server.",
                        "https://i.imgur.com/1Zb1zbv.jpg"
                ),
                new Feature(
                        "/msg <player> <message>", Color.BLUE,
                        "Send Private Messages."
                ),
                new Feature(
                        "/discordlink <Name>#<Id>", Color.BLUE,
                        "Link your OrbitMines account with your Discord User."
                ),
                new Feature(
                        "/reply <message>", Color.BLUE,
                        "Reply to a Private Message."
                ),
                new Feature(
                        "/hub", Color.BLUE,
                        "Connect to the Hub."
                ),
                new Feature(
                        "/server <server>", Color.BLUE,
                        "Connect to a specific Server."
                ),
                new Feature(
                        "/list", Color.BLUE,
                        "Show all players online."
                ),
                new Feature(
                        "/website", Color.BLUE,
                        "Display our website url."
                ),
                new Feature(
                        "/shop", Color.BLUE,
                        "Display our shop url."
                ),
                new Feature(
                        "/report <player> <reason>", Color.BLUE,
                        "Report a player for breaking the rules."
                ),
                new Feature(
                        "/rules", Color.BLUE,
                        "Open the Rule Book."
                ),
                new Feature(
                        "/servers", Color.BLUE,
                        "Open the Server Selector."
                ),
                new Feature(
                        "/topvoters", Color.BLUE,
                        "Display the Top Voters."
                ),
                new Feature(
                        "/vote", Color.BLUE,
                        "Display the Vote Links."
                ),
                new Feature(
                        "/loot", Color.BLUE,
                        "Interact with the SpaceTurtle."
                ),
                new Feature(
                        "/friends", Color.BLUE,
                        "Open the Friends GUI."
                ),
                new Feature(
                        "/stats <player>", Color.BLUE,
                        "Open Stats for a specific player."
                ),
                new Feature(
                        "/settings", Color.BLUE,
                        "Open your Settings."
                ),
                new Feature(
                        "/prisms", Color.BLUE,
                        "Display your Prisms."
                ),
                new Feature(
                        "/solars", Color.BLUE,
                        "Display your Solars."
                ),
                new Feature(
                        "/afk (reason)", Color.BLUE,
                        "Go afk, this feature is for IRON+."
                ),
                new Feature(
                        "/nick <name>|off", Color.BLUE,
                        "Set your nickname, this feature is for GOLD+."
                )
        ));

        add(new Instance(Server.SURVIVAL, "v1.0.2", "New Spigot Version & Bug Fixes", "We've fixed a lot of bugs behind the scenes and several bugs noticeable for users.", "2018-08-01",
                new Feature(
                        "SPIGOT", Color.ORANGE,
                        "We have updated to a new version of Spigot 1.13, meaning Boats, Fishing & Minecarts are fixed now!",
                        "https://i.imgur.com/HQrNURk.jpg"
                ),
                new Feature(
                        "HEAD TEXTURES", Color.LIME,
                        "Fixed: Custom Head Textures weren't generating correctly.",
                        "https://i.imgur.com/Oon4z4Q.jpg"
                ),
                new Feature(
                        "REGION SPEED", Color.AQUA,
                        "Added Speed Potion Effect to Region Beacons."
                ),
                new Feature(
                        "HOME PRICE", Color.BLUE,
                        "Increased the price for Homes from ~900~ to 2,500 Prisms."
                ),
                new Feature(
                        "TRUSTING", Color.LIME,
                        "Fixed: Trusting players has finally been fixed!"
                ),
                new Feature(
                        "PROJECTILE PVP", Color.RED,
                        "Fixed: You were able to damage other players with Projectiles, that is no longer possible."
                ),
                new Feature(
                        "SPAWN INTERACTING", Color.LIME,
                        "Fixed: You were able to interact with certain blocks in the lobby, that is no longer possible."
                )
        ));

        add(new Instance(Server.SURVIVAL, "v1.0.1", "Commands, Claim Changes & Bug Fixes", "We've fixed a lot of bugs that occured over the past few days and added a few new features.", "2018-07-25",
                new Feature(
                        "SPIGOT", Color.ORANGE,
                        "We have updated the a new version of Spigot 1.13 and the spigot-related issues should be resolved."
                ),
                new Feature(
                        "LOBBY FOOD", Color.LIME,
                        "Fixed: You weren't able to consume food in the Lobby."
                ),
                new Feature(
                        "CUSTOM ANVIL", Color.SILVER,
                        "Fixed: Using a custom anvil would cost the player experience. Ex: Trusting players."
                ),
                new Feature(
                        "TELEPORT ISSUE", Color.TEAL,
                        "Fixed: /tphere would sometimes teleport you instead of the player you want to teleport."
                ),
                new Feature(
                        "MAX HOMES", Color.ORANGE,
                        "Fixed: Setting a home that already exists when having your maximum amount of homes would deny you to replace your home."
                ),
                new Feature(
                        "TRUSTING", Color.LIME,
                        "Fixed: Trusting players didn't work.."
                ),
                new Feature(
                        "SMALL BUGS", Color.LIME,
                        "Fixed: Several other small bugs were fixed."
                ),
                new Feature(
                        "TNT", Color.RED,
                        "You can now explode TNT within your own claim."
                ),
                new Feature(
                        "CREEPERS", Color.RED,
                        "Creepers will now explode in claims if the player has BUILDER access to the claim."
                ),
                new Feature(
                        "HOME NAMES", Color.ORANGE,
                        "You can use the character '_' in Home names."
                ),
                new Feature(
                        "/prismshop", Color.BLUE,
                        "Open the Prism & Solar Shop."
                ),
                new Feature(
                        "/credits", Color.BLUE,
                        "Display your credits."
                ),
                new Feature(
                        "/pay <player> <amount>", Color.BLUE,
                        "Give Credits to other players."
                )
        ));

        add(new Instance(Server.SURVIVAL, "v1.0.0", "Survival Release", "The first Survival release, presenting new systems for claiming and warps and many other features!", "2018-07-23",
                new Feature(
                        "SURVIVAL", Color.LIME,
                        "We have an amazing Lobby fitting our OrbitMines lore for Survival created by the entire OrbitMines Team.",
                        "https://i.imgur.com/dBAPVie.jpg"
                ),
                new Feature(
                        "LAND CLAIMING", Color.ORANGE,
                        "We have completely redesigned Land Claiming and it is easy to use and easy to keep an oversight over your claims.",
                        "https://i.imgur.com/zSt7Ekl.jpg"
                ),
                new Feature(
                        "REGIONS", Color.RED,
                        "We have added two types of regions, Under Water and Above Water. 100 Regions have been made teleportable in this update. (125 more that aren't)",
                        "https://i.imgur.com/eMMywzQ.jpg"
                ),
                new Feature(
                        "NETHER & END RESETS", Color.RED,
                        "We have a system that automatically resets the Nether & End on a certain interval.",
                        "https://i.imgur.com/IVduJdP.jpg"
                ),
                new Feature(
                        "PRISM & SOLAR SHOP", Color.YELLOW,
                        "We have added the Prism & Solar Shop to Survival which allows you to use your trade Solars and Prisms with items and currency in Survival.",
                        "https://i.imgur.com/Fl8ggZR.jpg"
                ),
                new Feature(
                        "WARPS", Color.TEAL,
                        "We have created a new Warp System which allows you to have up to 3 Warps.",
                        "https://i.imgur.com/PPnBupx.jpg"
                ),
                new Feature(
                        "CHEST SHOPS", Color.LIME,
                        "Buy and sell items through Chest Shops.",
                        "https://i.imgur.com/TnHMHEG.jpg"
                ),
                new Feature(
                        "/spawn", Color.BLUE,
                        "Teleport to Spawn."
                ),
                new Feature(
                        "/region (number)|random", Color.BLUE,
                        "Open the Region GUI, teleport to a Region or teleport to a Random Region."
                ),
                new Feature(
                        "/claim", Color.BLUE,
                        "Receive the Claiming Tool."
                ),
                new Feature(
                        "/home (name)", Color.BLUE,
                        "Teleport to one of your Homes."
                ),
                new Feature(
                        "/homes", Color.BLUE,
                        "Display all your Homes."
                ),
                new Feature(
                        "/sethome (name)", Color.BLUE,
                        "Set a new Home."
                ),
                new Feature(
                        "/delhome <name>", Color.BLUE,
                        "Delete a Home."
                ),
                new Feature(
                        "/warps", Color.BLUE,
                        "Open the Warps GUI."
                ),
                new Feature(
                        "/mywarps", Color.BLUE,
                        "Open the Your Warp Slots."
                ),
                new Feature(
                        "/accept", Color.BLUE,
                        "Accept an incoming Teleport Request."
                ),
                new Feature(
                        "/tp <player>", Color.BLUE,
                        "Send a teleport request to a player, this feature is for GOLD+."
                ),
                new Feature(
                        "/fly", Color.BLUE,
                        "Enable fly, this only works in your claims and claims where you have Access, this feature is for DIAMOND+."
                ),
                new Feature(
                        "/workbench", Color.BLUE,
                        "Open a workbench, this feature is for DIAMOND+."
                ),
                new Feature(
                        "/enderchest", Color.BLUE,
                        "Open an encherchest, this feature is for EMERALD+."
                ),
                new Feature(
                        "/tphere <player>", Color.BLUE,
                        "Send a teleport here request to a player, this feature is for EMERALD+."
                )
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

            Database.get().update(Table.SERVER_DATA, new Set(TableServerData.DATA, latest.version), new Where(TableServerData.SERVER, server.toString()), new Where(TableServerData.TYPE, "LATEST_PATCH"));
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

        String url = null;
        switch (token) {

            case DEFAULT:
                url = DiscordBot.Images.ORBITMINES_LOGO.getUrl();
                break;
            case SURVIVAL:
                url = DiscordBot.Images.SURVIVAL_LOGO.getUrl();
                break;
        }

        if (url != null) {
            try {
                BufferedImage img = ImageIO.read(new URL(url));
                File file = new File("patch_notes_logo.png");
                ImageIO.write(img, "png", file);

                channel.sendFile(file).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            this.lines = lines;
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
