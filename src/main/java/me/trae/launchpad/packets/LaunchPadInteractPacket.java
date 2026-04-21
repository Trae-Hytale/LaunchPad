package me.trae.launchpad.packets;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChains;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.io.adapter.PlayerPacketWatcher;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hytale.framework.packet.InboundPacketWatcher;
import io.github.trae.utilities.UtilTime;
import lombok.AllArgsConstructor;
import me.trae.launchpad.configs.Config;
import me.trae.launchpad.configs.ParticleConfig;
import me.trae.launchpad.configs.SoundConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class LaunchPadInteractPacket implements PlayerPacketWatcher, InboundPacketWatcher {

    private final ConcurrentHashMap<UUID, Long> cooldownMap = new ConcurrentHashMap<>();

    private final Config config;
    private final ParticleConfig particleConfig;
    private final SoundConfig soundConfig;

    @Override
    public void accept(@Nonnull final PlayerRef playerRef, @Nonnull final Packet packet) {
        if (!(packet instanceof final SyncInteractionChains syncInteractionChains)) {
            return;
        }

        if (!(this.config.isEnabled())) {
            return;
        }

        for (final SyncInteractionChain chain : syncInteractionChains.updates) {
            if (chain.interactionType != InteractionType.Use) {
                continue;
            }

            if (!(chain.initial)) {
                continue;
            }

            if (chain.data == null || chain.data.blockPosition == null) {
                continue;
            }

            this.handleInteract(playerRef, chain.data.blockPosition);
            break;
        }
    }

    private void handleInteract(final PlayerRef playerRef, final BlockPosition blockPosition) {
        final Ref<EntityStore> playerReference = playerRef.getReference();
        if (playerReference == null) {
            return;
        }

        final UUID worldUuid = playerRef.getWorldUuid();
        if (worldUuid == null) {
            return;
        }

        final World world = Universe.get().getWorld(worldUuid);
        if (world == null) {
            return;
        }

        final Vector3i targetBlock = new Vector3i(blockPosition.x, blockPosition.y, blockPosition.z);

        world.execute(() -> {
            if (!(playerReference.isValid())) {
                return;
            }

            final Store<EntityStore> store = world.getEntityStore().getStore();

            final Player player = store.getComponent(playerReference, Player.getComponentType());
            if (player == null) {
                return;
            }

            if (!(this.isLaunchPadBlock(world, targetBlock))) {
                return;
            }

            final TransformComponent transformComponent = store.getComponent(playerReference, EntityModule.get().getTransformComponentType());
            if (transformComponent == null) {
                return;
            }

            final Vector3d playerPosition = transformComponent.getPosition();
            final double blockTopY = targetBlock.getY() + 1.0D;

            if (Math.abs(playerPosition.getY() - blockTopY) > this.config.getStandingTolerance()) {
                return;
            }

            final Vector3i blockBelow = new Vector3i((int) Math.floor(playerPosition.getX()), (int) Math.floor(playerPosition.getY()) - 1, (int) Math.floor(playerPosition.getZ()));

            if (targetBlock.getX() != blockBelow.getX() || targetBlock.getY() != blockBelow.getY() || targetBlock.getZ() != blockBelow.getZ()) {
                return;
            }

            if (this.config.getCooldownDuration() > 0L) {
                if (this.hasCooldown(playerRef)) {
                    return;
                }
            }

            final Vector3d targetBlockCenter = new Vector3d(targetBlock.getX() + 0.5D, targetBlock.getY() + 1.0D, targetBlock.getZ() + 0.5D);

            if (this.particleConfig.isEnabled()) {
                this.handleParticle(store, targetBlockCenter);
            }

            if (this.soundConfig.isEnabled()) {
                this.handleSound(store, targetBlockCenter);
            }

            this.handleVelocity(store, playerReference);
        });
    }

    private void handleParticle(final Store<EntityStore> store, final Vector3d position) {
        final List<Ref<EntityStore>> nearbyPlayers = new ArrayList<>();

        store.getResource(EntityModule.get().getPlayerSpatialResourceType()).getSpatialStructure().collect(position, 75.0D, nearbyPlayers);

        final String particleId = this.particleConfig.getParticleId();

        final Color color;

        if (this.particleConfig.isColorOverride()) {
            color = new Color((byte) this.particleConfig.getColorRed(), (byte) this.particleConfig.getColorGreen(), (byte) this.particleConfig.getColorBlue());
        } else {
            final BlockType blockType = BlockType.fromString(this.config.getMaterial());
            if (blockType != null && blockType.getBlockParticleSetId() != null) {
                color = blockType.getParticleColor();
            } else {
                color = null;
            }
        }

        ParticleUtil.spawnParticleEffect(
                particleId,
                position.getX(), position.getY(), position.getZ(),
                this.particleConfig.getRotationYaw(),
                this.particleConfig.getRotationPitch(),
                this.particleConfig.getRotationRoll(),
                this.particleConfig.getScale(),
                color,
                null,
                nearbyPlayers,
                store
        );
    }

    private void handleSound(final Store<EntityStore> store, final Vector3d position) {
        final int soundIndex = SoundEvent.getAssetMap().getIndex(this.soundConfig.getSoundId());
        if (soundIndex == 0 || soundIndex == Integer.MIN_VALUE) {
            return;
        }

        SoundUtil.playSoundEvent3d(
                soundIndex,
                SoundCategory.SFX,
                position.getX(), position.getY(), position.getZ(),
                this.soundConfig.getVolume(),
                this.soundConfig.getPitch(),
                store
        );
    }

    private void handleVelocity(final Store<EntityStore> store, final Ref<EntityStore> playerReference) {
        final Velocity velocity = store.getComponent(playerReference, Velocity.getComponentType());
        if (velocity == null) {
            return;
        }

        final Vector3d vector3d = this.config.isPreserveHorizontalMomentum() ? new Vector3d(velocity.getX(), this.config.getLaunchVelocity(), velocity.getZ()) : new Vector3d(0.0D, this.config.getLaunchVelocity(), 0.0D);

        velocity.addInstruction(vector3d, null, ChangeVelocityType.Set);
    }

    private boolean isLaunchPadBlock(final World world, final Vector3i block) {
        final BlockType launchPadBlockType = BlockType.fromString(this.config.getMaterial());
        if (launchPadBlockType == null) {
            return false;
        }

        return world.getBlock(block) == BlockType.getAssetMap().getIndex(launchPadBlockType.getId());
    }

    private boolean hasCooldown(final PlayerRef playerRef) {
        final Long systemTime = this.cooldownMap.get(playerRef.getUuid());
        if (systemTime != null) {
            if (!(UtilTime.elapsed(systemTime, this.config.getCooldownDuration()))) {
                return true;
            }

            this.cooldownMap.remove(playerRef.getUuid());
        } else {
            this.cooldownMap.put(playerRef.getUuid(), System.currentTimeMillis());
        }

        return false;
    }
}