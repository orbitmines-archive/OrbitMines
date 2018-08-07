package com.orbitmines.spigot.api.nms.anvilgui;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface AnvilNms {

    void open();

    HashMap<AnvilSlot, ItemStack> getItems();

    enum AnvilSlot {

        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }
    }

    class AnvilClickEvent {

        private AnvilSlot slot;

        private String name;

        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name) {
            this.slot = slot;
            this.name = name;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    interface AnvilClickEventHandler {
        void onAnvilClick(AnvilClickEvent event);
    }

    abstract class AnvilCloseEvent {

        public abstract void onClose();

    }
}
