package com.orbitmines.spigot.servers.hub.gui.punishments;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.api.CachedPlayer;
import com.orbitmines.api.Message;
import com.orbitmines.api.punishment.Punishment;
import com.orbitmines.api.punishment.PunishmentHandler;
import com.orbitmines.api.punishment.offences.Offence;
import com.orbitmines.api.punishment.offences.Severity;
import com.orbitmines.api.utils.DateUtils;
import com.orbitmines.spigot.api.handlers.GUI;
import com.orbitmines.spigot.api.handlers.OMPlayer;
import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import com.orbitmines.spigot.api.handlers.itembuilders.PlayerSkullBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class PunishGUI extends GUI {

    private final CachedPlayer player;
    private Type type;

    public PunishGUI(CachedPlayer player) {
        this.player = player;
        this.type = Type.SMALL;

        newInventory(45, "§0§lPunish (" + player.getPlayerName() + ")");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        PunishmentHandler handler = player.getPunishmentHandler();
        handler.update();

        for (Offence offence : Offence.values()) {
            int slot = getSlot(omp, offence);

            Punishment active = handler.getActivePunishment(offence);
            if (active != null && active.getPunishedBy().getUUID().toString().equals(omp.getUUID().toString())) {
                CachedPlayer punishedBy = active.getPunishedBy();

                ItemBuilder builder = new ItemBuilder(Material.BARRIER, 1,
                        "§c§l" + omp.lang("Ontzeg Straf", "Pardon Player"),
                        "",
                        "§7" + omp.lang("Overtreding", "Offence") + ": §c§l" + omp.lang(offence.getName()),
                        "§7" + omp.lang("Zwaarte", "Severity") + ": §c§l" + omp.lang(active.getSeverity().getName()),
                        "§7" + omp.lang("Van", "From") + ": §c§l" + active.getFromString(type.format));

                if (active.getSeverity().getDuration() == Punishment.Duration.PERMANENT) {
                    builder.addLore("§7" + omp.lang("Tot", "To") + ": §c§lPERMANENT");
                } else {
                    builder.addLore("§7" + omp.lang("Tot", "To") + ": §c§l" + active.getToString(type.format));
                    builder.addLore("§7" + omp.lang("Verloopt over", "Expires in") + ": §c§l" + active.getExpireInString(omp.getLanguage()));
                }

                builder.addLore("§7" + omp.lang("Gestraft door", "Punished by") + ": " + punishedBy.getRankPrefix() + punishedBy.getPlayerName());
                builder.addLore("§7" + omp.lang("Reden", "Reason") + ": §c§l" + active.getReason());

                add(slot + getSlotAddition(offence), new ItemInstance(builder.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        new PardonReasonGUI(player, active).open(omp);
                    }
                });
            }

            ItemBuilder builder = getIcon(omp, offence);
            builder.addLore("");
            builder.addLore("§7§l" + omp.lang("Geschiedenis", "History"));

            /* Add history to lore */
            List<Punishment> punishments = handler.getPunishments(offence, true);
            if (punishments.size() == 0) {
                builder.addLore("§7§o" + omp.lang("Geen geschiedenis om weer te geven", "No history to display") + ".");
            } else {
                /* old -> new, new -> old */
                Collections.reverse(punishments);

                for (Punishment punishment : punishments) {
                    CachedPlayer punishedBy = punishment.getPunishedBy();

                    builder.addLore(
                            (punishment.isPardoned() ? "§a§l" + omp.lang("VERGEVEN", "PARDONED") + "§r" :
                                    (punishment.getSeverity() == Severity.WARNING ? "§8§l" + omp.lang("WAARSCHUWING", "WARNING") :
                                            (punishment.hasExpired() ? "§c§l" + omp.lang("VERLOPEN", "EXPIRED") + "§r" :
                                                    "§4§l" + omp.lang("ACTIEF", "ACTIVE") + "§r"
                                            )
                                    )
                            ) +
                            " §7| §c" + omp.lang(punishment.getSeverity().getName()) +
                            " §7| §c" + (punishment.getSeverity().getDuration() == Punishment.Duration.PERMANENT ? "§lPERMANENT§r" : punishment.getFromString(type.format) + (punishment.getSeverity() != Severity.WARNING ? "§7 - §c" + punishment.getToString(type.format) : "")));
                    builder.addLore("    §7" + omp.lang("Gestraft door", "Punished by") + (punishment.getSeverity().getDuration() == Punishment.Duration.PERMANENT ? " §7(§c" + punishment.getFromString(type.format) + "§7)" : "") + ": " + punishedBy.getRankPrefix() + punishedBy.getPlayerName() + "§7: §c" + punishment.getReason());

                    if (punishment.isPardoned()) {
                        CachedPlayer pardonedBy = punishment.getPardonedBy();
                        builder.addLore("    §7" + omp.lang("Gestraft door", "Pardoned by") + ": " + pardonedBy.getRankPrefix() + pardonedBy.getPlayerName() +
                                " §7(§c" + punishment.getPardonedOnString(type.format) +
                                "§7): §c" + punishment.getPardonedReason());
                    }
                }
            }

            if (handler.getActivePunishment(offence.getType()) == null)
                add(slot, new ItemInstance(builder.build()) {
                    @Override
                    public void onClick(InventoryClickEvent event, OMPlayer omp) {
                        switch (offence) {

                            /* First select severity */
                            case GAME_PLAY:
                            case CHAT:
                            case HACKING:
                                new SeverityGUI(player, offence).open(omp);
                                break;
                            /* Straight to confirmation */
                            case SKIN:
                                new ConfirmPunishGUI(player, offence, Severity.SEV_3, new Message("Ongepaste Skin (Geef aan op Discord wanneer je jouw skin hebt aangepast)", "Inappropriate Skin (Appeal on Discord after changing your skin)").lang(player.getLanguage())).open(omp);
                                break;
                            case NAME:
                                new ConfirmPunishGUI(player, offence, Severity.SEV_3, new Message("Ongepaste Naam (Geef aan op Discord wanneer je jouw naam hebt aangepast)", "Inappropriate Name (Appeal on discord after changing your name)").lang(player.getLanguage())).open(omp);
                                break;
                            /* Straight to reason */
                            case NETWORK_BAN:
                                new PunishReasonGUI(player, offence, Severity.SEV_3).open(omp);
                                break;
                        }
                    }
                });
            else
                add(slot, new EmptyItemInstance(builder.build()));

            add(4, 8, new ItemInstance(type.getItemBuilder().clone().setDisplayName("§7" + omp.lang(type.name)).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    type = type.next();
                    reopen(omp);
                }
            });
        }

        return true;
    }

    private ItemBuilder getIcon(OMPlayer omp, Offence offence) {
        String name = omp.lang(offence.getName());

        switch (offence) {

            case GAME_PLAY:
                return new ItemBuilder(Material.GRASS, 1, "§c§l" + name);
            case CHAT:
                return new ItemBuilder(Material.WRITABLE_BOOK, 1, "§c§l" + name);
            case HACKING:
                return new ItemBuilder(Material.DIAMOND_SWORD, 1, "§c§l" + name).addFlag(ItemFlag.HIDE_ATTRIBUTES);
            case SKIN:
                return new PlayerSkullBuilder(player::getPlayerName, 1, "§c§l" + name);
            case NAME:
                return new ItemBuilder(Material.NAME_TAG, 1, "§c§l" + name);
            case NETWORK_BAN:
                return new ItemBuilder(Material.PISTON, 1, "§c§l" + name);
            default:
                return new ItemBuilder(Material.STONE, 1, "§c§l" + name);
        }
    }

    private int getSlot(OMPlayer omp, Offence offence) {
        switch (offence) {

            case GAME_PLAY:
                return 11;
            case CHAT:
                return 13;
            case HACKING:
                return 15;
            case SKIN:
                return 29;
            case NAME:
                return 31;
            case NETWORK_BAN:
                return 33;
            default:
                return 0;
        }
    }

    private int getSlotAddition(Offence offence) {
        switch (offence) {

            case SKIN:
            case NAME:
            case NETWORK_BAN:
                return 9;
            default:
                return -9;
        }
    }

    enum Type {

        NORMAL(new Message("Normale Timestamps", "Normal Timestamps"), DateUtils.FORMAT, new ItemBuilder(Material.CLOCK)),
        SMALL(new Message("Kleine Timestamps", "Small Timestamps"), DateUtils.SIMPLE_FORMAT, new ItemBuilder(Material.CLOCK).glow());

        private final Message name;
        private final SimpleDateFormat format;
        private final ItemBuilder itemBuilder;

        Type(Message name, SimpleDateFormat format, ItemBuilder itemBuilder) {
            this.name = name;
            this.format = format;
            this.itemBuilder = itemBuilder;
        }

        public Message getName() {
            return name;
        }

        public SimpleDateFormat getFormat() {
            return format;
        }

        public ItemBuilder getItemBuilder() {
            return itemBuilder;
        }

        public Type next() {
            Type[] values = values();

            if (ordinal() == values.length -1)
                return values[0];

            return values[ordinal() + 1];
        }
    }
}
