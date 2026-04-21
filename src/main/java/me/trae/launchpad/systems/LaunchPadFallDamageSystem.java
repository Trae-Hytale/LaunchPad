package me.trae.launchpad.systems;

import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.system.CustomEntityEventSystem;
import io.github.trae.hytale.framework.system.data.SystemContext;
import me.trae.launchpad.configs.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

@Component
public class LaunchPadFallDamageSystem extends CustomEntityEventSystem<Damage> {

    private final Config config;

    public LaunchPadFallDamageSystem(final Config config) {
        super(Damage.class);

        this.config = config;
    }

    @Nullable
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getGatherDamageGroup();
    }

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return Collections.singleton(RootDependency.first());
    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }

    @Override
    public void onEvent(final Damage event, final SystemContext<EntityStore> systemContext) {
        if (event.isCancelled()) {
            return;
        }

        if (!(this.config.isEnabled())) {
            return;
        }

        if (!(this.config.isCancelFallDamage())) {
            return;
        }

        if (event.getDamageCauseIndex() != DamageCause.getAssetMap().getIndex("Fall")) {
            return;
        }

        final Player player = systemContext.getComponent(Player.getComponentType());
        if (player == null) {
            return;
        }

        final World world = player.getWorld();
        if (world == null) {
            return;
        }

        final TransformComponent transformComponent = systemContext.getComponent(EntityModule.get().getTransformComponentType());
        if (transformComponent == null) {
            return;
        }

        if (!(this.isNearLaunchPadBlock(world, transformComponent.getPosition()))) {
            return;
        }

        event.setAmount(0.0F);

        event.setCancelled(true);
    }

    private boolean isNearLaunchPadBlock(final World world, final Vector3d playerPosition) {
        final BlockType launchPadBlockType = BlockType.fromString(this.config.getMaterial());
        if (launchPadBlockType == null) {
            return false;
        }

        final int launchPadBlockId = BlockType.getAssetMap().getIndex(launchPadBlockType.getId());

        final int playerX = (int) Math.floor(playerPosition.getX());
        final int playerY = (int) Math.floor(playerPosition.getY());
        final int playerZ = (int) Math.floor(playerPosition.getZ());

        for (int y = playerY; y >= playerY - 3; y--) {
            if (world.getBlock(new Vector3i(playerX, y, playerZ)) == launchPadBlockId) {
                return true;
            }
        }

        return false;
    }
}