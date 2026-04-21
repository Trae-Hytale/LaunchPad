package me.trae.launchpad.cooldown;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.trae.di.annotations.method.PostConstruct;
import io.github.trae.di.annotations.type.component.Service;
import io.github.trae.hytale.framework.utility.UtilTask;
import io.github.trae.utilities.UtilTime;
import lombok.AllArgsConstructor;
import me.trae.launchpad.configs.Config;
import me.trae.launchpad.cooldown.interfaces.ICooldownManager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class CooldownManager implements ICooldownManager {

    private final ConcurrentHashMap<UUID, Long> cooldownMap = new ConcurrentHashMap<>();

    private final Config config;

    @PostConstruct
    public void onPostConstruct() {
        UtilTask.schedule(() -> this.cooldownMap.values().removeIf(systemTime -> UtilTime.elapsed(systemTime, this.config.getCooldownDuration())), 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void addCooldown(final PlayerRef playerRef) {
        this.cooldownMap.put(playerRef.getUuid(), System.currentTimeMillis());
    }

    @Override
    public boolean hasCooldown(final PlayerRef playerRef) {
        return Optional.ofNullable(this.cooldownMap.get(playerRef.getUuid())).map(systemTime -> !(UtilTime.elapsed(systemTime, this.config.getCooldownDuration()))).orElse(false);
    }
}