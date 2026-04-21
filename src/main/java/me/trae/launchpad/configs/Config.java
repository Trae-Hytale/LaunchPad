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
@Configuration(value = "Config", type = ConfigType.JSON)
public class Config {

    @Comment("Whether the launch pad is enabled.")
    private boolean enabled = true;

    @Comment("The block type ID that acts as a launch pad.")
    private String material = "Cloth_Block_Wool_Yellow";

    @Comment("The upward velocity applied to the player on launch.")
    private double launchVelocity = 20.0D;

    @Comment("The maximum Y-distance tolerance for detecting if a player is standing on the block.")
    private double standingTolerance = 0.25D;

    @Comment("The cooldown duration in milliseconds between launches per player.")
    private long cooldownDuration = 500L;

    @Comment("Whether horizontal momentum is preserved on launch. If false, velocity is purely vertical.")
    private boolean preserveHorizontalMomentum = true;

    @Comment("Whether fall damage is cancelled when landing on a launch pad block.")
    private boolean cancelFallDamage = true;
}