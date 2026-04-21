# LaunchPad

A configurable launch pad plugin for Hytale servers. Stand on a block, press interact, and get blasted into the sky.

---

## Features

- Configurable launch pad block type — any block in the game
- Adjustable launch velocity with optional horizontal momentum preservation
- Customizable particle effects with scale, rotation, and color override support
- Customizable sound effects with volume and pitch control
- Configurable fall damage cancellation when landing on a launch pad block
- Per-player cooldown system
- Block-below validation — only triggers when the interacted block is the one you're standing on

---

## How It Works

1. Stand on or near the configured block
2. Look down at it and press **F** (Use/Interact)
3. Get launched into the sky
4. Land on any launch pad block without taking fall damage

---

## Configuration

All configuration files are generated in the plugin's data directory on first run.

### Config.json

| Field | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `true` | Whether the launch pad is enabled |
| `material` | string | `Cloth_Block_Wool_Yellow` | The block type ID that acts as a launch pad |
| `launchVelocity` | double | `20.0` | The upward velocity applied to the player |
| `standingTolerance` | double | `1.5` | Maximum Y-distance tolerance for standing detection |
| `cooldownDuration` | long | `500` | Cooldown in milliseconds between launches per player |
| `preserveHorizontalMomentum` | boolean | `true` | Whether horizontal momentum is preserved on launch |
| `cancelFallDamage` | boolean | `true` | Whether fall damage is cancelled when landing on a launch pad block |

### Particle.json

| Field | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `true` | Whether particles are enabled on launch |
| `particleId` | string | `Hytale/DirtBreak` | The particle system ID |
| `scale` | float | `1.5` | Scale multiplier for the particle effect |
| `rotationYaw` | float | `0.0` | Yaw rotation in radians |
| `rotationPitch` | float | `0.0` | Pitch rotation in radians |
| `rotationRoll` | float | `0.0` | Roll rotation in radians |
| `colorOverride` | boolean | `false` | Whether to override the particle color |
| `colorRed` | int | `255` | Red component (0-255), only used when colorOverride is true |
| `colorGreen` | int | `255` | Green component (0-255), only used when colorOverride is true |
| `colorBlue` | int | `255` | Blue component (0-255), only used when colorOverride is true |

When `colorOverride` is `false`, the particle color is resolved from the configured block type's asset definition.

### Sound.json

| Field | Type | Default | Description |
|---|---|---|---|
| `enabled` | boolean | `true` | Whether the sound is enabled on launch |
| `soundId` | string | `SFX_Cloth_Hit` | The sound event ID |
| `volume` | float | `1.0` | Volume multiplier |
| `pitch` | float | `1.0` | Pitch multiplier |

---

## Built-in Dependencies

- [Hytale-Plugin-Framework](https://github.com/Trae-Maven/hytale-plugin-framework)

---

## Installation

1. Download the latest `LaunchPad.jar`
2. Place it in your server's `mods/` directory
3. Start the server
4. Configure via the generated JSON files in the plugin's data directory

---

## Building

```
mvn clean package
```

The output JAR will be in `target/`.
