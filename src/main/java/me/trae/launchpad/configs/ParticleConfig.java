package me.trae.launchpad.configs;

import io.github.trae.di.configuration.annotations.Comment;
import io.github.trae.di.configuration.annotations.Configuration;
import io.github.trae.di.configuration.enums.ConfigType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Configuration(value = "Particle", type = ConfigType.JSON)
public class ParticleConfig {

    @Comment("Whether particles are enabled on launch.")
    private boolean enabled = true;

    @Comment("The particle system ID to spawn on launch. Default matches the cloth block break particle.")
    private String particleId = "Block_Land_Hard_Crystal";

    @Comment("The scale multiplier for the particle effect.")
    private float scale = 1.5F;

    @Comment("The yaw rotation of the particle effect in radians.")
    private float rotationYaw = 0.0F;

    @Comment("The pitch rotation of the particle effect in radians.")
    private float rotationPitch = 0.0F;

    @Comment("The roll rotation of the particle effect in radians.")
    private float rotationRoll = 0.0F;

    @Comment("Whether to override the particle color. If false, the block's color are used.")
    private boolean colorOverride = false;

    @Comment("The red component of the particle color (0-255). Only used when colorOverride is true.")
    private int colorRed = 255;

    @Comment("The green component of the particle color (0-255). Only used when colorOverride is true.")
    private int colorGreen = 255;

    @Comment("The blue component of the particle color (0-255). Only used when colorOverride is true.")
    private int colorBlue = 255;
}