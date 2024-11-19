package org.bsdevelopment.mobfarming;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLPaths;
import org.bsdevelopment.mobfarming.config.Config;
import org.bsdevelopment.mobfarming.config.MobSpawnConfig;
import org.bsdevelopment.mobfarming.utilities.MobSpawner;

import java.nio.file.Path;

public class ModConstants {
    public static final String MOD_ID = "mob_farming";
    public static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    public static final Path DEATHROOT_JSON_FILE = CONFIG_DIR.resolve("deathroot-mobs.json");
    private static DamageSource SPIKE_DAMAGE;

    public static final ResourceKey<DamageType> SPIKE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "mob_spike"));

    public static final GameProfile FAKE_PROFILE = new GameProfile(Config.fakePlayerUUID, Component.translatable("mob_blender.fakeplayer").getString());

    public static final MobSpawnConfig DEATHROOT_SPAWN_CONFIG = MobSpawnConfig.loadConfig(DEATHROOT_JSON_FILE,
            // Default Overworld Mobs
            new MobSpawnConfig.SpawnEntry("minecraft:spider", 4, 4, 100),
            new MobSpawnConfig.SpawnEntry("minecraft:zombie", 4, 4, 95),
            new MobSpawnConfig.SpawnEntry("minecraft:zombie_villager", 1, 1, 5),
            new MobSpawnConfig.SpawnEntry("minecraft:skeleton", 4, 4, 100),
            new MobSpawnConfig.SpawnEntry("minecraft:creeper", 4, 4, 100),
            new MobSpawnConfig.SpawnEntry("minecraft:slime", 4, 4, 100),
            new MobSpawnConfig.SpawnEntry("minecraft:enderman", 1, 4, 10),
            new MobSpawnConfig.SpawnEntry("minecraft:witch", 1, 1, 5),

            // Default Nether Mobs
            new MobSpawnConfig.SpawnEntry("minecraft:blaze", 1, 1, 5).setDimensions("minecraft:the_nether"),
            new MobSpawnConfig.SpawnEntry("minecraft:wither_skeleton", 1, 1, 5).setDimensions("minecraft:the_nether"),
            new MobSpawnConfig.SpawnEntry("minecraft:magma_cube", 1, 1, 5).setDimensions("minecraft:the_nether"),
            new MobSpawnConfig.SpawnEntry("minecraft:zombified_piglin", 1, 1, 5).setDimensions("minecraft:the_nether")
    );
    public static final MobSpawner MOB_SPAWNER = new MobSpawner(DEATHROOT_SPAWN_CONFIG);

    public static DamageSource getSpikeDamage(Level level) {
        if (SPIKE_DAMAGE == null)
            SPIKE_DAMAGE = new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModConstants.SPIKE_TYPE));
        return SPIKE_DAMAGE;
    }
}
