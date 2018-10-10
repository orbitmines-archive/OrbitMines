package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;

import java.util.Map;
import java.util.Set;

public interface KitItem<T> {

    Map<Passive, Integer> getPassives();

    Map<Passive, Integer> getActives();

    T addPassive(Passive passive, Integer integer);

    T addActive(Active active, Integer integer);

    T applyNewPassive(Set<Passive> newPassives);

    T applyRemovedPassive(Set<Passive> removedPassives);

    T applyNewActives(Set<Active> newActives);

    T applyRemovedActive(Set<Active> removedActives);

}
