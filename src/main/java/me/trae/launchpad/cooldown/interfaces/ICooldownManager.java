package me.trae.launchpad.cooldown.interfaces;

import com.hypixel.hytale.server.core.universe.PlayerRef;

public interface ICooldownManager {

    void addCooldown(final PlayerRef playerRef);

    boolean hasCooldown(final PlayerRef playerRef);
}