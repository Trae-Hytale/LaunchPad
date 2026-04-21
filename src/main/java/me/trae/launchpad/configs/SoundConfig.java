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
@Configuration(value = "Sound", type = ConfigType.JSON)
public class SoundConfig {

    @Comment("Whether the sound is enabled on launch.")
    private boolean enabled = true;

    @Comment("The sound event ID to play on launch. Default matches the cloth block break sound.")
    private String soundId = "SFX_Cloth_Break";

    @Comment("The volume multiplier for the sound effect.")
    private float volume = 1.0F;

    @Comment("The pitch multiplier for the sound effect.")
    private float pitch = 9.0F;
}